package org.example.controllers;

import org.example.exceptions.AddressNotSupportRushDeliveryException;
import org.example.exceptions.NoMediaInCartException;
import org.example.exceptions.NoRushMediaException;
import org.example.models.Cart;
import org.example.models.Delivery;
import org.example.models.Invoice;
import org.example.models.Order;
import org.example.models.RushDelivery;

import java.sql.SQLException;

public class DeliveryController {
    private final Order order;

    public DeliveryController() {
        this.order = new Order();
    }

    /**
     * Set the delivery information for the order.
     * @param delivery the delivery details
     */
    public void setDelivery(Delivery delivery) {
        order.setDelivery(delivery);
    }

    /**
     * Save the invoice for the given order.
     * @param order the order to save the invoice for
     * @return the saved invoice
     * @throws SQLException if a database access error occurs
     * @throws ClassNotFoundException if the JDBC driver is not found
     */
    public Invoice saveInvoice(Order order) throws SQLException, ClassNotFoundException {
        return Invoice.saveInvoice(order);
    }

    /**
     * Save the order with the given delivery information.
     * @param delivery the delivery details
     * @return the saved order
     */
    public Order saveOrder(Delivery delivery) {
        return order.saveOrder(delivery);
    }

    /**
     * Validate the delivery form.
     * @param delivery the delivery details
     * @return true if the form is valid, false otherwise
     */
    public boolean validateForm(Delivery delivery) {
        // Implement actual validation logic here
        return true;
    }

    /**
     * Check if rush delivery is supported for the items in the cart.
     * @return true if rush delivery is supported, false otherwise
     * @throws NoRushMediaException if no media supports rush delivery
     */
    public boolean checkRushDeliverySupport() throws NoRushMediaException {
        return Cart.getCart().isRushDeliverySupport();
    }

    /**
     * Check if the cart is empty.
     * @return true if the cart is empty, false otherwise
     */
    public boolean checkCartEmpty() {
        return Cart.getCart().isCartEmpty();
    }

    /**
     * Get the total amount of items in the cart.
     * @return the total amount
     */
    public double getCartAmounts() {
        return order.getCartAmounts();
    }

    /**
     * Get the shipping amount based on the delivery type.
     * @return the shipping amount
     * @throws AddressNotSupportRushDeliveryException if the address does not support rush delivery
     * @throws NoMediaInCartException if there are no media items in the cart
     * @throws NoRushMediaException if no media supports rush delivery
     */
    public double getShipAmounts() throws AddressNotSupportRushDeliveryException, NoMediaInCartException, NoRushMediaException {
        if (order.isRushDelivery()) {
            return order.getRushShipAmounts();
        }
        return order.getShipAmounts();
    }

    /**
     * Get the total amount including shipping.
     * @return the total amount
     * @throws AddressNotSupportRushDeliveryException if the address does not support rush delivery
     * @throws NoRushMediaException if no media supports rush delivery
     */
    public double getTotalAmounts() throws AddressNotSupportRushDeliveryException, NoRushMediaException {
        return order.getTotalAmounts();
    }

    /**
     * Save the delivery details.
     * @param delivery the delivery details
     * @throws SQLException if a database access error occurs
     * @throws ClassNotFoundException if the JDBC driver is not found
     */
    public void saveDelivery(Delivery delivery) throws SQLException, ClassNotFoundException {
        if (delivery.isRushDelivery()) {
            RushDelivery.saveDelivery(delivery);
        } else {
            Delivery.saveDelivery(delivery);
        }
    }

    /**
     * Get the current order.
     * @return the order
     */
    public Order getOrder() {
        return order;
    }
}
