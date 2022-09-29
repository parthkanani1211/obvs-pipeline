package ai.obvs.model;

import ai.obvs.Enums.Country;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "organizations", uniqueConstraints =
        {@UniqueConstraint(columnNames = "name", name = "uniqueNameConstraint")}
)
public class Organization extends GenericEntity {
    @NotBlank
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "org_users",
            joinColumns = @JoinColumn(name = "org_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private Country country;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Country getCounty() {
        return country;
    }

    public void setCounty(Country country) {
        this.country = country;
    }
}

