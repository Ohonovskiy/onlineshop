package ua.ohonovskiy.onlineshop.util.exception;

public class ProductNotExistsException extends RuntimeException {
    public ProductNotExistsException(String errorMessage) {
        super(errorMessage);
    }
}
