package ua.ohonovskiy.onlineshop.util.exception;

public class ImportNotExistsException extends RuntimeException {
    public ImportNotExistsException(String errorMessage) {
        super(errorMessage);
    }
}
