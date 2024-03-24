package com.techbirdssolutions.springpos.service;

import com.techbirdssolutions.springpos.config.DefaultDataLoad;
import com.techbirdssolutions.springpos.config.JwtService;
import com.techbirdssolutions.springpos.constant.CustomMataDataConstant;
import com.techbirdssolutions.springpos.constant.UserConstant;
import com.techbirdssolutions.springpos.entity.User;
import com.techbirdssolutions.springpos.exception.InvalidTokenException;
import com.techbirdssolutions.springpos.exception.LicenseExpiredException;
import com.techbirdssolutions.springpos.exception.UserDisabledException;
import com.techbirdssolutions.springpos.model.response.JwtResponseModel;
import com.techbirdssolutions.springpos.repository.MetaSettingsRepository;
import com.techbirdssolutions.springpos.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    private boolean isExpired() {
        try {
            LocalDateTime expDate = metaSettingsRepository.findByName(CustomMataDataConstant.META_EXP_DATE_KEY).getDate();
            return LocalDateTime.now().isAfter(expDate);
        } catch (Exception e) {
            log.error("Error while checking system expiry: {}", ExceptionUtils.getStackTrace(e));
        }
        return true;
    }

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
        return new JwtResponseModel(accessToken, refreshToken);
    }

    public void logout(String token) {
        User user = userRepository.findByRefreshToken(token);
        user.setRefreshToken(null);
        userRepository.save(user);
        log.info("User logged out successfully: {}",user.getEmail());
    }

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
}
