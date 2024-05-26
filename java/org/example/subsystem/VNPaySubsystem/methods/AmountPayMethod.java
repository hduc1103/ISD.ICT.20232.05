package org.example.subsystem.VNPaySubsystem.methods;

import org.example.models.Transaction;
import org.example.subsystem.VNPaySubsystem.VNPayManager;

public class AmountPayMethod implements IPayMethod {
    private String contents;
    private double amounts;
    public AmountPayMethod(String contents, double amounts) {
        this.contents = contents;
        this.amounts = amounts;
    }
    @Override
    public Transaction pay() {
        return new VNPayManager().payAmountOrder(amounts, contents);
    }
}
