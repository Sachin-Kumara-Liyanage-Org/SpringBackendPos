package com.techbirdssolutions.springpos.service;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
/**
 * This service class is responsible for encryption and decryption of settings.
 * It uses Jasypt library for encryption and decryption.
 * It is annotated with @Service to indicate that it's a Spring Service.
 */
@Service
public class SettingEncryptorService {
    /**
     * The password for the encryption and decryption.
     * It is injected from the application properties file using @Value annotation.
     */
    @Value("${jasypt.encryptor.password}")
    private String password;
    /**
     * This method creates and configures a StringEncryptor bean.
     * The StringEncryptor is configured with the password and other properties.
     * @return The configured StringEncryptor bean.
     */
    @Bean(name = "encryptorBean")
    public StringEncryptor stringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(password);
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName("SunJCE");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }
    /**
     * This method encrypts a given value using the StringEncryptor.
     * @param value The value to encrypt.
     * @return The encrypted value.
     */
    public String encrypt(String value) {
        return stringEncryptor().encrypt(value);
    }
    /**
     * This method decrypts a given value using the StringEncryptor.
     * @param value The value to decrypt.
     * @return The decrypted value.
     */
    public String decrypt(String value) {
        return stringEncryptor().decrypt(value);
    }


}
