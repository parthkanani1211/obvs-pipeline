package ai.obvs.dto;

public class AgentDto extends UserDto{

    public AgentDto(){

    }
    
    public AgentDto(UserDto userDto){
      super.setFirstName(userDto.getFirstName());
      super.setLastName(userDto.getLastName());
      super.setEmailAddress(userDto.getEmailAddress());
      super.setMobileNumber(userDto.getMobileNumber());
      super.setRoles(userDto.getRoles());
      super.setId(userDto.getId());
      this.employeeId = userDto.getEmployeeId();
      this.employerName = userDto.getEmployerName();
    }

    private Long employeeId;
    private String employerName;

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
