package org.example.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import org.example.utils.DBConnection;
import org.example.utils.MessageBox;
import org.example.views.media_items.OrderItem;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderController {

    private static final Logger logger = Logger.getLogger(OrderController.class.getName());
    private static final String PHONE_NUMBER_PATTERN = "^(08|09)\\d{7,9}$";

    /**
     * Validates the phone number format.
     *
     * @param phoneNumber the phone number to validate
     * @return true if the phone number is valid, false otherwise
     */
    public boolean validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(PHONE_NUMBER_PATTERN);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    /**
     * Searches for order items based on the provided phone number.
     *
     * @param phoneNumber the phone number to search for
     * @return a list of Parent nodes representing the order items
     * @throws ClassNotFoundException if the JDBC driver is not found
     */
    public List<Parent> getSearchItems(String phoneNumber) throws ClassNotFoundException {
        List<Parent> uiItems = new ArrayList<>();

        if (!validatePhoneNumber(phoneNumber)) {
            MessageBox.showAlert("Error in phone number", "Phone Number is incorrect format", "Wrong format", Alert.AlertType.WARNING);
            return uiItems;
        }

        String sql = "SELECT " +
                "    o.order_id, " +
                "    o.total_amounts, " +
                "    inv.transaction_id AS transaction_id, " +
                "    inv.status AS invoice_status, " +
                "    di.name AS receiver_name, " +
                "    di.phone_number, " +
                "    di.email, " +
                "    di.province, " +
                "    di.address " +
                "FROM `order` o " +
                "JOIN invoice inv ON o.order_id = inv.order_id " +
                "JOIN delivery_info di ON o.delivery_info_id = di.delivery_info_id " +
                "WHERE di.phone_number = ?;";

        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, phoneNumber);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Parent root = loadOrderItem(resultSet);
                uiItems.add(root);
            }
        } catch (SQLException | IOException e) {
            logger.log(Level.SEVERE, "Error accessing database or loading FXML", e);
            throw new RuntimeException("Error accessing database or loading FXML", e);
        }

        if (uiItems.isEmpty()) {
            MessageBox.showAlert("No order found", "Check phone number if it is correct", "No order found", Alert.AlertType.INFORMATION);
        }

        return uiItems;
    }

    /**
     * Loads an order item from the ResultSet and returns the corresponding Parent node.
     *
     * @param resultSet the ResultSet containing the order item data
     * @return the Parent node representing the order item
     * @throws IOException if an error occurs while loading the FXML
     * @throws SQLException if an error occurs while accessing the ResultSet
     */
    private Parent loadOrderItem(ResultSet resultSet) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/order_item.fxml"));
        Parent root = loader.load();

        OrderItem controller = loader.getController();
        controller.setInfo(
                resultSet.getString("order_id"),
                resultSet.getString("transaction_id"),
                resultSet.getString("total_amounts"),
                resultSet.getString("invoice_status"),
                resultSet.getString("receiver_name"),
                resultSet.getString("phone_number"),
                resultSet.getString("email"),
                resultSet.getString("province"),
                resultSet.getString("address")
        );

        return root;
    }
}
