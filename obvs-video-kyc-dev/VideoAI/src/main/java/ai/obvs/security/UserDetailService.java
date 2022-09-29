package ai.obvs.security;

import ai.obvs.model.User;
import ai.obvs.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.LogManager;

public class UserDetailService implements org.springframework.security.core.userdetails.UserDetailsService {

    private UserRepository userRepository;

    public UserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserPrincipal loadUserByMobileNumber(Long mobileNumber)
            throws UsernameNotFoundException {
        // Let people login with mobile number
        User user = userRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with mobile number : " + mobileNumber)
                );

        return UserPrincipal.create(user);
    }

    // This method is used by JWTAuthenticationFilter
    @Transactional
    public UserPrincipal loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );

        return UserPrincipal.create(user);
    }

    @Override
    public UserPrincipal loadUserByUsername(String mobileNumber) throws UsernameNotFoundException {
        User user = userRepository.findByMobileNumber(Long.valueOf(mobileNumber))
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with mobile number : " + mobileNumber)
                );

        return UserPrincipal.create(user);
    }
}
