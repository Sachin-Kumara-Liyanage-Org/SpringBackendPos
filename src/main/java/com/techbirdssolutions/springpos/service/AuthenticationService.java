package com.techbirdssolutions.springpos.service;

import com.techbirdssolutions.springpos.config.DefaultDataLoad;
import com.techbirdssolutions.springpos.config.JwtService;
import com.techbirdssolutions.springpos.constant.CustomMataDataConstant;
import com.techbirdssolutions.springpos.constant.UserConstant;
import com.techbirdssolutions.springpos.entity.User;
import com.techbirdssolutions.springpos.exception.InvalidTokenException;
import com.techbirdssolutions.springpos.exception.LicenseExpiredException;
import com.techbirdssolutions.springpos.exception.UserDisabledException;
import com.techbirdssolutions.springpos.model.custom.EmailData;
import com.techbirdssolutions.springpos.model.request.PasswordChangeRequest;
import com.techbirdssolutions.springpos.model.request.PasswordResetRequest;
import com.techbirdssolutions.springpos.model.response.JwtResponseModel;
import com.techbirdssolutions.springpos.repository.MetaSettingsRepository;
import com.techbirdssolutions.springpos.repository.UserRepository;
import com.techbirdssolutions.springpos.util.EmailUtil;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * This service class is responsible for handling authentication related operations.
 * It uses JwtService for token generation and validation.
 * It is annotated with @Service to indicate that it's a Spring Service.
 */
@Service
@Slf4j
public class AuthenticationService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService userDetailsServiceImpl;

    @Autowired
    private MetaSettingsRepository metaSettingsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailUtil emailUtil;
    /**
     * This method authenticates a user and returns a JWT token.
     * @param username The username of the user to authenticate.
     * @return A JwtResponseModel containing the access and refresh tokens.
     * @throws UserDisabledException If the user is disabled.
     * @throws LicenseExpiredException If the system license is expired.
     */
    public JwtResponseModel authenticateAndGetToken(String username) throws UserDisabledException, LicenseExpiredException {
        // 1. Authenticate the user
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!userDetails.isEnabled()) {
            log.warn("User is disabled: {}",username);
            throw new UserDisabledException("User is disabled");
        }
        log.info("User authenticated successfully: {}",username);
        return this.createToken(username);
    }
    /**
     * This method checks if the system license is expired.
     * @return true if the system license is expired, false otherwise.
     */
    private boolean isExpired() {
        try {
            LocalDateTime expDate = metaSettingsRepository.findByName(CustomMataDataConstant.META_EXP_DATE_KEY).getDate();
            return LocalDateTime.now().isAfter(expDate);
        } catch (Exception e) {
            log.error("Error while checking system expiry: {}", ExceptionUtils.getStackTrace(e));
        }
        return true;
    }
    /**
     * This method creates a JWT token for a given username.
     * @param username The username for which to create the token.
     * @return A JwtResponseModel containing the access and refresh tokens.
     * @throws LicenseExpiredException If the system license is expired.
     */
    private JwtResponseModel createToken(String username) throws LicenseExpiredException {
        if(isExpired()){
            log.warn("System is expired");
            boolean isSuperAdminUser = userRepository.existsByEmailAndAdminRole(username, UserConstant.SUPER_ADMIN);
            if(Boolean.FALSE.equals(isSuperAdminUser)){
                log.warn("User is not super admin: {}",username);
                throw new LicenseExpiredException("System is expired");
            }
        }
        // 1. Generate an access token and refresh token
        String accessToken = jwtService.GenerateToken(username);
        String refreshToken = jwtService.generateRefreshToken(username);
        User user = userRepository.findByEmail(username);
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
        return new JwtResponseModel(accessToken, refreshToken,username);
    }
    /**
     * This method logs out a user by invalidating their refresh token.
     * @param token The refresh token of the user to log out.
     */
    public void logout(String token) {
        User user = userRepository.findByRefreshToken(token);
        user.setRefreshToken(null);
        userRepository.save(user);
        log.info("User logged out successfully: {}",user.getEmail());
    }
    /**
     * This method refreshes a JWT token.
     * @param refreshToken The refresh token to refresh.
     * @return A JwtResponseModel containing the new access and refresh tokens.
     * @throws InvalidTokenException If the refresh token is invalid.
     * @throws UserDisabledException If the user is disabled.
     * @throws LicenseExpiredException If the system license is expired.
     */
    public JwtResponseModel refreshToken(String refreshToken) throws InvalidTokenException, UserDisabledException, LicenseExpiredException {
        // Extract username from the refresh token
        String username = jwtService.getUsernameFromToken(refreshToken);
        // Load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // 1. Validate the refresh token
        if (!jwtService.validateToken(refreshToken,userDetails)) {
            log.warn("Failed to validate refresh token for user: {}",username);
            throw new InvalidTokenException("Invalid refresh token");
        }
        User user = userRepository.findByEmail(username);
        if(!user.getRefreshToken().equals(refreshToken)){
            log.warn("Mismatch in refresh token for user: {}",username);
            throw new InvalidTokenException("Invalid refresh token");
        }
        log.info("Refresh token validated successfully for user: {}",username);
        return this.authenticateAndGetToken(username);
    }

    public JwtResponseModel sendPasswordResetToken(PasswordResetRequest passwordResetRequest) throws MessagingException, UnsupportedEncodingException {
        User user = userRepository.findByEmail(passwordResetRequest.getEmail());
        if(user==null){
            log.warn("User not found: {}",passwordResetRequest.getEmail());
            throw new UsernameNotFoundException("User not found");
        }
        String refreshToken = jwtService.generateRefreshToken(passwordResetRequest.getEmail());
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
        emailUtil.sendEmail(createPasswordResetEmailData(passwordResetRequest.getEmail(),refreshToken,passwordResetRequest));
        return new JwtResponseModel(null, refreshToken,passwordResetRequest.getEmail());
    }

    private EmailData createPasswordResetEmailData(String email, String token,PasswordResetRequest passwordResetRequest) {
        EmailData emailData = new EmailData();
        emailData.setTo(List.of(email));
        emailData.setSubject("Password Reset Request");
        String link = (""+passwordResetRequest.getCallbackUrl()).replace(passwordResetRequest.getPlaceholder(), token);
        emailData.setBody("Please click the link below to reset your password: <br/>"
                + "<a href=\""+link+"\">Reset Password</a>");
        emailData.setHtml(true);
        return emailData;
    }

    public JwtResponseModel resetPassword(PasswordChangeRequest passwordResetRequest) throws InvalidTokenException, LicenseExpiredException, UserDisabledException {
        String username = jwtService.getUsernameFromToken(passwordResetRequest.getToken());
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!jwtService.validateToken(passwordResetRequest.getToken(),userDetails)) {
            log.warn("Failed to validate token for user: {}",username);
            throw new InvalidTokenException("Invalid Password Change token");
        }
        User user = userRepository.findByEmail(username);
        if(!user.getRefreshToken().equals(passwordResetRequest.getToken())){
            log.warn("Mismatch in Password Change token for user: {}",username);
            throw new InvalidTokenException("Invalid Password Change token");
        }

        user.setPassword(passwordEncoder.encode(passwordResetRequest.getNewPassword()));
        user.setRefreshToken(null);
        userRepository.save(user);
        return this.authenticateAndGetToken(username);
    }
}
