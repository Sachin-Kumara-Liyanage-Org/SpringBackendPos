package com.techbirdssolutions.springpos.controller;


import com.techbirdssolutions.springpos.config.JwtService;
import com.techbirdssolutions.springpos.entity.User;
import com.techbirdssolutions.springpos.model.AuthRequestModel;
import com.techbirdssolutions.springpos.model.JwtResponseModel;
import com.techbirdssolutions.springpos.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.catalina.Authenticator;
import org.springframework.beans.factory.annotation.Autowired;
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
    @PostMapping("/login")
    public JwtResponseModel AuthenticateAndGetToken(@RequestBody AuthRequestModel authRequestModel){

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestModel.getUsername(), authRequestModel.getPassword()));
        System.out.println("sachin");
        if(authentication.isAuthenticated()){
            return JwtResponseModel.builder()
                    .accessToken(jwtService.GenerateToken(authRequestModel.getUsername())).build();
        } else {
            throw new UsernameNotFoundException("invalid user request..!!");
        }
    }

    @PostMapping("/data")
    public String data(@RequestBody AuthRequestModel authRequestModel){

        return userRepository.findByEmail(authRequestModel.getUsername()).getEmail();

    }
}
