package com.amigoscode.product.exception;

public class ProductNotInStockException extends RuntimeException {
    public ProductNotInStockException(int id) {
        super("Not enough stock available for Product with Id " + id + ".");
    }
}
