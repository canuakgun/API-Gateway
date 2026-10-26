package com.gateway.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Value("${auth.username}")
    private String authUsername;
    @Value("${auth.password}")
    private String authPassword;

    public boolean authenticate(String username, String password) {
        return username.equals(authUsername) && password.equals(authPassword);
    }
}