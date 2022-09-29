package ai.obvs.model.MasterData;

import javax.persistence.*;

@Entity
@Table(name = "Master_Constitution", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "code"
        })
})
public class Constitution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

