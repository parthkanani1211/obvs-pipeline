package ai.obvs.dto;

import ai.obvs.validation.MobileNumber;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

public class UserDto extends GenericEntityDto {

    @NotNull(message = "Please provide mobileNumber")
    @MobileNumber(value = 10)
    private Long mobileNumber;
    
//	@NotEmpty(message = "Please provide Email Address")
    private String emailAddress;
    
//    @NotEmpty(message = "Please provide a First Name")
    private String firstName;
    
    private String lastName;
    
//    @NotEmpty(message = "Please provide roles")
    private List<RoleBaseDto> roles;

    private Long employeeId;

    private String employerName;

    private String address;

    public Long getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(Long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<RoleBaseDto> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleBaseDto> roles) {
        this.roles = roles;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }
}
