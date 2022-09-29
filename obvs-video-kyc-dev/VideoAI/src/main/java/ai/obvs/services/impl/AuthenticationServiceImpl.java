package ai.obvs.services.impl;

import ai.obvs.Enums.Roles;
import ai.obvs.dto.*;
import ai.obvs.exceptions.OTPVerificationFailedException;
import ai.obvs.mapper.UserMapper;
import ai.obvs.model.CBRequest;
import ai.obvs.model.Organization;
import ai.obvs.model.Role;
import ai.obvs.model.User;
import ai.obvs.security.JwtTokenProvider;
import ai.obvs.security.SmsAuthenticationToken;
import ai.obvs.services.AuthenticationService;
import ai.obvs.services.CBRequestsService;
import ai.obvs.services.CustomerService;
import ai.obvs.services.notification.NotificationService;
import ai.obvs.services.notification.PropertiesData;
import ai.obvs.services.notification.SMSService;
import ai.obvs.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {
    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JwtTokenProvider tokenProvider;
    private OTPService otpService;

    private CustomerService customerService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CBRequestsService cbRequestsService;

    @Autowired
    private PropertiesData propertiesData;

    public AuthenticationServiceImpl(UserService userService, AuthenticationManager authenticationManager,
                                     JwtTokenProvider tokenProvider, OTPService otpService, CustomerService customerService) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.otpService = otpService;
        this.customerService = customerService;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Long mobileNumber = loginRequest.getMobileNumber();

        boolean userRegistered = userService.isUserRegistered(mobileNumber);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserExists(userRegistered);
        if (propertiesData.getCkycEnabled() && loginRequest.getRefId()!= null && !loginRequest.getRefId().isBlank()) {
            if (!userRegistered) {
                validateCBRequestExists(mobileNumber, loginRequest.getRefId());
            } else {
                Optional<User> userByMobileNumber = userService.getUserByMobileNumber(mobileNumber);
                User user = userByMobileNumber.get();
                boolean isCustomer = user.getRoles().stream().anyMatch(r -> r.getName().equals(Roles.CUSTOMER.getValue()));
                if (isCustomer)
                    validateCBRequestExists(mobileNumber, loginRequest.getRefId());
            }
        }
        int otp = otpService.generateOTP(mobileNumber);
        OTPCode otpCode = new OTPCode(String.valueOf(otp), 60);

        if (propertiesData.getSmsEnabled())
            notificationService.sendOTP(mobileNumber, otp);
        else
            loginResponse.setOtpCode(otpCode);

//        if(userRegistered){
//            Optional<User> userByMobileNumber = userService.getUserByMobileNumber(mobileNumber);
//            User user = userByMobileNumber.get();
//            user.setActive(true);
//            userService.
//
//        }
        return loginResponse;
    }

    private void validateCBRequestExists(Long mobileNumber, String refId) {
        Optional<CBRequest> cbRequestByMobileNumber = cbRequestsService.getCBRequestByMobileNumber(mobileNumber);
        if (!cbRequestByMobileNumber.isPresent()) {
            throw new IllegalArgumentException("Oops, something went wrong!. Please try again.");
        } else {
            CBRequest cbRequest = cbRequestByMobileNumber.get();
            if (!cbRequest.getRefId().equals(refId)) {
                throw new IllegalArgumentException("Oops, something went wrong!. Please try again.");
            }
        }
    }

//    @Override
//    public void logout(LogoutRequest logoutRequest) {
//        userService.updateUserLoginStatus(logoutRequest.getUserId(),LoginStatus.OFFLINE);
//    }

    @Override
    public AuthenticationResponse validateOtp(Long orgId, LoginRequest loginRequest) {
        Long mobileNumber = loginRequest.getMobileNumber();
        Long OtpNumber = loginRequest.getOtpCode();

        if (OtpNumber >= 0) {
            int serverOtp = otpService.getOtp(mobileNumber);
            if (serverOtp > 0) {
                if (OtpNumber == serverOtp) {
                    registerCustomerIfNotExists(orgId, mobileNumber);
                    if (propertiesData.getCkycEnabled() && loginRequest.getRefId() != null && !loginRequest.getRefId().isBlank()) {
                        Optional<User> userByMobileNumber = userService.getUserByMobileNumber(mobileNumber);
                        User user = userByMobileNumber.get();
                        boolean isCustomer = user.getRoles().stream().anyMatch(r -> r.getName().equals(Roles.CUSTOMER.getValue()));
                        if (isCustomer)
                            validateCBRequestExists(mobileNumber, loginRequest.getRefId());

                    }
                    return authenticate(mobileNumber);
                } else {
                    throw new OTPVerificationFailedException("Invalid OTP.");
                }
            } else {
                throw new OTPVerificationFailedException("OTP expired.");
            }
        } else {
            throw new OTPVerificationFailedException("Invalid OTP.");
        }
    }

    @Override
    public AuthenticationResponse authenticate(Long orgId, LoginRequest loginRequest) {
        Long mobileNumber = loginRequest.getMobileNumber();
        Long OtpNumber = loginRequest.getOtpCode();

        Optional<User> userByMobileNumber = userService.getUserByMobileNumber(mobileNumber);
        if (userByMobileNumber.isPresent()) {
            User user = userByMobileNumber.get();
            List<Role> roleList = user.getRoles().stream().filter(x -> x.getName().equals(Roles.APIUSER.getValue())).collect(Collectors.toList());
            if (roleList.size() > 0) {
                if (Long.valueOf(user.getCode()).equals(OtpNumber))
                    return authenticate(mobileNumber);
                else {
                    throw new OTPVerificationFailedException("Authentication failed.");
                }
            }
        }
        throw new IllegalArgumentException("User is not registered as API user.");
    }

    private void registerCustomerIfNotExists(Long orgId, Long mobileNumber) {
        Optional<User> userByMobileNumber = userService.getUserByMobileNumber(mobileNumber);
        if (!userByMobileNumber.isPresent()) {
            CustomerDto customerDto = new CustomerDto();
            customerDto.setMobileNumber(mobileNumber);
            customerService.create(orgId, customerDto);
        } else {
            Optional<Organization> optionalOrganization = userByMobileNumber.get().getOrganizations().stream()
                    .filter(organization -> organization.getId().equals(orgId))
                    .findFirst();
            if (!optionalOrganization.isPresent()) {
                throw new OTPVerificationFailedException("User already registered with other organization");
            }
        }
    }

    private AuthenticationResponse authenticate(Long mobileNumber) {
        Authentication authentication = authenticationManager.authenticate(new SmsAuthenticationToken(String.valueOf(mobileNumber)));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        Optional<User> userByMobileNumber = userService.getUserByMobileNumber(mobileNumber);
        UserDto userDto = UserMapper.MAPPER.ToUserDto(userByMobileNumber.get());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(userDto, jwt);
        otpService.clearOTP(mobileNumber);
        return authenticationResponse;
    }

}