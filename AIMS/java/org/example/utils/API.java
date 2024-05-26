package org.example.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class API {
    public static final String VNPAY_REFUND_URL = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";

    //Data Coupling
    //Functional cohesion
    public static JsonObject post(JsonObject jsonBody) {
        JsonObject responseJson = null;
        Gson gson = new Gson();

        try {
            URL url = new URL(VNPAY_REFUND_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // Convert JsonObject to String directly
            String jsonInputString = jsonBody.toString();

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    responseJson = gson.fromJson(reader, JsonObject.class);
                }
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseJson;
    }
}
