package com.pongo.pongoedu.application.port;

public interface JwtProvider {
    String gerarToken(Long usuarioId, String email);
    Long extrairUsuarioId(String token);
    String extrairEmail(String token);
    boolean validarToken(String token);
    long obterTempoExpiracao();
}
