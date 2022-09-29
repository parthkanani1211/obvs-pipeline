package ai.obvs.dto.workflow;

import ai.obvs.Enums.DocumentName;
import ai.obvs.dto.GenericEntityDto;

public class DocumentDto {
    private Long id;
    private DocumentName name;
    private int inputImages;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentName getName() {
        return name;
    }

    public void setName(DocumentName name) {
        this.name = name;
    }

    public int getInputImages() {
        return inputImages;
    }

    public void setInputImages(int inputImages) {
        this.inputImages = inputImages;
    }
}
