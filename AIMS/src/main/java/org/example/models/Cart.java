package org.example.models;

import org.example.exceptions.InvalidQuantityException;
import org.example.exceptions.NoMediaInCartException;
import org.example.exceptions.NotEnoughQuantityException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Cart {
    /**
     * Not Single responsibility:
     * Functions having 2 responsibility for mediaList and mediaBuyList.
     * Cart is used by singleton design pattern
     */

    private List<HashMap<Media,Integer>> mediaList = new ArrayList<HashMap<Media,Integer>>();
    private List<HashMap<Media,Integer>> mediaBuyList = new ArrayList<HashMap<Media,Integer>>();
    private Cart() {}
    private static Cart cart;
    public static Cart  getCart() {
        if (cart == null) {
            cart = new Cart();
        }
        return cart;
    }
    //Thêm sản phẩm trong giỏ hàng muốn mua
    // Communicational cohesion
    // Stamp coupling
    public void addBuyMedia(Media media, int quantity) throws NotEnoughQuantityException, InvalidQuantityException {
        if (quantity<0) {
            throw new InvalidQuantityException("Invalid quantity!");
        }
        if (quantity > media.getAvailable()) {
            throw new NotEnoughQuantityException("There are only " + media.getAvailable() + " available. You ask " + quantity);
        }
//        for (HashMap<Media, Integer> mediaBuy : mediaBuyList) {
//            if (mediaBuy.containsKey(media)) {
//                Integer currentQuantity = mediaBuy.get(media);
//                mediaBuy.put(media, currentQuantity + quantity);
//                return;
//            }
//        }

        HashMap<Media, Integer> newMediaBuyMap = new HashMap<>();
        newMediaBuyMap.put(media, quantity);
        mediaBuyList.add(newMediaBuyMap);
    }

    //Thêm sản phẩm vào giỏ hàng với số lượng cụ thể
    // Communicational cohesion
    public void addMedia(Media media, int quantity) throws NotEnoughQuantityException, InvalidQuantityException {
        if (quantity<0) {
            throw new InvalidQuantityException("Invalid quantity!");
        }
        if (quantity > media.getAvailable()) {
            throw new NotEnoughQuantityException("There are only " + media.getAvailable() + " available. You ask " + quantity);
        }
        for (HashMap<Media, Integer> mediaMap : mediaList) {
            if (mediaMap.containsKey(media)) {
                Integer currentQuantity = mediaMap.get(media);
                mediaMap.put(media, currentQuantity + quantity);
                return;
            }
        }
        HashMap<Media, Integer> newMediaMap = new HashMap<>();
        newMediaMap.put(media, quantity);
        mediaList.add(newMediaMap);
    }

    //Giảm số lượng sản phẩm
    // Communicational cohesion
    public void decreaseMedia(Media media){
        int quantity = 1;
        decreaseBuyMedia(media);
        for (HashMap<Media, Integer> mediaMap : mediaList) {
            if (mediaMap.containsKey(media)) {
                Integer currentQuantity = mediaMap.get(media);
                mediaMap.put(media, Math.max(0, currentQuantity - quantity)); // Ensure quantity doesn't go below 0
                return;
            }
        }
    }

    // Communicational cohesion
    public void decreaseBuyMedia(Media media){
        int quantity = 1;
        for (HashMap<Media, Integer> mediaMap : mediaBuyList) {
            if (mediaMap.containsKey(media)) {
                Integer currentQuantity = mediaMap.get(media);
                mediaMap.put(media, Math.max(0, currentQuantity - quantity)); // Ensure quantity doesn't go below 0
                return;
            }
        }
    }

    //Tăng số lượng sản phẩm
    // Communicational cohesion
    public void increaseMedia(Media media) throws NotEnoughQuantityException {
        int quantity = 1; // Increase by 1
        if (quantity > media.getAvailable()) {
            throw new NotEnoughQuantityException("There are only " + media.getAvailable() + " available. You asked for " + quantity);
        }
        increaseBuyMedia(media);
        for (HashMap<Media, Integer> mediaMap : mediaList) {
            if (mediaMap.containsKey(media)) {
                Integer currentQuantity = mediaMap.get(media);
                mediaMap.put(media, currentQuantity + quantity);
                return;
            }
        }
    }

    // Communicational cohesion
    public void increaseBuyMedia(Media media) throws NotEnoughQuantityException {
        int quantity = 1; // Increase by 1
        if (quantity > media.getAvailable()) {
            throw new NotEnoughQuantityException("There are only " + media.getAvailable() + " available. You asked for " + quantity);
        }
        for (HashMap<Media, Integer> mediaMap : mediaBuyList) {
            if (mediaMap.containsKey(media)) {
                Integer currentQuantity = mediaMap.get(media);
                mediaMap.put(media, currentQuantity + quantity);
                return;
            }
        }
    }


    //Xóa sản phẩm khỏi giỏ hàng
    // Communicational cohesion
    public void removeMedia(Media media) {
        removeBuyMedia(media);
        for (HashMap<Media, Integer> mediaMap : mediaList) {
            if (mediaMap.containsKey(media)) {
                mediaList.remove(mediaMap);
                System.out.println("Removed media in Cart");
                return;
            }
        }
    }

    //Loại sản phẩm đã chọn
    // Communicational cohesion
    public void removeBuyMedia(Media media) {
        for (HashMap<Media, Integer> mediaMap : mediaBuyList) {
            if (mediaMap.containsKey(media)) {
                mediaBuyList.remove(mediaMap);
                System.out.println("Removed media in Cart");
                return;
            }
        }
    }

    // Functional cohesion
    //Lấy tổng số tiền sản phẩm trong giỏ hàng
    public double getCartAmounts() throws NoMediaInCartException {
        if (isCartEmpty())
                throw new NoMediaInCartException("No media in cart");
        double cartAmounts = 0;
        for (HashMap<Media, Integer> mediaMap: mediaBuyList) {
            for (Media media : mediaMap.keySet()) {
                int quantity = mediaMap.get(media);
                double mediaPrice = media.getPrice();
                cartAmounts += quantity * mediaPrice;
            }
        }
        return cartAmounts;
    }


    // Communicational cohesion
    //Làm sạch giỏ hàng
    public void clearCart() {
        mediaList.clear();
    }

    public void clearByCart() {
        mediaBuyList.clear();
    }


    // Functional cohesion
    //Lấy tổng khối lượng sản phẩm
    public double getWeights() {
        double totalWeights = 0;
        for (HashMap<Media, Integer> mediaMap: mediaBuyList) {
            for (Media media : mediaMap.keySet()) {
                int quantity = mediaMap.get(media);
                double mediaWeight = media.getWeight();
                totalWeights += quantity * mediaWeight;
            }
        }
        return totalWeights;
    }
    // Functional cohesion
    public double getRushAmounts() {
        List<HashMap<Media, Integer>> rushMediaList =  mediaBuyList.stream()
                .filter(mediaMap -> mediaMap.keySet().stream().anyMatch(Media::isRushDelivery))
                .toList();
        double rushAmounts = 0;
        for (HashMap<Media, Integer> mediaMap: rushMediaList) {
            for (Media media : mediaMap.keySet()) {
                int quantity = mediaMap.get(media);
                rushAmounts += quantity * 10000;
            }
        }
        return rushAmounts;
    }

    // Data coupling
    // Communicational cohesion
    public List<HashMap<Media, Integer>> getMediaItems() {
        List<HashMap<Media, Integer>> currentMediaList = new ArrayList<>(mediaList);
        return currentMediaList;
    }
    // Data coupling
    // Communicational cohesion
    public List<HashMap<Media, Integer>> getMediaBuyItems() {
        List<HashMap<Media, Integer>> currentMediaList = new ArrayList<>(mediaBuyList);
        return currentMediaList;
    }
    // Functional cohesion
    public boolean isRushDeliverySupport() {
        for (HashMap<Media, Integer> mediaMap : mediaBuyList) {
            for (Media media : mediaMap.keySet()) {
                if (media.isRushDelivery()) {
                    return true;
                }
            }
        }
        return false;
    }
    // Functional cohesion
    public boolean isCartEmpty() {
        return mediaList.isEmpty();
    }
}
