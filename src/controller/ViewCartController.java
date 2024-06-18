package controller;

import java.sql.SQLException;
import java.util.List;

import entity.media.Media;
import entity.cart.Cart;
import entity.cart.CartMedia;

//funtional cohesion
public class ViewCartController extends BaseController{
    
    /**
     * This method checks the available products in Cart
     * @throws SQLException
     * Data Coupling
     */
    public void checkAvailabilityOfProduct() throws SQLException{
        Cart.getCart().checkAvailabilityOfProduct();
    }

    /**
     * This method calculates the cart subtotal
     * @return subtotal
     * Data Coupling
     */
    public int getCartSubtotal(){
        int subtotal = Cart.getCart().calSubtotal();
        return subtotal;
    }

}
