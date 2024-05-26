package org.example.views.screens;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.controllers.InvoiceController;
import org.example.exceptions.AddressNotSupportRushDeliveryException;
import org.example.exceptions.NoRushMediaException;
import org.example.models.Invoice;
import org.example.models.Media;
import org.example.views.media_items.InvoiceMediaItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InvoiceScreen{
    private InvoiceController invoiceController = new InvoiceController();
    @FXML
    public Button confirmButton;
    @FXML
    public Button cancelButton;
    @FXML
    public Label totalAmountsLabel;
    @FXML
    public VBox mediaContainer;

    // data coupling
    // Coincidental
    public void setInvoice(Invoice invoice) {
        invoiceController.setInvoice(invoice);
    }

    // no coupling
    // no cohesion
    @FXML
    public void backToHome(ActionEvent e) throws IOException {
        //Load home screen
        FXMLLoader deliveryLoader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(deliveryLoader.load()));
    }

    //data coupling
    // no cohesion
    @FXML
    public void confirmInvoice(ActionEvent e) throws IOException, AddressNotSupportRushDeliveryException, NoRushMediaException {
        //Set invoice for the next screen
        Invoice invoice = invoiceController.getInvoice();
        //Load the payment screen
        FXMLLoader paymentLoader = new FXMLLoader(getClass().getResource("/fxml/payment.fxml"));
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(paymentLoader.load()));
        //Init data
        PaymentScreen paymentScreen = paymentLoader.getController();
        paymentScreen.setInvoice(invoice);
        paymentScreen.initData();
    }

    //data coupling
    //Functional cohesion
    public void initData () {

        java.util.List<HashMap<Media, Integer>> mediaList = invoiceController.getMediaItems();
        List<Pane> mediaPanes = new ArrayList<>();
        mediaList.forEach(media -> {
            try {
                FXMLLoader mediaLoader = new FXMLLoader(getClass().getResource("/fxml/invoice_media.fxml"));
                Pane item = mediaLoader.load();
                InvoiceMediaItem mediaItemController = mediaLoader.getController();
                //Set info for every media invoice item
                mediaItemController.setInfo(media);
                mediaPanes.add(item);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        mediaContainer.getChildren().addAll(mediaPanes);
        try {
            totalAmountsLabel.setText(invoiceController.getCartAmounts() + " đồng");
        } catch (AddressNotSupportRushDeliveryException e) {
            throw new RuntimeException(e);
        }
    }

}
