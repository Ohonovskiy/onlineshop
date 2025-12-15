package ua.ohonovskiy.onlineshop.shared.exception;

public class ProductNotExistsException extends RuntimeException {
    public ProductNotExistsException(String errorMessage) {
        super(errorMessage);
    }
}
