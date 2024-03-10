package com.techbirdssolutions.springpos.service;

import com.techbirdssolutions.springpos.config.JwtService;
import com.techbirdssolutions.springpos.entity.User;
import com.techbirdssolutions.springpos.exception.InvalidTokenException;
import com.techbirdssolutions.springpos.exception.UserDisabledException;
import com.techbirdssolutions.springpos.model.JwtResponseModel;
import com.techbirdssolutions.springpos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
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
            throw new UserDisabledException("User is disabled");
        }

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
    }

    public JwtResponseModel refreshToken(String refreshToken) throws InvalidTokenException, UserDisabledException {
        // Extract username from the refresh token
        String username = jwtService.getUsernameFromToken(refreshToken);
        // Load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // 1. Validate the refresh token
        if (!jwtService.validateToken(refreshToken,userDetails)) {
            throw new InvalidTokenException("Invalid refresh token");
        }
        User user = userRepository.findByEmail(username);
        if(user.getRefreshToken().equals(refreshToken)){
            throw new InvalidTokenException("Invalid refresh token");
        }


        return this.authenticateAndGetToken(username);
    }
}
