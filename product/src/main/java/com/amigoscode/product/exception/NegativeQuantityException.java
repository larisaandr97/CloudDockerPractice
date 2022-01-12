package com.amigoscode.product.exception;

public class NegativeQuantityException extends RuntimeException {
    public NegativeQuantityException() {
        super("The quantity cannot be zero or a negative number!");
    }
}
