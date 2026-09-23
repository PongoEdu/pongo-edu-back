package com.pongo.pongoedu.web.controller;

import com.pongo.pongoedu.application.dto.request.LoginRequest;
import com.pongo.pongoedu.application.dto.response.TokenResponse;
import com.pongo.pongoedu.application.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.autenticar(request));
    }
}
