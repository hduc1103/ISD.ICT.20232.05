package org.example.subsystem.VNPaySubsystem.refund;

import com.google.gson.Gson;

public class RefundResponse {
    /**
     * This function have only 1 responsibility is to make the RefundResponse from the jsonString
     */

    private String vnp_ResponseId;
    private String vnp_Command;
    private String vnp_ResponseCode;
    private String vnp_Message;
    private String vnp_TmnCode;
    private String vnp_TxnRef;
    private String vnp_Amount;
    private String vnp_OrderInfo;
    private String vnp_BankCode;
    private String vnp_PayDate;
    private String vnp_TransactionNo;
    private String vnp_TransactionType;
    private String vnp_TransactionStatus;
    private String vnp_SecureHash;

    public RefundResponse fromJson(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, RefundResponse.class);
    }
}
