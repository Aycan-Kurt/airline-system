package com.aycan.airlineapi.controller;

import com.aycan.airlineapi.config.JwtUtil;
import com.aycan.airlineapi.dto.LoginRequest;
import com.aycan.airlineapi.dto.LoginResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @PostMapping("/login")
    public Object login(@RequestBody LoginRequest request) {
        if ("admin".equals(request.getUsername()) && "1234".equals(request.getPassword())) {
            String token = JwtUtil.generateToken(request.getUsername());
            return new LoginResponse(token);
        }

        return "Invalid username or password";
    }
}