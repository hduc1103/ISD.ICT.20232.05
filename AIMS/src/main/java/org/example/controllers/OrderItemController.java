package org.example.controllers;

import javafx.scene.control.Alert;
import org.example.models.Invoice;
import org.example.subsystem.IPayment;
import org.example.subsystem.VNPaySubsystem.VNPaySubsystem;
import org.example.subsystem.VNPaySubsystem.methods.TransactionRefundMethod;
import org.example.subsystem.VNPaySubsystem.refund.RefundResponse;
import org.example.utils.MessageBox;

public class OrderItemController {
    private final IPayment iPayment = new VNPaySubsystem();

    //Data Coupling
    //Functional cohesion
    public void cancelOrder(String transactionId, String orderId) {
        RefundResponse refundResponse = iPayment.refund(new TransactionRefundMethod(transactionId));
        if (refundResponse != null) {
            Invoice.updateStatus(Integer.parseInt(transactionId), "refunded");
            MessageBox.showAlert("Cancel order result", "Cancel order success", "Cancel order with id " + orderId + " success", Alert.AlertType.INFORMATION);
        } else {
            MessageBox.showAlert("Cancel order result", "Cancel order failed", "Cancel order with id " + orderId + " failed", Alert.AlertType.ERROR);
        }
    }
}
