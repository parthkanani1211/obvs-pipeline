package ai.obvs.controllers;

import ai.obvs.services.FileStorageService;
import ai.obvs.services.ocr.ExtractDataService;
import ai.obvs.services.ocr.OcrEngine;
import org.apache.commons.io.FilenameUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RequestMapping("/v1/aiblock")
public class OCRController {

    @Autowired
    private OcrEngine ocrEngine;

    @Autowired
    private ExtractDataService extractDataService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/ocr")
    public ResponseEntity<?> post(@RequestParam("file") MultipartFile multipartFile) {
        try {
            String imagePath = getFilePath(multipartFile);
            List<String> stringList = ocrEngine.ocr(imagePath);
            return ResponseEntity.ok(stringList.get(0));
        } catch (IOException e) {
            throw new IllegalArgumentException("Please select file to do OCR" + e.toString());
        }
    }

    @PostMapping("/extract")
    public ResponseEntity<?> processFile(@RequestParam("file") MultipartFile multipartFile) {
        try {
            String filePath = getFilePath(multipartFile);
            extractDataService.extractData(filePath);
            return ResponseEntity.ok("");
        } catch (IOException e) {
            throw new IllegalArgumentException("Please select file to do OCR" + e.toString());
        }
    }

    private String getFilePath(MultipartFile multipartFile) throws IOException {
        byte[] multipartFileBytes = multipartFile.getBytes();
        String name = multipartFile.getOriginalFilename();
        if (FilenameUtils.getExtension(name).toLowerCase().equals("pdf")) {
            String nameWithoutExtension = FilenameUtils.removeExtension(name);
            String fileName = name; //nameWithoutExtension + "_" + DateTime.now().getSecondOfDay() + ".pdf";
            String filePath = fileStorageService.storeFile("input/images", fileName, multipartFileBytes);
            return filePath;
        }
        throw new IllegalArgumentException("Invalid File. Please select any *.pdf file.");

    }

}
