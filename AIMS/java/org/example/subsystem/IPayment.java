package org.example.subsystem;

import org.example.models.Transaction;
import org.example.subsystem.VNPaySubsystem.methods.IPayMethod;
import org.example.subsystem.VNPaySubsystem.methods.IRefundMethod;
import org.example.subsystem.VNPaySubsystem.refund.RefundResponse;

public interface IPayment {
    /**
     * Interface Segregation: This
     */
    public Transaction payOrder(IPayMethod payMethod);

    public RefundResponse refund(IRefundMethod refundMethod);
}
