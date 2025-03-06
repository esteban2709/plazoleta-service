package com.pragma.plazoletaservice.infraestructure.exeptionhandler;

public enum ExceptionResponse {
    NO_DATA_FOUND("No data was found for the requested operation"),
    USER_NOT_FOUND("No user was found for the requested operation"),
    ACCESS_DENIED("The user does not have permission to perform the requested operation");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}