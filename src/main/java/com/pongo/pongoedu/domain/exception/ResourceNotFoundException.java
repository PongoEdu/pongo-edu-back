package com.pongo.pongoedu.domain.exception;

public class ResourceNotFoundException extends DomainException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException of(String recurso, Object identificador) {
        return new ResourceNotFoundException(recurso + " nao encontrado: " + identificador);
    }
}
