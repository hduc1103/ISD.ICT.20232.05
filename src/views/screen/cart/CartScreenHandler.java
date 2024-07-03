package views.screen.cart;

import common.exception.MediaNotAvailableException;
import common.exception.PlaceOrderException;
import controller.PlaceOrderController;
import controller.ViewCartController;
import entity.cart.CartMedia;
import entity.media.Media;
import entity.order.Order;
import entity.order.OrderMedia;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.home.HomeScreenHandler;
import views.screen.popup.PopupScreen;
import views.screen.shipping.ShippingScreenHandler;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class CartScreenHandler extends BaseScreenHandler {

	private static Logger LOGGER = Utils.getLogger(CartScreenHandler.class.getName());

	@FXML
	private ImageView aimsImage;

	@FXML
	private Label pageTitle;

	@FXML
	VBox vboxCart;

	@FXML
	private Label shippingFees;

	@FXML
	private Label labelAmount;

	@FXML
	private Label labelSubtotal;

	@FXML
	private Label labelVAT;

	@FXML
	private Button btnPlaceOrder;

	@FXML
	private Button btnPlaceRushOrder;

	public CartScreenHandler(Stage stage, String screenPath) throws IOException {
		super(stage, screenPath);
		// fix relative image path caused by fxml
		File file = new File("assets/images/Logo.png");
		Image im = new Image(file.toURI().toString());
		aimsImage.setImage(im);
		setHomeScreenHandler(new HomeScreenHandler(this.stage, Configs.HOME_PATH));
		// on mouse clicked, we back to home
		aimsImage.setOnMouseClicked(e -> {
			setScreenTitle("Home Screen");
			homeScreenHandler.show();

		});
		// on mouse clicked, we start processing place order usecase
		btnPlaceOrder.setOnMouseClicked(e -> {
			LOGGER.info("Place Order button clicked");
			try {
				requestToPlaceOrder();
			} catch (SQLException | IOException exp) {
				LOGGER.severe("Cannot place the order, see the logs");
				exp.printStackTrace();
				throw new PlaceOrderException(Arrays.toString(exp.getStackTrace()).replaceAll(", ", "\n"));
			}
		});
		btnPlaceRushOrder.setOnMouseClicked(e -> {
			LOGGER.info("Place Rush Order button clicked");
			try {
				requestToPlaceRushOrder();
			} catch (SQLException | IOException exp) {
				LOGGER.severe("Cannot place the order, see the logs");
				exp.printStackTrace();
				throw new PlaceOrderException(Arrays.toString(exp.getStackTrace()).replaceAll(", ", "\n"));
			}
		});
	}

	public Label getLabelAmount() {
		return labelAmount;
	}

	public Label getLabelSubtotal() {
		return labelSubtotal;
	}

	public ViewCartController getBController(){
		return (ViewCartController) super.getBController();
	}

	public void requestToViewCart(BaseScreenHandler prevScreen) throws SQLException {
		setPreviousScreen(prevScreen);
		setScreenTitle("Cart Screen");
		//System.out.println(getBController().toString());
		getBController().checkAvailabilityOfProduct();
		displayCartWithMediaAvailability();
		show();
	}
	public void requestToPlaceOrder() throws SQLException, IOException {
		processOrder("standard order");
	}
	public void requestToPlaceRushOrder() throws SQLException, IOException {
		processOrder("rush order");
	}
	private boolean isCartEmpty(PlaceOrderController controller) {
		return controller.getListCartMedia().isEmpty();
	}

	private Order createOrderWithPaymentType(PlaceOrderController controller, String paymentType) throws SQLException, IOException {
		Order order = controller.createOrder();
		order.setTypePayment(paymentType);
		return order;
	}

	private void processOrder(String orderType) throws SQLException, IOException {
		try {
			PlaceOrderController placeOrderController = new PlaceOrderController();
			if (isCartEmpty(placeOrderController)) {
				PopupScreen.error("You don't have anything to place");
				return;
			}

			placeOrderController.placeOrder();
			displayCartWithMediaAvailability();
			Order order = createOrderWithPaymentType(placeOrderController, orderType);

			if ("rush order".equals(orderType) && !isRushOrderSupported(order)) {
				return;
			}

			displayShippingForm(order, placeOrderController);
		} catch (MediaNotAvailableException e) {
			displayCartWithMediaAvailability();
		}
	}

	private void displayShippingForm(Order order, PlaceOrderController placeOrderController) throws IOException {
		ShippingScreenHandler shippingScreenHandler = new ShippingScreenHandler(this.stage, Configs.SHIPPING_SCREEN_PATH, order);
		shippingScreenHandler.setScreenTitle("Shipping Screen");
		shippingScreenHandler.setPreviousScreen(this);
		shippingScreenHandler.setHomeScreenHandler(homeScreenHandler);
		setImage(aimsImage, "assets/images/Logo.png");
		aimsImage.setOnMouseClicked(e -> {
			setScreenTitle("Home screen");
			this.getPreviousScreen().show();
		});
		shippingScreenHandler.setBController(placeOrderController);
		shippingScreenHandler.show();
	}

	public void updateCart() throws SQLException{
		getBController().checkAvailabilityOfProduct();
		displayCartWithMediaAvailability();
	}

	void updateCartAmount(){
		// calculate subtotal and amount
		int subtotal = getBController().getCartSubtotal();
		int vat = (int)((Configs.PERCENT_VAT/100)*subtotal);
		int amount = subtotal + vat;
		LOGGER.info("amount " + amount);
		// update subtotal and amount of Cart
		labelSubtotal.setText(Utils.getCurrencyFormat(subtotal));
		labelVAT.setText(Utils.getCurrencyFormat(vat));
		labelAmount.setText(Utils.getCurrencyFormat(amount));
	}
	private boolean isRushOrderSupported(Order order) {
		StringBuilder unsupportedMediaNames = new StringBuilder();
		for (Object item : order.getlstOrderMedia()) {
			if (item instanceof OrderMedia) {
				Media media = ((OrderMedia) item).getMedia();
				if (!media.getIsSupportedPlaceRushOrder()) {
					if (unsupportedMediaNames.length() > 0) {
						unsupportedMediaNames.append(", ");
					}
					unsupportedMediaNames.append(media.getTitle());
				}
			}
		}

		if (unsupportedMediaNames.length() > 0) {
			try {
				PopupScreen.error("The following items in your cart do not support Rush order delivery: " + unsupportedMediaNames);
			} catch (IOException ex) {
			}
			return false;
		}
		return true;
	}
	private void displayCartWithMediaAvailability(){
		// clear all old cartMedia
		vboxCart.getChildren().clear();

		// get list media of cart after check availability
		List lstMedia = getBController().getListCartMedia();

		try {
			for (Object cm : lstMedia) {

				// display the attribute of vboxCart media
				CartMedia cartMedia = (CartMedia) cm;
				MediaHandler mediaCartScreen = new MediaHandler(Configs.CART_MEDIA_PATH, this);
				mediaCartScreen.setCartMedia(cartMedia);
				// add spinner
				vboxCart.getChildren().add(mediaCartScreen.getContent());
			}
			// calculate subtotal and amount
			updateCartAmount();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
