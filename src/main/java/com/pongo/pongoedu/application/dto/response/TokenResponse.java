package com.pongo.pongoedu.application.dto.response;

public record TokenResponse(
        String token,
        String tipo,
        long expiraEmMs,
        UsuarioResponse usuario
) {
    public static TokenResponse de(String token, long expiraEmMs, UsuarioResponse usuario) {
        return new TokenResponse(token, "Bearer", expiraEmMs, usuario);
    }
}
