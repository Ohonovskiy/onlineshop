package ua.ohonovskiy.onlineshop.util.exception;

public class CartNotExistsException extends RuntimeException {
    public CartNotExistsException(String errorMessage) {
        super(errorMessage);
    }
}
