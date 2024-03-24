package com.techbirdssolutions.springpos.config;

import com.techbirdssolutions.springpos.constant.CommonConstant;
import com.techbirdssolutions.springpos.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
/**
 * This class is used to configure the security settings for the application.
 * It enables web security and method security, and configures the authentication and authorization rules.
 * The class uses a JwtAuthFilter for JWT authentication, and a CustomUserDetailsService for user details.
 * The class also provides beans for the PasswordEncoder, AuthenticationProvider, and AuthenticationManager.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    JwtAuthFilter jwtAuthFilter;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CommonConstant commonConstant;
    /**
     * Configures the security filter chain.
     * The method disables CSRF, permits all requests to the login and refresh endpoints, and requires authentication for all other requests.
     * The method also adds the JwtAuthFilter before the UsernamePasswordAuthenticationFilter.
     *
     * @param http the HttpSecurity
     * @return the SecurityFilterChain
     * @throws Exception if an error occurs
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers("/api/auth/login","/api/auth/refresh","/api/auth/password/reset").permitAll())
                .sessionManagement(
                        (sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter,UsernamePasswordAuthenticationFilter.class);

                if(Boolean.TRUE.equals(commonConstant.isLocal())){
                    http.authorizeHttpRequests((authorizeHttpRequests) ->
                            authorizeHttpRequests.requestMatchers("/swagger-ui/**","/v3/api-docs/**").permitAll());
                }
                http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests.anyRequest().authenticated());
                return http.build();

    }
    /**
     * Provides a PasswordEncoder bean.
     * The method returns a BCryptPasswordEncoder.
     *
     * @return the PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /**
     * Provides an AuthenticationProvider bean.
     * The method returns a DaoAuthenticationProvider with the CustomUserDetailsService and the PasswordEncoder.
     *
     * @return the AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;

    }
    /**
     * Provides an AuthenticationManager bean.
     * The method returns the AuthenticationManager from the AuthenticationConfiguration.
     *
     * @param config the AuthenticationConfiguration
     * @return the AuthenticationManager
     * @throws Exception if an error occurs
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
