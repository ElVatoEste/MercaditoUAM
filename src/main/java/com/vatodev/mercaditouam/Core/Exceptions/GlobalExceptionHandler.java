package com.vatodev.mercaditouam.Core.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.CONFLICT, ex.getErrorCode());
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<Object> handleInvalidRefreshTokenException(InvalidRefreshTokenException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.UNAUTHORIZED, ex.getErrorCode());
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException ex) {
        return buildResponseEntity(ex.getMessage(), HttpStatus.BAD_REQUEST, ex.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        return buildResponseEntity("Ocurrió un error inesperado", HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    private ResponseEntity<Object> buildResponseEntity(String message, HttpStatus status, String errorCode) {
        ApiErrorResponse response = new ApiErrorResponse(status.value(), message, errorCode);
        return new ResponseEntity<>(response, status);
    }
}
