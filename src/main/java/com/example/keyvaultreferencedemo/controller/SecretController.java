package com.example.keyvaultreferencedemo.controller;

import com.example.keyvaultreferencedemo.service.SecretService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecretController {
    
    private final SecretService secretService;
    
    public SecretController(SecretService secretService) {
        this.secretService = secretService;
    }
    
    @GetMapping("/secrets")
    public String getSecret() {
        return secretService.getSecret();
    }
    
    @GetMapping("/appConfigs")
    public String getAppConfig() {
        return secretService.getAppConfig();
    }
    
    @GetMapping("/secretsAndAppConfigs")
    public String getSecretAndAppConfig() {
        return secretService.getSecretAndAppConfig();
    }
}
