package org.example.views.screens;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.controllers.PaymentController;
import org.example.exceptions.AddressNotSupportRushDeliveryException;
import org.example.exceptions.NoRushMediaException;
import org.example.models.Invoice;
import org.example.models.Transaction;

import java.io.IOException;

public class PaymentScreen {
    public Label titleLabel;
    public Button payButton;
    public Label totalAmountsLabel;
    public Label contentLabel;
    private PaymentController paymentController = new PaymentController();
    // data coupling
    // no cohesion
    public void setInvoice(Invoice invoice) {
        this.paymentController.setInvoice(invoice);
    }
    //data coupling
    //no cohesion
    @FXML
    public void requestPayment() throws AddressNotSupportRushDeliveryException, IOException, NoRushMediaException {
        double amounts = paymentController.getTotalAmounts();
        String contents = paymentController.getOrderContent();
        Transaction transaction = paymentController.makePayment(amounts,contents);
        loadResultScreen(transaction);
    }
    //no coupling
    // Procedural cohesion
    private void loadResultScreen(Transaction transaction) throws IOException {
        Stage stage = (Stage)(contentLabel.getScene()).getWindow();
        FXMLLoader resultLoader = new FXMLLoader(getClass().getResource("/fxml/result.fxml"));
        stage.setScene(new Scene(resultLoader.load()));
        ResultScreen resultScreen = resultLoader.getController();
        resultScreen.setResult(transaction);
    }
    // data coupling
    public void initData() throws AddressNotSupportRushDeliveryException, NoRushMediaException {
        totalAmountsLabel.setText(String.valueOf(paymentController.getTotalAmounts()));
        contentLabel.setText(paymentController.getOrderContent());
    }
}
