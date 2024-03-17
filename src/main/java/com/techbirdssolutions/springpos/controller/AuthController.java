package com.techbirdssolutions.springpos.controller;


import com.techbirdssolutions.springpos.config.JwtService;
import com.techbirdssolutions.springpos.entity.User;
import com.techbirdssolutions.springpos.exception.InvalidTokenException;
import com.techbirdssolutions.springpos.exception.LicenseExpiredException;
import com.techbirdssolutions.springpos.exception.UserDisabledException;
import com.techbirdssolutions.springpos.model.AuthRequestModel;
import com.techbirdssolutions.springpos.model.JwtResponseModel;
import com.techbirdssolutions.springpos.model.RefreshTokenRequestModel;
import com.techbirdssolutions.springpos.repository.UserRepository;
import com.techbirdssolutions.springpos.service.AuthenticationService;
import jakarta.transaction.Transactional;
import org.apache.catalina.Authenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationService authenticationService;
    @PostMapping("/login")
    public JwtResponseModel login(@RequestBody AuthRequestModel authRequestModel) throws UserDisabledException, LicenseExpiredException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestModel.getUsername(), authRequestModel.getPassword()));
        if(authentication.isAuthenticated()){
            return authenticationService.authenticateAndGetToken(authRequestModel.getUsername());
        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }
    }

    @PostMapping("/refresh")
    public JwtResponseModel refreshToken(@RequestBody RefreshTokenRequestModel refreshTokenRequestModel) throws UserDisabledException, InvalidTokenException, LicenseExpiredException {
        return authenticationService.refreshToken(refreshTokenRequestModel.getRefreshToken());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody RefreshTokenRequestModel refreshTokenRequestModel){
        authenticationService.logout(refreshTokenRequestModel.getRefreshToken());
        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
    }
}
