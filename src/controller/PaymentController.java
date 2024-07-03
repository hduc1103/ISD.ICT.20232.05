package controller;

import entity.cart.Cart;
import subsystem.PaymentStrategy;

public class PaymentController extends BaseController {
	private PaymentStrategy paymentStrategy;

	public PaymentController(PaymentStrategy paymentStrategy) {
		this.paymentStrategy = paymentStrategy;
	}

	public String getUrlPay(int amount, String content) {
		return paymentStrategy.generatePayUrl(amount, content);
	}

	public void emptyCart() {
		Cart.getCart().emptyCart();
	}

	public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
		this.paymentStrategy = paymentStrategy;
	}
}
