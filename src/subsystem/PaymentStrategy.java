package subsystem;

import common.exception.PaymentException;
import common.exception.UnrecognizedException;
import entity.payment.PaymentTransaction;
import entity.response.Response;

import java.text.ParseException;

public interface PaymentStrategy {
    /**
     * Generate the payment URL for the given amount and contents.
     */
    String generatePayUrl(int amount, String contents) throws PaymentException, UnrecognizedException;

    /**
     * Create a payment transaction from the response.
     */
    PaymentTransaction makePaymentTransaction(Response response) throws ParseException;
}
