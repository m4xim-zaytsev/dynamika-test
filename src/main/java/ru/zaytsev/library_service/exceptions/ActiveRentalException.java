package ru.zaytsev.library_service.exceptions;

public class ActiveRentalException extends RuntimeException {
    public ActiveRentalException(String message) {
        super(message);
    }
}
