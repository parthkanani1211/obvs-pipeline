package ai.obvs.services.ocr;

import ai.obvs.services.ocr.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import liquibase.pro.packaged.C;
import liquibase.pro.packaged.I;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ExtractDataService {

    private static final Logger log = LogManager.getLogger(ExtractDataService.class);

    private final String CONST_OUTPUT = "Result";
    private final String CONST_FIELDS = "Fields";
    private final String CONST_CLASSIFICATION = "Classification";
    private final String CONST_OUTPUT_CSV = "Output.csv";
    private final String CONST_EXTRACT = "Extract";
    private final String CONST_TABLES = "Table";

    @Autowired
    private OCRProperties ocrProperties;

    @Autowired
    OcrEngine ocrEngine;

    public void extractData(String inputFilepath) throws IOException {
        String baseDirectory = ocrProperties.getBaseDirectory();

        String fileFullName = FilenameUtils.getName(inputFilepath);
        String fileNameWithoutExtension = FilenameUtils.removeExtension(fileFullName);

        Path outputDirectory = Paths.get(baseDirectory).resolve(CONST_OUTPUT);
        Path outputPathPDFFileName = outputDirectory.resolve(fileNameWithoutExtension);

        File file = new File(outputPathPDFFileName.toString());
        file.mkdirs();

        if (Files.exists(outputPathPDFFileName)) {
            Files.copy(Paths.get(inputFilepath), outputPathPDFFileName.resolve(fileFullName), StandardCopyOption.REPLACE_EXISTING);
//          detectControls(inputFilepath);
            List<ImageFileResult> imageFileResults = processResults(outputPathPDFFileName);

            Result result = new Result();
            result.setFileName(fileFullName);
            result.setImageFileResults(imageFileResults);
            result.setResultFolderPath(outputPathPDFFileName.toString());
            writeToJson(outputPathPDFFileName, fileNameWithoutExtension, result);

        } else {
            throw new IllegalArgumentException("Unable to detect fields and tables");
        }
    }

    private void detectControls(String inputFilePath) {
        String modelDir = ocrProperties.getModelDirectory();
        String pythonInstallDir = ocrProperties.getPythonInstallationDirectory();

        String pythonFileFullPath = Paths.get(modelDir, "pysrc", "label_image.py").toString();
        String pythonExecFileFullPath = Paths.get(pythonInstallDir, "python").toString();

        //command to do pass parameters
        List<String> commands = new ArrayList<String>();
        commands.add(pythonExecFileFullPath); // python command
        commands.add(pythonFileFullPath); // command
        commands.add("--image");
        commands.add(String.valueOf(Paths.get(inputFilePath)));
        commands.add("--graph");
        commands.add(String.valueOf(Paths.get(modelDir, "tensorflow.pb")));
        commands.add("--labels");
        commands.add(String.valueOf(Paths.get(modelDir, "labels.txt")));

        String highestScoreString = null;
        try {

            ProcessBuilder processBuilder = new ProcessBuilder(commands);
            String sitePackageDir = ocrProperties.getPythonSitePackageDirectory();
            processBuilder.environment().put("PYTHONPATH", sitePackageDir);
            Process p = processBuilder.start();

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader error = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            String s = null;
            String e = null;
            Float highestScoreValue = new Float(0.0);
            while ((e = error.readLine()) != null) {
                log.error(e);
            }
            while ((s = stdInput.readLine()) != null) {
                log.debug(e);
//                if (s.contains("VIDNEW") || s.contains("VIDOLD"))
//                    if (highestScoreValue < Float.parseFloat(s.split(",")[1])) {
//                        highestScoreValue = Float.parseFloat(s.split(",")[1]);
//                        highestScoreString = s.split(",")[0];
//                    }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<ImageFileResult> processResults(Path outputPathPDFFileName) throws IOException {

        List<ImageFileResult> imageFileResultList = new ArrayList<>();
        for (File file : outputPathPDFFileName.toFile().listFiles()) {
            if (file.isDirectory()) {
                ImageFileResult imageFileResult = new ImageFileResult();
                String pageFileName = file.getName();
                Path outputPathPageFileName = Paths.get(file.getAbsolutePath()); //outputPathPDFFileName.resolve(pageFileName);

                Classification classification = processClassificationResult(outputPathPageFileName);
                if (classification.getResult().toLowerCase().equals("pass")) {
                    ExtractData extractData = extractData(outputPathPageFileName);
                    imageFileResult.setData(extractData);
                }
                imageFileResult.setFileName(pageFileName);
                imageFileResult.setClassification(classification);
                imageFileResultList.add(imageFileResult);
            }
        }

        return imageFileResultList;
    }

    private Classification processClassificationResult(Path outputPathPageFileName) {
        Classification classification = new Classification();
        Path outputClassificationFolderPath = outputPathPageFileName.resolve(CONST_CLASSIFICATION);
        Path csvOutputFilePath = outputClassificationFolderPath.resolve(CONST_OUTPUT_CSV);
        if (Files.exists(csvOutputFilePath)) {
            try {
                List<String> lines = Files.readAllLines(csvOutputFilePath);
                lines.forEach(line -> {
                    String[] splitStrings = line.split(",");

                    if (splitStrings.length == 4) {
                        classification.setClassId(splitStrings[1]);
                        classification.setAccuracy(splitStrings[2]);
                        classification.setResult(splitStrings[3]);
                    }

                });
            } catch (IOException e) {
                log.error(e);
            }

        }
        return classification;
    }

    private ExtractData extractData(Path outputPathPageFileName) {
        Path outputExtractFolderPath = outputPathPageFileName.resolve(CONST_EXTRACT);
        List<Field> fields = doOCR_Fields(outputExtractFolderPath);
        List<Table> tables = doOCR_Tables(outputExtractFolderPath);
        ExtractData extractData = new ExtractData();
        extractData.setFields(fields);
        if (tables.size() > 0)
            extractData.setTable(tables.get(0));

        return extractData;
    }

    private List<Field> doOCR_Fields(Path outputPathPageFileName) {
        List<Field> ocrFieldsData = new ArrayList<>();
        Path fieldsPath = outputPathPageFileName.resolve(CONST_FIELDS);
        try {
            Files.walk(fieldsPath)
                    .filter(Files::isRegularFile)
                    .forEach(file -> {
                        try {
                            String filePath = file.toString();
                            String fileName = FilenameUtils.getName(filePath);
                            String extension = FilenameUtils.getExtension(file.toString()).toLowerCase();
                            String fileNameWithoutExtension = FilenameUtils.removeExtension(fileName);
//                        if (FilenameUtils.getExtension(file.toString()).equals("txt")) {
//                            List<String> lines = Files.readAllLines(file);
//                            lines.forEach(line -> {
//                                String[] splitStrings = line.split(":");
//                                if(splitStrings.length> 1){
//
//                                }
//                            });
//                        }
                            if (extension.equals("jpeg") || extension.equals("png")) {
                                List<String> resultStrings = ocrEngine.ocr(filePath);
                                String[] fileNames = fileNameWithoutExtension.split("_");
                                if (fileNames.length > 0) {
                                    String fieldName = fileNames[0];
                                    Field field = new Field();
                                    field.setName(fieldName);
                                    String rawValue = resultStrings.get(0);
                                    String[] splitStrings = rawValue.split(":");
                                    String value = rawValue;
                                    if (splitStrings.length > 1) {
                                        String data = splitStrings[1].trim();
                                        value = data.isBlank() ? rawValue : data;
                                    }
                                    field.setValue(value);
                                    ocrFieldsData.add(field);
                                }
                            }
                        } catch (Exception e) {
                            log.error(e);
                        }
                    });
        } catch (IOException ex) {
            log.error(ex);
        }
        return ocrFieldsData;
    }

    private List<Table> doOCR_Tables(Path detectedControlFolderPath) {
        Path tablesPath = detectedControlFolderPath.resolve(CONST_TABLES);
        List<Table> tables = new ArrayList<>();

        if (Files.exists(tablesPath)) {
            try {
                Files.walk(tablesPath)
                        .filter(Files::isRegularFile)
                        .forEach(file -> {
                            if (FilenameUtils.getExtension(file.toString()).equals("json")) {
                                try {
                                    AtomicInteger index = new AtomicInteger(0);
                                    Table table = new Table();
                                    List<List<String>> rows = new ArrayList<>();
                                    String fileData = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
                                    JSONObject jsonObject = (JSONObject) new JSONParser().parse(fileData);
                                    JSONArray jsonArray = (JSONArray) jsonObject.get("data");
                                    jsonArray.forEach(item -> {
                                        try {
                                            List<String> stringList = new ArrayList<>();
                                            JSONObject jsonElementObject = (JSONObject) new JSONParser().parse(item.toString());
                                            Set<Map.Entry<String, JSONObject>> entrySet = jsonElementObject.entrySet();
                                            for (Map.Entry<String, JSONObject> entry : entrySet) {
                                                String value = String.valueOf(entry.getValue());
                                                stringList.add(value);
                                            }
                                            index.getAndIncrement();
                                            if (index.get() == 1) {
                                                table.setHeaders(stringList);
                                            } else {
//                                                Row row = new Row();
//                                                row.setValues(stringList);
                                                rows.add(stringList);
                                            }
                                        } catch (Exception e) {
                                            log.error(e);
                                        }
                                    });
                                    table.setName(file.getFileName().toString());
                                    table.setRows(rows);
                                    tables.add(table);
                                } catch (IOException | ParseException e) {
                                    log.error(e);
                                }
                            }
                        });
            } catch (IOException e) {
                log.error(e);
            }
        }
        log.error("Data not extracted.");
        return tables;
    }

    private void writeToJson(Path outputPath, String fileNameWithoutExtension, Result result) {
        Path jsonOutputPath = outputPath.resolve(fileNameWithoutExtension + ".json");
        try {
            Files.deleteIfExists(jsonOutputPath);
            FileWriter fileWriter = new FileWriter(jsonOutputPath.toString());
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(fileWriter, result);
        } catch (IOException e) {
            log.error(e);
        }
    }
}
