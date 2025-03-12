package com.pragma.plazoletaservice.domain.exception;

public enum ExceptionMessage {
    INVALID_NIT_FORMAT("NIT must contain only numeric characters"),
    USER_NOT_FOUND("No user was found for the requested operation"),
    PHONE_INCORRECT_FORMAT("Phone number must be max 13 characters and can only contain numbers and '+' symbol"),
    INVALID_RESTAURANT_NAME("Restaurant name cannot contain only numbers"),
    INVALID_OWNER("User is not an owner"),
    USER_NOT_OWNER("User is not an owner"),
    CATEGORY_NOT_FOUND("No category was found for the requested operation"),
    RESTAURANT_NOT_FOUND("No restaurant was found for the requested operation"),
    DISH_NOT_FOUND("No dish was found for the requested operation"),
    NOT_RESTAURANT_OWNER("User is not the owner of the restaurant"),
    CLIENT_HAS_ACTIVE_ORDER("Client already has an order in progress. Cannot create a new order."),
    INVALID_STATE_TRANSITION("Only orders in READY status can be changed to DELIVERED status"),
    DELIVERED_ORDER_CANNOT_BE_MODIFIED("Delivered orders cannot be modified"),
    INVALID_SECURITY_CODE("The provided security code is invalid"),
    ORDER_IS_PREPARING("Sorry, your order is already being prepared and cannot be canceled."),;

    private final String message;

    ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
