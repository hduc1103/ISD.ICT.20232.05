package org.example.views.media_items;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.controllers.OrderItemController;

public class OrderItem {
    private final OrderItemController controller = new OrderItemController();
    public Label orderIdLabel;
    public Label totalAmountLabel;
    public Label statusLabel;
    public Label addressLabel;
    public Label receiverLabel;
    public Label phoneNumberLabel;
    public Label provinceLabel;
    public Label emailLabel;
    public Button cancelButton;
    public Label transactionIdIdLabel;
    //data coupling
     //Coincidental
    public void setInfo(String orderId, String transactionId, String totalAmount, String status,
                        String receiverName, String phoneNumber, String email,
                        String province, String address) {
        transactionIdIdLabel.setText(transactionId);
        orderIdLabel.setText(orderId);
        totalAmountLabel.setText(totalAmount);
        statusLabel.setText(status);
        receiverLabel.setText(receiverName);
        phoneNumberLabel.setText(phoneNumber);
        emailLabel.setText(email);
        provinceLabel.setText(province);
        addressLabel.setText(address);
        cancelButton.setDisable(!statusLabel.getText().equals("paid"));
    }

    //data coupling
    //Coincidental
    public void cancelOrder() {
        String transactionId = transactionIdIdLabel.getText();
        String orderId = orderIdLabel.getText();
        controller.cancelOrder(transactionId, orderId);
        statusLabel.setText("refunded");
        cancelButton.setDisable(true);
    }
}
