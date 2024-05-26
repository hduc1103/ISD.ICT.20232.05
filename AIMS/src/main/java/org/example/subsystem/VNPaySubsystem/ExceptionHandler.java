package org.example.subsystem.VNPaySubsystem;

import org.example.exceptions.CanceledPaymentException;
import org.example.models.Transaction;

import java.beans.ExceptionListener;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;

public class ExceptionHandler {
    /**
     * Single responsibility:
     * This class have 1 responsibility to handle Exception
     * Open close:
     * e and the function name should be more clearly to avoid modification if there will be more exception to handle
     */
    public Transaction handleException(Exception e) {
        if (e instanceof CanceledPaymentException) {
            // External coupling
            return new Transaction(-1, "Payment was canceled", Timestamp.from(Instant.now()));
        }
        return null;
    }
}
