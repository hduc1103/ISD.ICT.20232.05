package org.example.exceptions;

public class CanceledPaymentException extends Exception{
    public CanceledPaymentException(String message) {
        super(message);
    }
}
