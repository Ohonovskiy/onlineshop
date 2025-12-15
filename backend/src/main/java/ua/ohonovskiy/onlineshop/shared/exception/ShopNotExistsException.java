package ua.ohonovskiy.onlineshop.shared.exception;

public class ShopNotExistsException extends RuntimeException {
    public ShopNotExistsException(String errorMessage) {
        super(errorMessage);
    }
}
