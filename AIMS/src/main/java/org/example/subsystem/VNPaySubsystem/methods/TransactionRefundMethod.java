package org.example.subsystem.VNPaySubsystem.methods;

import org.example.subsystem.VNPaySubsystem.VNPayManager;
import org.example.subsystem.VNPaySubsystem.refund.RefundResponse;

public class TransactionRefundMethod implements IRefundMethod{
    private String transactionId;

    public TransactionRefundMethod(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public RefundResponse refund() {
        return new VNPayManager().refund(transactionId);
    }
}
