package com.pongo.pongoedu.application.dto.request;

import com.pongo.pongoedu.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrarUsuarioRequest {
    private String email;
    private String nome;
    private String senha;
    private Role role;
}
