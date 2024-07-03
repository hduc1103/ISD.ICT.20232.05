package subsystem;

import entity.payment.PaymentTransaction;
import entity.response.Response;
import subsystem.vnPay.VnPaySubsystemController;

import java.io.IOException;
import java.text.ParseException;

public class VnPayStrategy implements PaymentStrategy {
    private VnPaySubsystemController ctrl;

    public VnPayStrategy() {
        this.ctrl = new VnPaySubsystemController();
    }

    @Override
    public String generatePayUrl(int amount, String contents) {
        try {
            return ctrl.generatePayOrderUrl(amount, contents);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PaymentTransaction makePaymentTransaction(Response response) throws ParseException {
        return ctrl.makePaymentTransaction(response);
    }
}
