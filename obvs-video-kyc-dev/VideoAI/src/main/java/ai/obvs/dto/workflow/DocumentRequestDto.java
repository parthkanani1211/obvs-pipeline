package ai.obvs.dto.workflow;

import ai.obvs.Enums.Country;
import ai.obvs.Enums.DocumentName;
import ai.obvs.dto.GenericEntityDto;

import java.util.List;

public class DocumentRequestDto {
    private Long id;
    private Country country;
    private List<DocumentDto> documents;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<DocumentDto> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentDto> documents) {
        this.documents = documents;
    }
}
