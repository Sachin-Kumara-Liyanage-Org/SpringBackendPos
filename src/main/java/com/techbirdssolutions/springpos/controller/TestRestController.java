package com.techbirdssolutions.springpos.controller;

import com.techbirdssolutions.springpos.service.SettingEncryptorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/crypt")
public class TestRestController {

    @Autowired
    SettingEncryptorService settingEncryptorService;

    @PostMapping("/encrypt")
    public String encrypt(@RequestBody Map<String,String> body) {
        return settingEncryptorService.encrypt(body.get("data"));
    }

    @PostMapping("/decrypt")
    public String decrypt(@RequestBody Map<String,String> body) {
        return settingEncryptorService.decrypt(body.get("data"));
    }
}
