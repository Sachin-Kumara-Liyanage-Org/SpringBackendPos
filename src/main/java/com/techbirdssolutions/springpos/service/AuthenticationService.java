package com.techbirdssolutions.springpos.service;

import com.techbirdssolutions.springpos.config.JwtService;
import com.techbirdssolutions.springpos.entity.User;
import com.techbirdssolutions.springpos.exception.InvalidTokenException;
import com.techbirdssolutions.springpos.exception.UserDisabledException;
import com.techbirdssolutions.springpos.model.JwtResponseModel;
import com.techbirdssolutions.springpos.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

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

    public JwtResponseModel authenticateAndGetToken(String username) throws UserDisabledException {
        // 1. Authenticate the user
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!userDetails.isEnabled()) {
            log.warn("User is disabled: {}",username);
            throw new UserDisabledException("User is disabled");
        }
        log.info("User authenticated successfully: {}",username);
        return this.createToken(username);
    }

    private JwtResponseModel createToken(String username) {
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

    public JwtResponseModel refreshToken(String refreshToken) throws InvalidTokenException, UserDisabledException {
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
        if(user.getRefreshToken().equals(refreshToken)){
            log.warn("Mismatch in refresh token for user: {}",username);
            throw new InvalidTokenException("Invalid refresh token");
        }
        log.info("Refresh token validated successfully for user: {}",username);
        return this.authenticateAndGetToken(username);
    }
}
