package ai.obvs.services.ocr;

import org.springframework.beans.factory.annotation.Value;

public class OCRProperties {

    @Value("${ocr.baseDirectory}")
    private String baseDirectory;

    @Value("${ocr.pythonInstallationDirectory}")
    private String pythonInstallationDirectory;

    @Value("${ocr.modelDirectory}")
    private String modelDirectory;

    @Value("${ocr.mlkitDirectory}")
    private String mlkitDirectory;

    @Value("${ocr.pythonSitePackageDirectory}")
    private String pythonSitePackageDirectory;

    public String getBaseDirectory() {
        return baseDirectory;
    }

    public String getPythonInstallationDirectory() {
        return pythonInstallationDirectory;
    }

    public String getModelDirectory() {
        return modelDirectory;
    }

    public String getPythonSitePackageDirectory() {
        return pythonSitePackageDirectory;
    }

    public String getMlkitDirectory() {
        return mlkitDirectory;
    }
}
