package ua.ohonovskiy.onlineshop.shared.exception;

public class CartNotExistsException extends RuntimeException {
    public CartNotExistsException(String errorMessage) {
        super(errorMessage);
    }
}
