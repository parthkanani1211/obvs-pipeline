package ai.obvs.model;

import ai.obvs.Enums.Country;
import ai.obvs.Enums.DocumentName;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "Documents", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "name", "country"
        })
})
public class Document extends GenericEntity{

    @Enumerated(EnumType.STRING)
    private DocumentName name;

    @Enumerated(EnumType.STRING)
    private ai.obvs.Enums.Country country;

    private int inputImages;

    public DocumentName getName() {
        return name;
    }

    public void setName(DocumentName name) {
        this.name = name;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public int getInputImages() {
        return inputImages;
    }

    public void setInputImages(int inputImages) {
        this.inputImages = inputImages;
    }
}

