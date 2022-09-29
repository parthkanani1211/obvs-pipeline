package ai.obvs.controllers;

import ai.obvs.dto.AuthenticationResponse;
import ai.obvs.dto.LoginRequest;
import ai.obvs.dto.LoginResponse;
import ai.obvs.dto.LogoutRequest;
import ai.obvs.services.AuthenticationService;
import ai.obvs.services.impl.OTPCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/v1/authentication")
public class AuthenticationController {
    private AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authenticationService.login(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/validateOtp")
    public ResponseEntity<?> validateOtp(@RequestHeader("x-obvs-org") Long orgId, @RequestBody LoginRequest loginRequest) {
        AuthenticationResponse authenticationResponse = authenticationService.validateOtp(orgId, loginRequest);
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestHeader("x-obvs-org") Long orgId, @RequestBody LoginRequest loginRequest) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(orgId, loginRequest);
        return ResponseEntity.ok(authenticationResponse);
    }
}
