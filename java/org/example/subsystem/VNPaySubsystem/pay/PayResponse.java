package org.example.subsystem.VNPaySubsystem.pay;

import org.example.utils.Config;
import org.example.models.Transaction;
import org.example.exceptions.CanceledPaymentException;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PayResponse {
    /**
     * This function have only 1 response is to make the transaction from the response_url
     */
    private int refId;
    private int id;
    private String contents;
    private String responseCode;
    private double amounts;
    private Timestamp payTime;

    public PayResponse(String responseString) throws CanceledPaymentException, MalformedURLException, UnsupportedEncodingException, ParseException {
        System.out.println(responseString);
        if (!responseString.startsWith(Config.vnp_ReturnUrl)){
            throw new CanceledPaymentException("Not follow enough paying step!");
        }
        Map<String, String> parameters = new HashMap<>();
        URL urlObject = new URL(responseString);
        String query = urlObject.getQuery();
        String[] paramPairs = query.split("&");
        for (String paramPair : paramPairs) {
            String[] keyValue = paramPair.split("=");
            if (keyValue.length == 2) {
                String key = URLDecoder.decode(keyValue[0], "UTF-8");
                String value = URLDecoder.decode(keyValue[1], "UTF-8");
                parameters.put(key, value);
            }
        }
        id = Integer.parseInt(parameters.get("vnp_TransactionNo"));
        amounts = Double.parseDouble(parameters.get("vnp_Amount"))/100;
        responseCode =  parameters.get("vnp_ResponseCode");
        refId = Integer.parseInt(parameters.get("vnp_TxnRef"));
        contents = parameters.get("vnp_OrderInfo");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date parsedDate = dateFormat.parse(parameters.get("vnp_PayDate"));
        payTime = new Timestamp(parsedDate.getTime());
    }

    public Transaction getTransaction() throws CanceledPaymentException, UnsupportedEncodingException, ParseException {
        Transaction transaction = new Transaction(id, amounts, contents, payTime, refId);
        switch (responseCode){
            case "00":
                break;
            default:
                throw new CanceledPaymentException("Payment was canceled!");
        }
        return transaction;
    }

}
