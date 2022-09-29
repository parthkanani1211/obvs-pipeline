package ai.obvs.dto.Org;

import ai.obvs.Enums.Country;
import ai.obvs.dto.GenericEntityDto;

public class OrgResponseDto extends GenericEntityDto {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
