package com.techbirdssolutions.springpos.controller;


import com.techbirdssolutions.springpos.config.JwtService;
import com.techbirdssolutions.springpos.constant.CommonConstant;
import com.techbirdssolutions.springpos.exception.InvalidTokenException;
import com.techbirdssolutions.springpos.exception.LicenseExpiredException;
import com.techbirdssolutions.springpos.exception.UserDisabledException;
import com.techbirdssolutions.springpos.model.ResponseModel;
import com.techbirdssolutions.springpos.model.request.AuthRequestModel;
import com.techbirdssolutions.springpos.model.request.PasswordChangeRequest;
import com.techbirdssolutions.springpos.model.request.PasswordResetRequest;
import com.techbirdssolutions.springpos.model.response.JwtResponseModel;
import com.techbirdssolutions.springpos.model.request.RefreshTokenRequestModel;
import com.techbirdssolutions.springpos.repository.UserRepository;
import com.techbirdssolutions.springpos.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.MDC;
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
@Slf4j
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
                    .requestId(MDC.get(CommonConstant.UNIQUE_ID_MDC_KEY))
                    .build(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .success(false)
                    .message(e.getMessage())
                    .data(ExceptionUtils.getStackTrace(e))
                    .requestId(MDC.get(CommonConstant.UNIQUE_ID_MDC_KEY))
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
                    .requestId(MDC.get(CommonConstant.UNIQUE_ID_MDC_KEY))
                    .build(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .success(false)
                    .message(e.getMessage())
                    .data(ExceptionUtils.getStackTrace(e))
                    .requestId(MDC.get(CommonConstant.UNIQUE_ID_MDC_KEY))
                    .build(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/password/reset/request")
    public ResponseEntity<ResponseModel> sendPasswordReset(@RequestBody PasswordResetRequest passwordResetRequest){
        try{
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.OK.value())
                    .success(true)
                    .message("Password reset token sent successfully")
                    .data(authenticationService.sendPasswordResetToken(passwordResetRequest))
                    .requestId(MDC.get(CommonConstant.UNIQUE_ID_MDC_KEY))
                    .build(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .success(false)
                    .message(e.getMessage())
                    .data(ExceptionUtils.getStackTrace(e))
                    .requestId(MDC.get(CommonConstant.UNIQUE_ID_MDC_KEY))
                    .build(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/password/reset/confirm")
    public ResponseEntity<ResponseModel> sendPasswordReset(@RequestBody PasswordChangeRequest passwordChangeRequest){
        try{
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.OK.value())
                    .success(true)
                    .message("Password reset successful")
                    .requestId(MDC.get(CommonConstant.UNIQUE_ID_MDC_KEY))
                    .data(authenticationService.resetPassword(passwordChangeRequest))
                    .build(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .success(false)
                    .message(e.getMessage())
                    .data(ExceptionUtils.getStackTrace(e))
                    .requestId(MDC.get(CommonConstant.UNIQUE_ID_MDC_KEY))
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
                    .requestId(MDC.get(CommonConstant.UNIQUE_ID_MDC_KEY))
                    .build(), HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .success(false)
                    .message(e.getMessage())
                    .data(ExceptionUtils.getStackTrace(e))
                    .requestId(MDC.get(CommonConstant.UNIQUE_ID_MDC_KEY))
                    .build(), HttpStatus.UNAUTHORIZED);
        }

    }
}
