package com.techbirdssolutions.springpos.controller;

import com.techbirdssolutions.springpos.service.SettingEncryptorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController

@RequestMapping("/crypt")
public class EncryptorController {

    @Autowired
    SettingEncryptorService settingEncryptorService;

    @PreAuthorize("hasAuthority('DO_Encrypt')")
    @SecurityRequirement(name = "Authorization")
    @PostMapping("/encrypt")
    public String encrypt(@RequestBody Map<String, String> body) {
        return settingEncryptorService.encrypt(body.get("data"));
    }

    @PreAuthorize("hasAuthority('DO_Decrypt')")
    @SecurityRequirement(name = "Authorization")
    @PostMapping("/decrypt")
    public String decrypt(@RequestBody Map<String, String> body) {
        return settingEncryptorService.decrypt(body.get("data"));
    }
}
