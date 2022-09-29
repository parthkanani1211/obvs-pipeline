package ai.obvs.dto;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

public class CustomerServiceDto extends CustomerDto{
    @NotEmpty
    private Set<AppDto> apps;

    public CustomerServiceDto(){
    }

    public Set<AppDto> getApps() {
        return apps;
    }

    public void setApps(Set<AppDto> apps) {
        this.apps = apps;
    }
}
