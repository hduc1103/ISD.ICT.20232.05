package controller;

import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.invoice.Invoice;
import entity.media.Media;
import entity.order.Order;
import entity.order.OrderMedia;
import utils.Configs;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

public class PlaceOrderController extends BaseController{

    /**
     * Just for logging purpose
     */
    private static Logger LOGGER = utils.Utils.getLogger(PlaceOrderController.class.getName());

    /**
     * This method checks the avalibility of product when user click PlaceOrder button
     * @throws SQLException
     */
    public void placeOrder() throws SQLException{
        Cart.getCart().checkAvailabilityOfProduct();
    }

    /**
     * This method creates the new Order based on the Cart
     * @return Order
     * @throws SQLException
     */
    public Order createOrder() throws SQLException{
        Order order = new Order();
        for (Object object : Cart.getCart().getListMedia()) {
            CartMedia cartMedia = (CartMedia) object;
            OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(),
                    cartMedia.getQuantity(),
                    cartMedia.getPrice());
            order.addOrderMedia(orderMedia);
        }
        return order;
    }

    /**
     * This method creates the new Invoice based on order
     * @param order
     * @return Invoice
     */
    public Invoice createInvoice(Order order, int userId) throws SQLException {
        //this.interbankInterface = new InterbankSubsystem();
        //String id = this.interbankInterface.getUrlPayOrder(order.getAmount() + calculateShippingFee(order));
        Invoice invoice = new Invoice(order);
        invoice.saveInvoice(userId);
        return invoice;
    }

    /**
     * This method takes responsibility for processing the shipping info from user
     * @param info
     * @throws InterruptedException
     * @throws IOException
     */
    public void processDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException {
        LOGGER.info("Process Delivery Info");
        LOGGER.info(info.toString());
        // Validate delivery info using DeliveryValidator
        DeliveryValidator.validateDeliveryInfo(info);
    }

    /**
     * This method calculates the shipping fees of order
     * @param order
     * @return shippingFee
     */
    public int calculateShippingFee(Order order) {
        String province = String.valueOf(order.getDeliveryInfo().get("province"));
        boolean isRushOrder = order.getTypePayment().toLowerCase().equals("rush order");

        if (Configs.PROVINCES_FREE_SHIP.contains(province)) {
            return isRushOrder ? Configs.SHIPPING_FEES_RUSH_ORDER : 0;
        }
        return Configs.SHIPPING_FEES;
    }
}
