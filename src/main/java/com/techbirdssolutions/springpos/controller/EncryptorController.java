package com.techbirdssolutions.springpos.controller;

import com.techbirdssolutions.springpos.service.SettingEncryptorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
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
     * It is a POST endpoint which takes a JSON body with a "data" field.
     * The method uses the SettingEncryptorService to encrypt the data.
     * The encrypted data is returned as a response.
     * The endpoint can only be accessed by users with the 'DO_Encrypt' authority.
     *
     * @param body a map containing the data to be encrypted
     * @return the encrypted data
     */
    @PreAuthorize("hasAuthority('DO_Encrypt')")
    @SecurityRequirement(name = "Authorization")
    @PostMapping("/encrypt")
    @Operation(summary = "Encrypt data",
            description = "This method is used to encrypt data.\nThe data to be encrypted must enter with key data like {\"data\":\"data to be encrypted\"}")
    public String encrypt(@RequestBody
                              @Parameter(description = "the data to be encrypted must enter with key data like {\"data\":\"data to be encrypted\"}")
                              Map<String, String> body) {
        return settingEncryptorService.encrypt(body.get("data"));
    }

    /**
     * This method is used to decrypt data.
     * It is a POST endpoint which takes a JSON body with a "data" field.
     * The method uses the SettingEncryptorService to decrypt the data.
     * The decrypted data is returned as a response.
     * The endpoint can only be accessed by users with the 'DO_Decrypt' authority.
     *
     * @param body a map containing the data to be decrypted
     * @return the decrypted data
     */
    @PreAuthorize("hasAuthority('DO_Decrypt')")
    @SecurityRequirement(name = "Authorization")
    @PostMapping("/decrypt")
    @Operation(summary = "Decrypt data",
            description = "This method is used to decrypt data.\nthe data to be decrypt must enter with key data like {\"data\":\"data to be decrypt\"}")
    public String decrypt(@RequestBody
                              @Parameter(description = "the data to be decrypt must enter with key data like {\"data\":\"data to be decrypt\"}")
                              Map<String, String> body) {
        return settingEncryptorService.decrypt(body.get("data"));
    }
}
