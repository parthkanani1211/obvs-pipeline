package ai.obvs.dto;

public class AuthenticationResponse {
    private UserDto userDto;
    private String token;

    public AuthenticationResponse(UserDto userDto, String token) {
        this.userDto = userDto;
        this.token = token;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public String getToken() {
        return token;
    }
}
