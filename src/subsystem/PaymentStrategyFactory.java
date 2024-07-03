package subsystem;

public class PaymentStrategyFactory {
    public static PaymentStrategy getPaymentStrategy(String paymentMethod) {
        switch (paymentMethod) {
            case "VnPay":
            default:
                return new VnPayStrategy();
        }
    }
}
