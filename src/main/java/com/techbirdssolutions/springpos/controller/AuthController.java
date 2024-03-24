package com.techbirdssolutions.springpos.controller;


import com.techbirdssolutions.springpos.config.JwtService;
import com.techbirdssolutions.springpos.exception.InvalidTokenException;
import com.techbirdssolutions.springpos.exception.LicenseExpiredException;
import com.techbirdssolutions.springpos.exception.UserDisabledException;
import com.techbirdssolutions.springpos.model.ResponseModel;
import com.techbirdssolutions.springpos.model.request.AuthRequestModel;
import com.techbirdssolutions.springpos.model.response.JwtResponseModel;
import com.techbirdssolutions.springpos.model.request.RefreshTokenRequestModel;
import com.techbirdssolutions.springpos.repository.UserRepository;
import com.techbirdssolutions.springpos.service.AuthenticationService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;
    @PostMapping("/login")
    public ResponseEntity<ResponseModel> login(@RequestBody AuthRequestModel authRequestModel) {
        try{
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.OK.value())
                    .success(true)
                    .message("Login successful")
                    .data(authenticationService.authenticateAndGetToken(authRequestModel.getUsername()))
                    .build(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .success(false)
                    .message(e.getMessage())
                    .data(ExceptionUtils.getStackTrace(e))
                    .build(), HttpStatus.UNAUTHORIZED);
        }

    }

    @PostMapping("/refresh")
    public ResponseEntity<ResponseModel> refreshToken(@RequestBody RefreshTokenRequestModel refreshTokenRequestModel){
        try{
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.OK.value())
                    .success(true)
                    .message("Token refreshed successfully")
                    .data(authenticationService.refreshToken(refreshTokenRequestModel.getRefreshToken()))
                    .build(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .success(false)
                    .message(e.getMessage())
                    .data(ExceptionUtils.getStackTrace(e))
                    .build(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/password/reset")
    public ResponseEntity<ResponseModel> sendPasswordReset(@RequestParam String email){
        try{
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.OK.value())
                    .success(true)
                    .message("Token refreshed successfully")
                    .build(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .success(false)
                    .message(e.getMessage())
                    .data(ExceptionUtils.getStackTrace(e))
                    .build(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/password/reset")
    public ResponseEntity<ResponseModel> sendPasswordReset(@RequestBody AuthRequestModel authRequestModel){
        try{
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.OK.value())
                    .success(true)
                    .message("Token refreshed successfully")
                    .build(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .success(false)
                    .message(e.getMessage())
                    .data(ExceptionUtils.getStackTrace(e))
                    .build(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<ResponseModel> logout(@RequestBody RefreshTokenRequestModel refreshTokenRequestModel){
        try{
            authenticationService.logout(refreshTokenRequestModel.getRefreshToken());
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.OK.value())
                    .success(true)
                    .message("Logout successful")
                    .build(), HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .success(false)
                    .message(e.getMessage())
                    .data(ExceptionUtils.getStackTrace(e))
                    .build(), HttpStatus.UNAUTHORIZED);
        }

    }
}
