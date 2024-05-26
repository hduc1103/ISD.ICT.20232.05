package org.example.models;

import org.example.exceptions.AddressNotSupportRushDeliveryException;
import org.example.exceptions.NoRushMediaException;
import org.example.utils.DBConnection;

import java.sql.*;
import java.util.HashMap;
import java.util.List;

public class Invoice {
    /**
     * Single responsibility:
     * Invoice should have only 1 responsibility for store data,
     * saveInvoice, updateTransaction, updateStatus should be stored in another class,
     * clearCart, getCartAmounts should be placed in another class
     */
    private int id;

    private Order order;
    private String status;
    private Transaction transaction;
    public Invoice(Order order) {
        this.order = order;
    }
    public Invoice(int id, Order order, String status, Transaction transaction) {
        this.id = id;
        this.order = order;
        this.status = status;
        this.transaction = transaction;
    }

    public static Invoice saveInvoice(Order order) throws SQLException, ClassNotFoundException {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }

        Invoice invoice = null;
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            // External coupling
            connection = new DBConnection().getConnection();
            String sql = "INSERT INTO invoice (order_id, amount, status) VALUES (?, ?, ?)";

            statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            // External coupling
            statement.setInt(1, order.getId()); // Assuming getOrderId() returns the order ID
            statement.setDouble(2, order.getTotalAmounts()); // Assuming getTotalAmounts() returns the total amount
            statement.setString(3, "pending");

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating invoice failed, no rows affected.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    // External coupling
                    invoice = new Invoice(generatedKeys.getInt(1), order, "pending", null);
                } else {
                    throw new SQLException("Creating invoice failed, no ID obtained.");
                }
            }
        } catch (SQLException | ClassNotFoundException | AddressNotSupportRushDeliveryException | NoRushMediaException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return invoice;
    }

    public void updateTransaction(int transactionId) {
        // Stamp coupling
        Connection connection = null;
        String dropForeignKeyQuery = "ALTER TABLE `invoice` DROP FOREIGN KEY `invoice_ibfk_1`";
        String updateInvoiceQuery = "UPDATE invoice SET transaction_id = ? WHERE invoice_id = ?";
        String addForeignKeyQuery = "ALTER TABLE `invoice` ADD CONSTRAINT `invoice_ibfk_1` FOREIGN KEY (`transaction_id`) REFERENCES `transaction` (`transaction_id`) ON UPDATE CASCADE";

        try {
            // External coupling
            connection = new DBConnection().getConnection();
            connection.setAutoCommit(false); // Start transaction

            try (Statement statement = connection.createStatement()) {
                // Drop foreign key
                statement.executeUpdate(dropForeignKeyQuery);

                // Update the invoice
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateInvoiceQuery)) {
                    preparedStatement.setInt(1, transactionId);
                    preparedStatement.setInt(2, id);
                    int affectedRows = preparedStatement.executeUpdate();
                    if (affectedRows == 0) {
                        throw new SQLException("Updating transaction failed, no rows affected.");
                    }
                }

                // Re-add foreign key
                statement.executeUpdate(addForeignKeyQuery);

                connection.commit(); // Commit transaction
            } catch (SQLException e) {
                connection.rollback(); // Rollback transaction on error
                throw e;
            } finally {
                connection.setAutoCommit(true); // Reset auto-commit to true
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // Handle the exception appropriately
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public int getId() {
        return 1;
    }

    public double getTotalAmounts() throws AddressNotSupportRushDeliveryException, NoRushMediaException {
        // Data coupling
        return order.getTotalAmounts();
    }

    public List<HashMap<Media, Integer>> getMediaItems() {
        // Data coupling
        return order.getMediaItems();
    }

    public static void updateStatus(int transactionId, String status) {
        // Stamp coupling
        String updateInvoiceQuery = "UPDATE invoice SET status = ? WHERE invoice.transaction_id = ?";

        try (Connection connection = new DBConnection().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateInvoiceQuery)) {

            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, transactionId);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating status failed, no rows affected.");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void clearCart() {
        // Data coupling
        order.clearCart();
    }
}
