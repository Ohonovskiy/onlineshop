package ua.ohonovskiy.onlineshop.util.exception;

public class ShopNotExistsException extends RuntimeException {
    public ShopNotExistsException(String errorMessage) {
        super(errorMessage);
    }
}
