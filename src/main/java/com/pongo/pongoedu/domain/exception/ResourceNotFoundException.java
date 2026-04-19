package com.pongo.pongoedu.domain.exception;

public class ResourceNotFoundException extends DomainException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public static ResourceNotFoundException of(String resource, Long id) {
        return new ResourceNotFoundException(resource + " com id " + id + " não encontrado");
    }

    public static ResourceNotFoundException of(String resource, String identifier) {
        return new ResourceNotFoundException(resource + " com identificador " + identifier + " não encontrado");
    }
}
