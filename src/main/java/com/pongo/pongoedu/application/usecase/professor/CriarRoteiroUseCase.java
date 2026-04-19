package com.pongo.pongoedu.application.usecase.professor;

import com.pongo.pongoedu.domain.entities.Roteiro;
import com.pongo.pongoedu.domain.exception.ResourceNotFoundException;
import com.pongo.pongoedu.domain.repository.ProfessorRepository;
import com.pongo.pongoedu.domain.repository.RoteiroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CriarRoteiroUseCase {
    private final RoteiroRepository roteiroRepository;
    private final ProfessorRepository professorRepository;

    public Roteiro executar(Long professorId, String titulo, String descricao) {
        var professor = professorRepository.buscarPorId(professorId)
            .orElseThrow(() -> ResourceNotFoundException.of("Professor", professorId));

        Roteiro roteiro = new Roteiro();
        roteiro.setTitulo(titulo);
        roteiro.setDescricao(descricao);
        roteiro.setProfessor(professor);
        roteiro.setDataCriacao(LocalDateTime.now());

        return roteiroRepository.salvar(roteiro);
    }
}
