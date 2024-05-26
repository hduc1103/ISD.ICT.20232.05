package org.example.controllers;

import org.example.exceptions.AddressNotSupportRushDeliveryException;
import org.example.exceptions.NoRushMediaException;
import org.example.models.Order;
import org.example.models.Media;

import java.util.HashMap;
import java.util.List;

public class OrderService {
    public double getTotalAmounts(Order order) throws AddressNotSupportRushDeliveryException, NoRushMediaException {
        return order.getTotalAmounts();
    }

    public List<HashMap<Media, Integer>> getMediaItems(Order order) {
        return order.getMediaItems();
    }

    public void clearCart(Order order) {
        order.clearCart();
    }
}
