package com.techbirdssolutions.springpos.controller;

import com.techbirdssolutions.springpos.constant.CommonConstant;
import com.techbirdssolutions.springpos.model.ResponseModel;
import com.techbirdssolutions.springpos.service.SettingEncryptorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * This is a Rest Controller class for handling encryption and decryption operations.
 * It exposes two endpoints: one for encryption and one for decryption.
 * The class uses the SettingEncryptorService for performing the actual encryption and decryption.
 */
@RestController
@RequestMapping("/crypt")
public class EncryptorController {

    // Autowired SettingEncryptorService for performing encryption and decryption
    @Autowired
    SettingEncryptorService settingEncryptorService;

    /**
     * This method is used to encrypt data.
     * The data to be encrypted must enter with key data like {"data":"data to be encrypted"}
     * The method returns a ResponseModel with the encrypted data.
     *
     * @param body the data to be encrypted
     * @return a ResponseEntity with a ResponseModel containing the encrypted data
     */
    @PreAuthorize("hasAuthority('DO_Encrypt')")
    @SecurityRequirement(name = "Authorization")
    @PostMapping("/encrypt")
    @Operation(summary = "Encrypt data",
            description = "This method is used to encrypt data.\nThe data to be encrypted must enter with key data like {\"data\":\"data to be encrypted\"}")
    public ResponseEntity<ResponseModel> encrypt(@RequestBody
                              @Parameter(description = "the data to be encrypted must enter with key data like {\"data\":\"data to be encrypted\"}")
                              Map<String, String> body) {

        try{
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.OK.value())
                    .success(true)
                    .message("encryption successful")
                    .data(settingEncryptorService.encrypt(body.get("data")))
                    .requestId(MDC.get(CommonConstant.UNIQUE_ID_MDC_KEY))
                    .build(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .success(false)
                    .message(e.getMessage())
                    .data(ExceptionUtils.getStackTrace(e))
                    .requestId(MDC.get(CommonConstant.UNIQUE_ID_MDC_KEY))
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This method is used to decrypt data.
     * The data to be decrypted must enter with key data like {"data":"data to be decrypted"}
     * The method returns a ResponseModel with the decrypted data.
     *
     * @param body the data to be decrypted
     * @return a ResponseEntity with a ResponseModel containing the decrypted data
     */
    @PreAuthorize("hasAuthority('DO_Decrypt')")
    @SecurityRequirement(name = "Authorization")
    @PostMapping("/decrypt")
    @Operation(summary = "Decrypt data",
            description = "This method is used to decrypt data.\nthe data to be decrypt must enter with key data like {\"data\":\"data to be decrypt\"}")
    public ResponseEntity<ResponseModel>  decrypt(@RequestBody
                              @Parameter(description = "the data to be decrypt must enter with key data like {\"data\":\"data to be decrypt\"}")
                              Map<String, String> body) {
        try{
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.OK.value())
                    .success(true)
                    .message("decrypted successfully")
                    .data(settingEncryptorService.decrypt(body.get("data")))
                    .requestId(MDC.get(CommonConstant.UNIQUE_ID_MDC_KEY))
                    .build(), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(ResponseModel.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .success(false)
                    .message(e.getMessage())
                    .data(ExceptionUtils.getStackTrace(e))
                    .requestId(MDC.get(CommonConstant.UNIQUE_ID_MDC_KEY))
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
