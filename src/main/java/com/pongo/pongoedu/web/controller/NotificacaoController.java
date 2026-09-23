package com.pongo.pongoedu.web.controller;

import com.pongo.pongoedu.application.dto.response.NotificacaoResponse;
import com.pongo.pongoedu.application.service.NotificacaoService;
import com.pongo.pongoedu.application.service.SessaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * RF18 - Enviar Novidades e Lembretes (Quadro 18): consulta do historico do usuario.
 */
@RestController
@RequestMapping("/notificacoes")
@RequiredArgsConstructor
public class NotificacaoController {

    private final NotificacaoService notificacaoService;
    private final SessaoService sessaoService;

    @GetMapping("/minhas")
    public List<NotificacaoResponse> listarMinhas() {
        return notificacaoService.listarDoUsuario(sessaoService.usuarioAtual().getId());
    }
}
