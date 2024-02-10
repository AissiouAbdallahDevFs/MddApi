package com.openclassrooms.mddapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Autowired
    SecurityConfig securityConfig;

    @Value("${jwt}")
    private String jwtSecret;

    @Value("${jwtExpirationMs}")
    private int jwtExpirationMs;

  
    public String getJwtSecret() {

        return jwtSecret;
    }
  public int getJwtExpirationMs() {
        return jwtExpirationMs;
    }
    
}
