package com.vatodev.mercaditouam.Core.Exceptions;

public class UserAlreadyExistsException extends CustomException {
    public UserAlreadyExistsException() {
        super("El nombre de usuario o el correo electrónico ya está en uso.", "USER_ALREADY_EXISTS");
    }
}