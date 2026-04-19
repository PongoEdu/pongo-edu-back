package com.pongo.pongoedu.web.controller;

import com.pongo.pongoedu.application.dto.request.LoginRequest;
import com.pongo.pongoedu.application.dto.request.RegistrarUsuarioRequest;
import com.pongo.pongoedu.application.dto.response.TokenResponse;
import com.pongo.pongoedu.application.usecase.auth.LoginUseCase;
import com.pongo.pongoedu.application.usecase.auth.RegistrarUsuarioUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final LoginUseCase loginUseCase;
    private final RegistrarUsuarioUseCase registrarUsuarioUseCase;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        TokenResponse response = loginUseCase.executar(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/registrar")
    public ResponseEntity<TokenResponse> registrar(@RequestBody RegistrarUsuarioRequest request) {
        TokenResponse response = registrarUsuarioUseCase.executar(request);
        return ResponseEntity.ok(response);
    }
}

