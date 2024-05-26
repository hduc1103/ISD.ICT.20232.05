package org.example.exceptions;

public class NotEnoughQuantityException extends Exception{
    public NotEnoughQuantityException(String message) {
        super(message);
    }
}
