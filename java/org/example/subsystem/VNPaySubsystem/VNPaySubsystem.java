package org.example.subsystem.VNPaySubsystem;

import org.example.models.Transaction;
import org.example.subsystem.IPayment;
import org.example.subsystem.VNPaySubsystem.methods.IPayMethod;
import org.example.subsystem.VNPaySubsystem.methods.IRefundMethod;
import org.example.subsystem.VNPaySubsystem.refund.RefundResponse;

public class VNPaySubsystem  implements IPayment {
    /**
     * Single responsibility:
     * This class is having 1  responsibility to execute VNPAY functionality
     */
    @Override
    public RefundResponse refund(IRefundMethod refundMethod) {
        // Data coupling
        return refundMethod.refund();
    }

    @Override
    public Transaction payOrder(IPayMethod payMethod) {
        return payMethod.pay();
    }
}
