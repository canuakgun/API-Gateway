package com.gateway.auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import com.gateway.model.LoginDTO;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;
    private JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO user) {
        if (authService.authenticate(user.getUsername(), user.getPassword())) {
            return ResponseEntity.ok(jwtUtil.generateToken(user.getUsername()));
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}