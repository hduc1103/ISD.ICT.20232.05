package org.example.models;

import org.example.exceptions.NoMediaInCartException;
import org.example.exceptions.NoRushMediaException;
import org.example.utils.Config;
import org.example.exceptions.AddressNotSupportRushDeliveryException;
import org.example.utils.DBConnection;

import java.sql.*;
import java.util.HashMap;
import java.util.List;

public class Order {
    /**
     * Single responsibility:
     * Order should have only 1 responsibility for store data, calculate order amounts should be in another class ,
     * saveOrder should be stored in another class
     * Open-close: If there are adding more type of delivery,
     * meaning that it will need modification for some method
     */
    private int id;
    private Delivery delivery;
    private Cart cart = Cart.getCart();
    public int getId() {
        return id;
    }
    public Order(Delivery delivery) {
        this.delivery = delivery;
    }


    public Order() {
    }
    // Communicational cohesion
    public double getRushShipAmounts() throws AddressNotSupportRushDeliveryException, NoRushMediaException {
        if (!isRushDeliverySupported()) {
            throw new AddressNotSupportRushDeliveryException("Unsupported address!");
        }
        if (!cart.isRushDeliverySupport()) {
            throw new NoRushMediaException("No media support");
        }
        try {
            if (cart.getCartAmounts() > 100000) {
                return 0;
            }
        } catch (NoMediaInCartException e) {
            throw new RuntimeException(e);
        }
        // Functional cohesion
        double baseAmount = getBaseShippingAmount();
        double additionalWeightAmount = calculateAdditionalWeightAmount();
        double rushAmounts = (delivery.getClass().equals(RushDelivery.class)) ? cart.getRushAmounts() : 0;

        return baseAmount + additionalWeightAmount + rushAmounts;
    }
    // Communicational cohesion
    public double getShipAmounts() {
        try {
            // Common coupling
            if (cart.getCartAmounts() > 100000) {
                return 0;
            }
        } catch (NoMediaInCartException e) {
            return 0;
        }
        // Functional cohesion
        double baseAmount = getBaseShippingAmount();
        double additionalWeightAmount = calculateAdditionalWeightAmount();
        return baseAmount + additionalWeightAmount;
    }

    private boolean isRushDeliverySupported() {
        // Common coupling
        return delivery.getClass().equals(RushDelivery.class) && Config.rushSupportAddress.contains(delivery.getProvince());
    }

    private double getBaseShippingAmount() {
        if (delivery.getProvince().equals("Hà Nội") || delivery.getProvince().equals("Thành phố Hồ Chí Minh")) {
            return (cart.getWeights() <= 3) ? 22000 : 22000 + (cart.getWeights() - 3) * 5000;
        } else {
            return (cart.getWeights() <= 0.5) ? 30000 : 30000 + (cart.getWeights() - 0.5) * 5000;
        }
    }

    private double calculateAdditionalWeightAmount() {
        if (delivery.getClass().equals(Delivery.class)) {
            // Functional cohesion
            return getBaseShippingAmount() - 22000;
        } else {
            return 0;
        }
    }
    public double getTotalAmounts() throws AddressNotSupportRushDeliveryException, NoRushMediaException {
        // Logical cohesion
        if (delivery instanceof RushDelivery) {
            try {
                // Common coupling
                // Functional cohesion
                return getRushShipAmounts() + cart.getCartAmounts();
            } catch (NoMediaInCartException e) {
                return 0;
            }
        }
        else {
            try {
                // Functional cohesion
                // Common coupling
                return getShipAmounts() + cart.getCartAmounts();
            } catch (NoMediaInCartException e) {
                return 0;
            }
        }
    }
    public Order saveOrder(Delivery delivery) {
        // External coupling
        try (Connection connection = new DBConnection().getConnection()) {
            // Logical cohesion
            if (!isRushDeliverySupported()) {
                String insertOrderQuery = "INSERT INTO `order` (delivery_info_id, shipping_amounts, total_amounts, cart_amounts, rush_order) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement orderStatement = connection.prepareStatement(insertOrderQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    // Data coupling
                    orderStatement.setInt(1, delivery.getId());
                    // Functional cohesion
                    orderStatement.setDouble(2, getShipAmounts());
                    // Functional cohesion
                    orderStatement.setDouble(3, getTotalAmounts());
                    // Functional cohesion
                    orderStatement.setDouble(4, getCartAmounts());
                    orderStatement.setBoolean(5, delivery instanceof RushDelivery); // Assuming getRushDelivery returns a boolean
                    int affectedRows = orderStatement.executeUpdate();
                    if (affectedRows > 0) {
                        try (ResultSet generatedKeys = orderStatement.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                this.id = generatedKeys.getInt(1);
                                // Insert media items into the order_media table
                                String insertOrderMediaQuery = "INSERT INTO order_media (media_id, order_id, delivery_info_id, quantity) VALUES (?, ?, ?, ?)";
                                try (PreparedStatement orderMediaStatement = connection.prepareStatement(insertOrderMediaQuery)) {
                                    for (HashMap<Media, Integer> mediaEntry : cart.getMediaItems()) {
                                        for (Media media : mediaEntry.keySet()) {
                                            orderMediaStatement.setInt(1, media.getId());
                                            orderMediaStatement.setInt(2, id); // Assuming you have a method to get the order_id
                                            orderMediaStatement.setInt(3, delivery.getId()); // Assuming delivery.getId() returns the delivery_info_id
                                            orderMediaStatement.setInt(4, mediaEntry.get(media)); // Quantity
                                            orderMediaStatement.executeUpdate();
                                        }
                                    }
                                }
                            } else {
                                throw new SQLException("Failed to retrieve generated ID.");
                            }
                        }
                    } else {
                        throw new SQLException("Failed to insert delivery, no rows affected.");
                    }
                } catch (AddressNotSupportRushDeliveryException | NoRushMediaException e) {
                    throw new RuntimeException(e);
                }

            } else {
                // Insert order details into the order table
                String insertOrderQuery = "INSERT INTO `order` (delivery_info_id, shipping_amounts, total_amounts, cart_amounts, rush_order) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement orderStatement = connection.prepareStatement(insertOrderQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    orderStatement.setInt(1, delivery.getId()); // Assuming delivery.getId() returns the delivery_info_id
                    orderStatement.setDouble(2, getRushShipAmounts()); // Implement your shipping amount calculation logic
                    orderStatement.setDouble(3, getTotalAmounts()); // Implement your total amount calculation logic
                    orderStatement.setDouble(4, getCartAmounts()); // Implement your cart amount calculation logic
                    orderStatement.setBoolean(5, delivery instanceof RushDelivery); // Assuming getRushDelivery returns a boolean
                    int affectedRows = orderStatement.executeUpdate();
                    if (affectedRows > 0) {
                        try (ResultSet generatedKeys = orderStatement.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                int generatedId = generatedKeys.getInt(1);
                                this.id = generatedId;
                                //
                                String insertOrderMediaQuery = "INSERT INTO order_media (media_id, order_id, delivery_info_id, quantity) VALUES (?, ?, ?, ?)";
                                try (PreparedStatement orderMediaStatement = connection.prepareStatement(insertOrderMediaQuery)) {
                                    //Common coupling
                                    for (HashMap<Media, Integer> mediaEntry : cart.getMediaItems()) {
                                        for (Media media : mediaEntry.keySet()) {
                                            // Data coupling
                                            orderMediaStatement.setInt(1, media.getId());
                                            // Functional cohesion
                                            orderMediaStatement.setInt(2, id); // Assuming you have a method to get the order_id
                                            // Data coupling
                                            orderMediaStatement.setInt(3, delivery.getId()); // Assuming delivery.getId() returns the delivery_info_id
                                            orderMediaStatement.setInt(4, mediaEntry.get(media)); // Quantity
                                            orderMediaStatement.executeUpdate();
                                        }
                                    }
                                }
                                return this;
                            } else {
                                throw new SQLException("Failed to retrieve generated ID.");
                            }
                        }
                    } else {
                        throw new SQLException("Failed to insert delivery, no rows affected.");
                    }
                } catch (AddressNotSupportRushDeliveryException | NoRushMediaException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public double getCartAmounts() {
        try {
            return cart.getCartAmounts();
        } catch (NoMediaInCartException e) {
            return 0;
        }
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public List<HashMap<Media, Integer>> getMediaItems() {
        return cart.getMediaItems();
    }

    public boolean isRushDelivery() {
        return delivery instanceof RushDelivery;
    }

    public void removeMedia(Media media) {
        cart.removeMedia(media);
    }

    public void clearCart() {
        cart.clearCart();
    }
}
