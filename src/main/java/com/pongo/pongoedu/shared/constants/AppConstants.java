package com.pongo.pongoedu.shared.constants;

public class AppConstants {
    // JWT Constants
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";
    public static final long JWT_EXPIRATION_TIME = 86400000; // 24 hours

    // API Constants
    public static final String API_VERSION = "/v1";
    public static final String BASE_PATH = "/api" + API_VERSION;

    // Validation Constants
    public static final int MIN_SENHA_LENGTH = 8;
    public static final int MAX_NOME_LENGTH = 255;

    // XP Constants
    public static final int XP_POR_ATIVIDADE = 100;
    public static final int XP_BONUS_PRIMEIRA_CONCLUSAO = 50;

    // HTTP Status
    public static final int STATUS_OK = 200;
    public static final int STATUS_CREATED = 201;
    public static final int STATUS_BAD_REQUEST = 400;
    public static final int STATUS_UNAUTHORIZED = 401;
    public static final int STATUS_FORBIDDEN = 403;
    public static final int STATUS_NOT_FOUND = 404;
    public static final int STATUS_INTERNAL_ERROR = 500;

    // Messages
    public static final String MSG_SUCCESS = "Operação realizada com sucesso";
    public static final String MSG_ERROR = "Erro ao processar a operação";
    public static final String MSG_NOT_FOUND = "Recurso não encontrado";
    public static final String MSG_UNAUTHORIZED = "Não autorizado";
    public static final String MSG_VALIDATION_ERROR = "Erro de validação";
}

