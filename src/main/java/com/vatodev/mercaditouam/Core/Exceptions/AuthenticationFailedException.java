package com.vatodev.mercaditouam.Core.Exceptions;

public class AuthenticationFailedException extends CustomException {
    public AuthenticationFailedException() {
        super("Credenciales incorrectas.", "AUTH_FAILED");
    }
}
