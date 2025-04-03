package com.onebox.ecommerce.exception;

import lombok.Data;

/**
 * Custom class for handling RuntimeExceptions
 */
@Data
public class CartServiceCustomException extends RuntimeException {

    private String errorCode;

    public CartServiceCustomException(String message, String errorCode){
        super(message);
        this.errorCode = errorCode;
    }
}
