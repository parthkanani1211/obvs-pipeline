package ai.obvs.dto;

public class CustomerDto extends UserDto{

    public CustomerDto(){

    }
    
    public CustomerDto(UserDto userDto){
        super.setFirstName(userDto.getFirstName());
        super.setLastName(userDto.getLastName());
        super.setEmailAddress(userDto.getEmailAddress());
        super.setMobileNumber(userDto.getMobileNumber());
        this.address = userDto.getAddress();
    }

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
