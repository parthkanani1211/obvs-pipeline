package ai.obvs.services;

import ai.obvs.dto.AuthenticationResponse;
import ai.obvs.dto.LoginRequest;
import ai.obvs.dto.LoginResponse;
import ai.obvs.dto.LogoutRequest;
import ai.obvs.services.impl.OTPCode;
import org.springframework.security.core.AuthenticationException;

public interface AuthenticationService {
    LoginResponse login(LoginRequest loginRequest);

//    void logout(LogoutRequest logoutRequest);

    AuthenticationResponse validateOtp(Long orgId, LoginRequest loginRequest);

    AuthenticationResponse authenticate(Long orgId, LoginRequest loginRequest);
}
