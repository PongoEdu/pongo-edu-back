package com.pongo.pongoedu.domain.repository;

import com.pongo.pongoedu.domain.entity.Usuario;
import com.pongo.pongoedu.domain.enums.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    List<Usuario> findByPerfil(Perfil perfil);

    List<Usuario> findByAtivoTrue();
}
