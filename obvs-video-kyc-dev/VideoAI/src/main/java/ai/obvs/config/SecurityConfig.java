package ai.obvs.config;

import ai.obvs.exceptions.UnableToStartVideoSession;
import ai.obvs.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailService userDetailService;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider, userDetailService);
    }

    @Autowired
    private SmsAuthenticationProvider smsAuthenticationProvider;

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        // Custom authentication provider - Order 1
        authenticationManagerBuilder.authenticationProvider(smsAuthenticationProvider);
//        authenticationManagerBuilder.userDetailsService(userDetailService);
//        // Built-in authentication provider - Order 2
//        authenticationManagerBuilder.inMemoryAuthentication()
//                .withUser("admin")
//                .password("{noop}admin@password")
//                //{noop} makes sure that the password encoder doesn't do anything
//                .roles("ADMIN") // Role of the user
//                .and()
//                .withUser("user")
//                .password("{noop}user@password")
//                .credentialsExpired(true)
//                .accountExpired(true)
//                .accountLocked(true)
//                .roles("USER");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/",
                        "/favicon.ico",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js")
                .permitAll()
                .antMatchers("/v1/authentication/**")
                .permitAll()
                .antMatchers("/v1/orgs/**")
                .permitAll()
                .antMatchers("/v1/users/admin")
                .permitAll()
                .antMatchers("/v1/users/create/firstAdmin")
                .permitAll()
                .antMatchers("/v1/aiblock/**")
                .permitAll()
                .antMatchers("/v1/workflow/**")
//                .permitAll()
//                .antMatchers("/v1/videoKYC/requests/**/status")
                .permitAll()
                .anyRequest()
                .authenticated();
//                .antMatchers( "/v1/**").permitAll()
//                .anyRequest().authenticated();
        // Add our custom JWT security filter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
