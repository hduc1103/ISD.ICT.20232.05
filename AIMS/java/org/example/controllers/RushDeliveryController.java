package org.example.controllers;

import org.example.exceptions.AddressNotSupportRushDeliveryException;
import org.example.exceptions.NoRushMediaException;
import org.example.models.Media;
import org.example.models.Order;

import java.util.HashMap;
import java.util.List;

public class RushDeliveryController {
    private Order order;

    public void setOrder(Order order) {
        this.order = order;
    }
    public List<HashMap<Media, Integer>> getMediaItems() {
        return order.getMediaItems();
    }

    public double getCartAmounts() {
        return order.getCartAmounts();
    }
    public double getRushShipAmounts() throws AddressNotSupportRushDeliveryException, NoRushMediaException {
        return order.getRushShipAmounts();
    }
    public double getTotalAmounts() throws AddressNotSupportRushDeliveryException, NoRushMediaException {
        return order.getTotalAmounts();
    }

    public void removeMedia(Media media) {
        order.removeMedia(media);
    }
}
