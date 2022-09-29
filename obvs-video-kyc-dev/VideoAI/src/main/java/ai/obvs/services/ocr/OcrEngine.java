package ai.obvs.services.ocr;

import java.io.IOException;
import java.util.List;

public interface OcrEngine {

    List<String> ocr(String filename) throws IOException;
}
