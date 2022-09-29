package ai.obvs.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "Workflows", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "name", "org_id"
        })
})
public class Workflow extends GenericEntity{

    @NotBlank
    private String name;

    @ManyToOne
    @JoinColumn(name = "org_id", nullable = false)
    private Organization organization;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}

