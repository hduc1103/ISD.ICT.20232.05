package views.screen.payment;

import controller.PaymentController;
import entity.db.AIMSDB;
import entity.invoice.Invoice;
import entity.response.Response;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import subsystem.PaymentStrategyFactory;
import utils.Configs;
import views.screen.BaseScreenHandler;
import views.screen.invoice.InvoiceScreenHandler;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentScreenHandler extends BaseScreenHandler {
	public Response response;

	@FXML
	private VBox vBox;

	private Invoice invoice;

	@FXML
	private ImageView back;

	private PaymentController paymentController;

	public PaymentScreenHandler(Stage stage, String screenPath, Invoice invoice) throws IOException {
		super(stage, screenPath);
		this.invoice = invoice;
		paymentController = new PaymentController(PaymentStrategyFactory.getPaymentStrategy("VnPay"));
		back.setOnMouseClicked(e -> {
			setScreenTitle("Invoice screen");
			this.getPreviousScreen().show();
		});
		displayWebView();
	}

	private void displayWebView() {
		var paymentUrl = paymentController.getUrlPay(invoice.getAmount(), "Thanh toan hoa don AIMS");
		System.out.println(paymentUrl);
		WebView paymentView = new WebView();
		WebEngine webEngine = paymentView.getEngine();
		System.out.println(paymentUrl);
		webEngine.load(paymentUrl);
		webEngine.locationProperty().addListener((observable, oldValue, newValue) -> {
			// Handle URL changes
			System.out.println(newValue);
			if (newValue.contains("http://127.0.0.1:50387/?")) {
				response = new Response(newValue);
				handlePaymentResponse(response);
			}
		});
		vBox.getChildren().clear();
		vBox.getChildren().add(paymentView);
	}

	private void handlePaymentResponse(Response response) {
		try {
			if ("00".equals(response.getVnp_ResponseCode())) {
				paymentController.emptyCart();
				System.out.println("Successful Payment");
				BaseScreenHandler resultScreenHandler = new ResultScreenHandler(
						this.stage, Configs.RESULT_SCREEN_PATH, "PAYMENT RESULT", "SUCCESSFUL!",
						String.valueOf(invoice.getId()), response.getVnp_BankCode(),
						response.getVnp_BankTranNo(), String.valueOf(invoice.getAmount()),
						response.getVnp_TransactionStatus(), response.getVnp_PayDate());
				resultScreenHandler.setPreviousScreen(this);
				resultScreenHandler.setScreenTitle("Result");
				resultScreenHandler.show();
				this.invoice.setPaypalId(response.getVnp_BankTranNo());
				this.invoice.updateStatus("PAYMENT COMPLETED");
				updateStock();
			} else {
				BaseScreenHandler invoiceScreenHandler = new InvoiceScreenHandler(
						this.stage, Configs.INVOICE_SCREEN_PATH, invoice);
				invoiceScreenHandler.setScreenTitle("Invoice Screen");
				invoiceScreenHandler.show();
			}
		} catch (IOException | SQLException e) {
			e.printStackTrace();
			// Optionally handle the exception with a user-friendly message
		}
	}

	public void updateStock() throws SQLException {
		int invoiceId = invoice.getId();
		Connection connection = AIMSDB.getConnection();
		int orderid;
		String sql0 = "SELECT orderId FROM Invoice WHERE id = ?";
		try (PreparedStatement preInvoice = connection.prepareStatement(sql0)) {
			preInvoice.setInt(1, invoiceId);
			try (ResultSet res0 = preInvoice.executeQuery()) {
				if (res0.next()) {
					orderid = res0.getInt(1);
				} else {
					throw new SQLException("Invoice with ID " + invoiceId + " not found.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}

		String sql1 = "SELECT mediaID, quantity FROM OrderMedia WHERE orderID = ?";
		try (PreparedStatement preOrder = connection.prepareStatement(sql1)) {
			preOrder.setInt(1, orderid);
			try (ResultSet res1 = preOrder.executeQuery()) {
				while (res1.next()) {
					int mediaid = res1.getInt("mediaID");
					int quantity = res1.getInt("quantity");

					String sql2 = "UPDATE Media SET quantity = quantity - ? WHERE id = ?";
					try (PreparedStatement preMedia = connection.prepareStatement(sql2)) {
						preMedia.setInt(1, quantity);
						preMedia.setInt(2, mediaid);
						int rowAffected = preMedia.executeUpdate();
						if (rowAffected > 0) {
							System.out.println("Media with ID " + mediaid + " updated successfully");
						} else {
							System.out.println("No media with ID " + mediaid + " exists");
						}
					} catch (SQLException e) {
						throw new RuntimeException(e);
					}
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
