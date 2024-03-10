package com.techbirdssolutions.springpos.controller;

import com.techbirdssolutions.springpos.service.SettingEncryptorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/crypt")
public class EncryptorController {

    @Autowired
    SettingEncryptorService settingEncryptorService;

    @PreAuthorize("hasRole('SUPER_ADMIN') or hasAuthority('DO_Encrypt')")
    @PostMapping("/encrypt")
    public String encrypt(@RequestBody Map<String, String> body) {
        return settingEncryptorService.encrypt(body.get("data"));
    }

    @PreAuthorize("hasRole('SUPER_ADMIN') or hasAuthority('DO_Decrypt')")
    @PostMapping("/decrypt")
    public String decrypt(@RequestBody Map<String, String> body) {
        return settingEncryptorService.decrypt(body.get("data"));
    }
}
