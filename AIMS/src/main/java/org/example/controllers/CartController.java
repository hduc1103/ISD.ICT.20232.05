package org.example.controllers;
import org.example.exceptions.NoMediaInCartException;
import org.example.models.Cart;
import org.example.models.Media;
import org.example.exceptions.InvalidQuantityException;
import org.example.exceptions.NotEnoughQuantityException;

import java.util.HashMap;
import java.util.List;

public class CartController {
    private Cart cart = Cart.getCart();


    //Tính tổng số tiền
    //Data Coupling
    public double getCartAmounts()  {
        try {
            return cart.getCartAmounts();
        } catch (NoMediaInCartException e) {
            return 0;
        }
    }

    //Giảm số lượng sản phẩm của giỏ hàng
    //Stamp Coupling - Giảm số lượng chỉ cần truyền id và quantity Media nhưng truyền cả Media
    //Functional cohesion
    public void decreaseMedia(Media media) {
        cart.decreaseMedia(media);
    }


    //Tăng số lượng sản phẩm trong giỏ hàng
    //Stamp Coupling - Tăng số lượng chỉ cần truyền id và quantity Media nhưng truyền cả Media
    //Functional cohesion
    public void increaseMedia(Media media) throws NotEnoughQuantityException {
        cart.increaseMedia(media);
    }

    //Lấy danh sách sản phẩm của giỏ hàng
    //Data Coupling
    //Functional cohesion
    public List<HashMap<Media, Integer>> getMediaItems() {
        return cart.getMediaItems();
    }

    //Lấy danh sách sản phẩm muốn mua
    //Data Coupling
    //Functional cohesion
    public List<HashMap<Media, Integer>> getMediaBuyItems() {
        return cart.getMediaBuyItems();
    }


    //Thêm sản phẩm vào giỏ hàng
    //Data Coupling
    //Functional cohesion
    public void addMedia(Media media, int quantity) throws NotEnoughQuantityException, InvalidQuantityException {
        cart.addMedia(media, quantity);
    }

    //Them san pham muon mua
    //Data Coupling
    //Functional cohesion
    public void addBuyMedia(Media media, int quantity) throws NotEnoughQuantityException, InvalidQuantityException {
        cart.addBuyMedia(media, quantity);
    }

    //Xóa sản phẩm khỏi giỏ hàng
    //Stamp Coupling
    //Functional cohesion
    public void removeMedia(Media media) {
        cart.removeMedia(media);
    }

    //Loại sản phẩm
    //Stamp Coupling
    //Functional cohesion
    public void removeBuyMedia(Media media) {
        cart.removeBuyMedia(media);
    }

    //Bỏ hết sản phẩm đã chọn
    //Data Coupling
    //Functional cohesion
    public void clearListBuyMedia() {
        cart.clearByCart();
    }
}
