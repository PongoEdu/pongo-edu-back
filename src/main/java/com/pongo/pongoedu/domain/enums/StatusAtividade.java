package com.pongo.pongoedu.domain.enums;

public enum StatusAtividade {
    NAO_INICIADA("NAO_INICIADA"),
    EM_PROGRESSO("EM_PROGRESSO"),
    CONCLUIDA("CONCLUIDA"),
    CANCELADA("CANCELADA");

    private final String value;

    StatusAtividade(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
