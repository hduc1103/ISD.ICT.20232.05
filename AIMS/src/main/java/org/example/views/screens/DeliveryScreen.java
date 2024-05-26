package org.example.views.screens;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.controllers.DeliveryController;
import org.example.exceptions.*;
import org.example.models.Delivery;
import org.example.models.Invoice;
import org.example.models.Order;
import org.example.models.RushDelivery;
import org.example.utils.Config;
import org.example.utils.MessageBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeliveryScreen {
    private static final Logger logger = Logger.getLogger(DeliveryScreen.class.getName());
    private final DeliveryController deliveryController = new DeliveryController();

    @FXML
    private TextField rushTimeField;
    @FXML
    private TextField rushAddressField;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField receiverField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField emailField;
    @FXML
    private TextArea instructionField;
    @FXML
    private ChoiceBox<String> provinceChoiceBox;
    @FXML
    private Button cancelButton;
    @FXML
    private Button submitButton;
    @FXML
    private CheckBox rushCheckBox;
    @FXML
    private Label shipAmountsLabel;
    @FXML
    private Label cartAmountsLabel;
    @FXML
    private Label totalAmountsLabel;

    @FXML
    private void initialize() {
        initializeRushCheckbox();
        initializeProvinceChoiceBox();
        updateCartAmounts();
        setInitialDelivery();

        provinceChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                updateDeliveryAndAmounts();
                if (!newValue.equals("Hà Nội")) {
                    rushCheckBox.setSelected(false);
                }
            }
        });
    }

    private void initializeRushCheckbox() {
        rushAddressField.setDisable(true);
        rushTimeField.setDisable(true);
        errorLabel.setText("");

        rushCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                handleRushCheckboxSelected();
            } else {
                rushAddressField.setDisable(true);
                rushTimeField.setDisable(true);
                submitButton.setDisable(false);
            }
        });
    }

    private void handleRushCheckboxSelected() {
        try {
            if (!deliveryController.checkRushDeliverySupport()) {
                showError("No media support rush delivery!");
                rushAddressField.setDisable(true);
                rushTimeField.setDisable(true);
                submitButton.setDisable(true);
                return;
            }
            rushAddressField.setDisable(false);
            rushTimeField.setDisable(false);
            RushDelivery newDelivery = (RushDelivery) getDeliveryFromForm();
            deliveryController.setDelivery(newDelivery);
            openRushDeliveryForm();
        } catch (IOException | AddressNotSupportRushDeliveryException | NoMediaInCartException | NoRushMediaException e) {
            showError("Address not support rush delivery");
            logger.log(Level.SEVERE, "Error handling rush checkbox selected", e);
        }
    }

    private void initializeProvinceChoiceBox() {
        ObservableList<String> provinces = FXCollections.observableArrayList(Config.provinces);
        provinceChoiceBox.setValue("Hà Nội");
        provinceChoiceBox.setItems(provinces);
    }

    private void updateCartAmounts() {
        cartAmountsLabel.setText(deliveryController.getCartAmounts() + " đồng");
    }

    private void setInitialDelivery() {
        Delivery delivery = getDeliveryFromForm();
        deliveryController.setDelivery(delivery);
        updateAmounts();
    }

    private void updateDeliveryAndAmounts() {
        Delivery newDelivery = getDeliveryFromForm();
        deliveryController.setDelivery(newDelivery);
        updateAmounts();
    }

    private void updateAmounts() {
        try {
            shipAmountsLabel.setText(deliveryController.getShipAmounts() + " đồng");
            totalAmountsLabel.setText(deliveryController.getTotalAmounts() + " đồng");
        } catch (AddressNotSupportRushDeliveryException | NoMediaInCartException | NoRushMediaException e) {
            logger.log(Level.SEVERE, "Error updating amounts", e);
            throw new RuntimeException(e);
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
    }

    private Delivery getDeliveryFromForm() {
        String receiverName = receiverField.getText();
        String email = emailField.getText();
        String address = addressField.getText();
        String phoneNumber = phoneField.getText();
        String province = provinceChoiceBox.getValue();
        String instruction = instructionField.getText();
        String rushTime = rushTimeField.getText();
        String rushAddress = rushAddressField.getText();

        if (rushCheckBox.isSelected()) {
            return new RushDelivery(receiverName, phoneNumber, province, instruction, address, email, rushTime, rushAddress);
        } else {
            return new Delivery(receiverName, phoneNumber, email, province, address, instruction);
        }
    }

    @FXML
    private void submitForm(ActionEvent e) throws IOException, SQLException, ClassNotFoundException {
        Delivery delivery = getDeliveryFromForm();
        try {
            validateAndSaveDelivery(delivery);
        } catch (InvalidInputException | NoMediaInCartException ex) {
            MessageBox.showAlert("Input error", "There is some error on your cart or delivery, please check it again!", ex.getMessage(), Alert.AlertType.WARNING);
            return;
        }
        processOrderAndInvoice(delivery, e);
    }

    private void validateAndSaveDelivery(Delivery delivery) throws SQLException, ClassNotFoundException, InvalidInputException, NoMediaInCartException {
        if (!deliveryController.validateForm(delivery)) {
            throw new InvalidInputException("Wrong input format!");
        } else if (deliveryController.checkCartEmpty()) {
            throw new NoMediaInCartException("Cart is empty!");
        }
        deliveryController.saveDelivery(delivery);
    }

    private void processOrderAndInvoice(Delivery delivery, ActionEvent e) throws SQLException, IOException, ClassNotFoundException {
        Order order = deliveryController.saveOrder(delivery);
        Invoice invoice = deliveryController.saveInvoice(order);
        loadInvoiceScreen(invoice, e);
    }

    private void loadInvoiceScreen(Invoice invoice, ActionEvent e) throws IOException {
        FXMLLoader invoiceLoader = new FXMLLoader(getClass().getResource("/fxml/invoice.fxml"));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Pane invoicePane = invoiceLoader.load();

        InvoiceScreen invoiceController = invoiceLoader.getController();
        invoiceController.setInvoice(invoice);
        invoiceController.initData();

        stage.setScene(new Scene(invoicePane));
    }

    @FXML
    private void backToCart(ActionEvent e) throws IOException {
        switchScene(e, "/fxml/cart.fxml");
    }

    private void switchScene(ActionEvent e, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
    }

    private void openRushDeliveryForm() throws IOException, AddressNotSupportRushDeliveryException, NoMediaInCartException, NoRushMediaException {
        Stage newWindow = new Stage();
        newWindow.setTitle("Rush Delivery");

        FXMLLoader rushLoader = new FXMLLoader(getClass().getResource("/fxml/rush_delivery.fxml"));
        Scene secondScene = new Scene(rushLoader.load(), 900, 600);
        RushDeliveryScreen screen = rushLoader.getController();

        deliveryController.setDelivery(getDeliveryFromForm());
        screen.setOrder(deliveryController.getOrder());
        screen.initData();

        newWindow.setScene(secondScene);
        newWindow.showAndWait();

        updateCartAmounts();
        updateAmounts();
    }
}
