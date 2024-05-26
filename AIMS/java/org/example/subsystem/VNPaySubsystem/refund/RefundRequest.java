package org.example.subsystem.VNPaySubsystem.refund;

// Import necessary libraries
import com.google.gson.JsonObject;
import org.example.models.Transaction;
import org.example.utils.Config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class RefundRequest {
    /**
     * this request class have only 1 responsibility for create the JsonObject
     */
    private final String vnp_RequestId = Config.getRandomNumber(8); // Assuming you need a random response ID
    private final String vnp_Version = "2.1.0";
    private final String vnp_Command = "refund";
    private final String vnp_TmnCode = Config.vnp_TmnCode;
    private final String vnp_TransactionType = "02";
    private String vnp_TxnRef;
    private String vnp_Amount;
    private String vnp_OrderInfo;
    private String vnp_TransactionNo;
    private String vnp_TransactionDate;
    private final String vnp_CreateBy = "admin";
    private String vnp_CreateDate;
    private final String vnp_IpAddr = "192.168.1.16";
    private String vnp_SecureHash;

    public RefundRequest(Transaction transaction) throws ParseException {
        vnp_Amount = String.valueOf((long)transaction.getAmount()*100);
        vnp_OrderInfo = transaction.getContent();
        vnp_TransactionNo = String.valueOf(transaction.getId());
        vnp_TransactionDate = String.valueOf(transaction.getTime());
        vnp_TxnRef = String.valueOf(transaction.getRefId());

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        vnp_CreateDate = formatter.format(cld.getTime());

        // Create data for checksum
        Map<String, String> fields = new HashMap<>();
        fields.put("vnp_RequestId", vnp_RequestId);
        fields.put("vnp_Version", vnp_Version);
        fields.put("vnp_Command", vnp_Command);
        fields.put("vnp_TmnCode", vnp_TmnCode);
        fields.put("vnp_TransactionType", vnp_TransactionType);
        fields.put("vnp_TxnRef", vnp_TxnRef);
        fields.put("vnp_Amount", vnp_Amount);
        fields.put("vnp_TransactionNo", vnp_TransactionNo);

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        Date originalDate = originalFormat.parse(vnp_TransactionDate);
        vnp_TransactionDate = targetFormat.format(originalDate);
        fields.put("vnp_TransactionDate", vnp_TransactionDate);


        fields.put("vnp_CreateBy", vnp_CreateBy);
        fields.put("vnp_CreateDate", vnp_CreateDate);
        fields.put("vnp_IpAddr", vnp_IpAddr);
        fields.put("vnp_OrderInfo", vnp_OrderInfo);

        String dataForChecksum = createDataForChecksum(fields);
        vnp_SecureHash = Config.hmacSHA512(Config.vnp_HashSecret, dataForChecksum);
    }

    private static String createDataForChecksum(Map<String, String> fields) {
        // Correct order as per VNPAY format
        String[] fieldOrder = new String[] {
                "vnp_RequestId",
                "vnp_Version",
                "vnp_Command",
                "vnp_TmnCode",
                "vnp_TransactionType",
                "vnp_TxnRef",
                "vnp_Amount",
                "vnp_TransactionNo",
                "vnp_TransactionDate",
                "vnp_CreateBy",
                "vnp_CreateDate",
                "vnp_IpAddr",
                "vnp_OrderInfo"
        };

        StringBuilder dataBuilder = new StringBuilder();
        for (String fieldName : fieldOrder) {
            if (fields.containsKey(fieldName)) {
                if (dataBuilder.length() > 0) {
                    dataBuilder.append("|");
                }
                dataBuilder.append(fields.get(fieldName));
            }
        }
        return dataBuilder.toString();
    }


    public JsonObject createJson() {
        JsonObject json = new JsonObject();
        json.addProperty("vnp_RequestId", vnp_RequestId);
        json.addProperty("vnp_Version", vnp_Version);
        json.addProperty("vnp_Command", vnp_Command);
        json.addProperty("vnp_TmnCode", vnp_TmnCode);
        json.addProperty("vnp_TransactionType", vnp_TransactionType);
        json.addProperty("vnp_TxnRef", vnp_TxnRef);
        json.addProperty("vnp_Amount", vnp_Amount);
        json.addProperty("vnp_TransactionNo", vnp_TransactionNo);
        json.addProperty("vnp_TransactionDate", vnp_TransactionDate);
        json.addProperty("vnp_CreateBy", vnp_CreateBy);
        json.addProperty("vnp_CreateDate", vnp_CreateDate);
        json.addProperty("vnp_IpAddr", vnp_IpAddr);
        json.addProperty("vnp_OrderInfo", vnp_OrderInfo);
        json.addProperty("vnp_SecureHash", vnp_SecureHash);
        return json;
    }

}
