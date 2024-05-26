package org.example.subsystem.VNPaySubsystem;

import com.google.gson.JsonObject;
import org.example.exceptions.CanceledPaymentException;
import org.example.exceptions.InvalidInputException;
import org.example.models.Transaction;
import org.example.subsystem.VNPaySubsystem.pay.PayRequest;
import org.example.subsystem.VNPaySubsystem.pay.PayResponse;
import org.example.subsystem.VNPaySubsystem.refund.RefundRequest;
import org.example.subsystem.VNPaySubsystem.refund.RefundResponse;
import org.example.utils.API;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.text.ParseException;

public class VNPayManager {
    /**
     * Single responsibility:
     * This class is having 2 responsibility to handle is refund and pay
     * Should remove refund
     */
    private VNPayView view= new VNPayView();
    private Validator validator = new Validator();
    private ExceptionHandler handler = new ExceptionHandler();

    public Transaction payAmountOrder(double amounts, String content) {
        try {
            // Data coupling
            if (validator.validatePaymentInput(amounts, content)) {
                // Data coupling
                PayRequest payRequest = new PayRequest();
                String url = payRequest.createUrl(content, amounts);
                PayResponse payResponse = new PayResponse(view.query(url));
                return payResponse.getTransaction();
            }
        } catch (InvalidInputException | CanceledPaymentException | MalformedURLException |
                 UnsupportedEncodingException | ParseException e) {
            // Data coupling
            return handler.handleException(e);
        }
        return null;
    }

    public RefundResponse refund(String transactionId) {
        // Data coupling
        Transaction transaction = Transaction.findById(Integer.parseInt(transactionId));
        RefundRequest request = null;
        try {
            // External coupling
            request = new RefundRequest(transaction);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        // External coupling
        JsonObject jsonRequest = request.createJson();
        // Common coupling
        JsonObject jsonResponse = API.post(jsonRequest);
        // External coupling
        return new RefundResponse().fromJson(String.valueOf(jsonResponse));
    }
}
