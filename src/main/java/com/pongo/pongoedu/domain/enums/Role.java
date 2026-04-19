package com.pongo.pongoedu.domain.enums;

public enum Role {
    ADMIN("ADMIN"),
    PROFESSOR("PROFESSOR"),
    ALUNO("ALUNO"),
    AUXILIAR("AUXILIAR");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
