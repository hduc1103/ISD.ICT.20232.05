package org.example.controllers;

import org.example.exceptions.AddressNotSupportRushDeliveryException;
import org.example.exceptions.NoRushMediaException;
import org.example.models.Invoice;
import org.example.models.Transaction;
import org.example.subsystem.IPayment;
import org.example.subsystem.VNPaySubsystem.VNPaySubsystem;
import org.example.subsystem.VNPaySubsystem.methods.AmountPayMethod;

public class PaymentController{
    private Invoice invoice;
    IPayment subsystem = new VNPaySubsystem();

    //Data Coupling
    //Functional cohesion
    public Transaction makePayment(double amounts, String contents) {
        Transaction transaction = subsystem.payOrder(new AmountPayMethod(contents, amounts));
        Transaction.saveTransaction(transaction);
        invoice.updateTransaction(transaction.getTransactionId());
        if (transaction.getAmount() == -1) {
            Invoice.updateStatus(transaction.getTransactionId(),"canceled");
        } else {
            Invoice.updateStatus(transaction.getTransactionId(),"paid");
        }
        emailToCustomer();
        clearCart();
        return transaction;
    }

    //Data Coupling
    private void clearCart() {
        invoice.clearCart();
    }


    private void emailToCustomer() {
        //TODO: Implement send email
    }


    //Data Coupling
    //Functional cohesion
    public double getTotalAmounts() throws AddressNotSupportRushDeliveryException, NoRushMediaException {
        return invoice.getTotalAmounts();
    }

    //Data Coupling
    //Functional cohesion
    public String getOrderContent() throws AddressNotSupportRushDeliveryException, NoRushMediaException {
        return "Paying for order " + invoice.getId() + " for " + (int)invoice.getTotalAmounts();
    }

    //Data Coupling
    //Functional cohesion
    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}
