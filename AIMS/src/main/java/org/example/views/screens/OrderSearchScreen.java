package org.example.views.screens;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.controllers.OrderController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderSearchScreen {
    private final OrderController orderController = new OrderController();
    public Button searchButton;
    public TextField phoneTextField;
    public VBox orderContainer;
    public Button backToHomeButton;

    public void searchOrder() {
        String phoneNumber = phoneTextField.getText(); // Get the phone number from the text field
        List<Parent> orderItems = new ArrayList<>(); // Initialize the list to hold the UI items

        try {
            // Try to get the list of order UI items based on the phone number
            orderItems = orderController.getSearchItems(phoneNumber);
        } catch (Exception e) {
            // Handle exceptions such as ClassNotFoundException, SQLException, IOException etc.
            e.printStackTrace(); // Print the stack trace to the console or log it
            // Display an error message to the user or log it, depending on your application's requirements
        }

        // Clear the previous search results
        orderContainer.getChildren().clear();

        // Add all the loaded UI items to the container
        orderContainer.getChildren().addAll(orderItems);
    }

    public void backToHome(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader homeLoader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
        stage.setScene(new Scene(homeLoader.load()));
    }
}
