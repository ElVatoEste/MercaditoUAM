package com.vatodev.mercaditouam.Core.Exceptions;

public class InvalidRefreshTokenException extends CustomException {
    public InvalidRefreshTokenException() {
        super("Token de refresco inválido o expirado", "INVALID_REFRESH_TOKEN");
    }
}