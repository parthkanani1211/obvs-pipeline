package ai.obvs.security;

import ai.obvs.security.SmsAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class SmsAuthenticationProvider implements AuthenticationProvider {

    private UserDetailService userDetailService;

    public SmsAuthenticationProvider(UserDetailService userDetailService){
        this.userDetailService = userDetailService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsAuthenticationToken authenticationToken = (SmsAuthenticationToken) authentication;
        //Call custom userDetailsService authentication
        UserDetails userDetails = userDetailService.loadUserByMobileNumber(Long.valueOf(((String) authenticationToken.getPrincipal())));

        if (userDetails == null) {
            throw new InternalAuthenticationServiceException("The user corresponding to the mobile number was not found");
        }
        //If user is not empty, rebuild SmsCodeAuthenticationToken (authenticated)
        SmsAuthenticationToken authenticationResult = new SmsAuthenticationToken(userDetails, userDetails.getAuthorities());

        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }

    /**
     * Only Authentication for SmsCodeAuthenticationToken uses this Provider Authentication
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public UserDetailService getUserDetailService() {
        return userDetailService;
    }

    public void setUserDetailService(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }
}