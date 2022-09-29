package ai.obvs.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Agent")
public class Agent extends User {
    private Long employeeId;
    private String employeeName;

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}
