package ai.obvs.model;

import javax.persistence.Entity;
import java.util.Objects;

@Entity
public class NameFields extends GenericEntity{
    private String prefix;
    private String firstName;
    private String MiddleName;
    private String lastName;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return MiddleName;
    }

    public void setMiddleName(String middleName) {
        MiddleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NameFields that = (NameFields) o;
        return Objects.equals(firstName, that.firstName) &&
                Objects.equals(MiddleName, that.MiddleName) &&
                Objects.equals(lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, MiddleName, lastName);
    }
}
