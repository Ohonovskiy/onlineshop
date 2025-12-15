package ua.ohonovskiy.onlineshop.shared.exception;

public class OrderNotExistsException extends RuntimeException {
    public OrderNotExistsException(String errorMessage) {
        super(errorMessage);
    }
}
