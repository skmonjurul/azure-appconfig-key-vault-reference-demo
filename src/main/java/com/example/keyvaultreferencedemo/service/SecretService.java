package com.example.keyvaultreferencedemo.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SecretService {
    
    private final String secret;
    private final String appConfig;
    
    private static final String SEPARATOR = " : ";
    
    public SecretService(@Value("${app.secret}") String secret, @Value("${app.config}") String appConfig) {
        this.secret = secret;
        this.appConfig = appConfig;
    }
    public String getSecret() {
        return secret;
    }
    
    public String getAppConfig() {
        return appConfig;
    }
    
    public String getSecretAndAppConfig() {
        return appConfig + SEPARATOR + secret;
    }
}
