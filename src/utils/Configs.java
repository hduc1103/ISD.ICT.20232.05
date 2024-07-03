package utils;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.util.HashSet;
import java.util.Set;


public class Configs {

	public static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiIxMTg2MDlfZ3JvdXAxXzIwMjAiLCJpYXQiOjE1OTkxMTk5NDl9.y81pBkM0pVn31YDPFwMGXXkQRKW5RaPIJ5WW5r9OW-Y";

	// database Configs
	public static final String DB_NAME = "aims";
	public static final String DB_USERNAME = System.getenv("DB_USERNAME");
	public static final String DB_PASSWORD = System.getenv("DB_PASSWORD");
	public static final String MEDIA_DETAIL_PATH ="/views/fxml/media_detail.fxml" ;
	public static final String INVOICE_DETAIL_PATH ="/views/fxml/invoice_detail.fxml" ;

	public static String CURRENCY = "VND";
	public static float PERCENT_VAT = 10;

	// static resource
	public static final String IMAGE_PATH = "assets/images";
	public static final String INVOICE_SCREEN_PATH = "/views/fxml/invoice.fxml";
	public static final String INVOICE_MEDIA_SCREEN_PATH = "/views/fxml/media_invoice.fxml";
	public static final String PAYMENT_SCREEN_PATH = "/views/fxml/payment.fxml";
	public static final String RESULT_SCREEN_PATH = "/views/fxml/result.fxml";
	public static final String SPLASH_SCREEN_PATH = "/views/fxml/splash.fxml";
	public static final String CART_SCREEN_PATH = "/views/fxml/cart.fxml";
	public static final String SHIPPING_SCREEN_PATH = "/views/fxml/shipping.fxml";
	public static final String CART_MEDIA_PATH = "/views/fxml/media_cart.fxml";
	public static final String HOME_PATH  = "/views/fxml/home.fxml";
	public static final String HOME_ADMIN_PATH = "/views/fxml/home_admin.fxml";
	public static final String HOME_MEDIA_ADMIN_PATH = "/views/fxml/media_home_admin.fxml";
	public static final String HOME_MEDIA_PATH = "/views/fxml/media_home.fxml";
	public static final String POPUP_PATH = "/views/fxml/popup.fxml";
	public static final String INVOICE_LIST_PATH = "/views/fxml/invoice_list.fxml";
	public static final String INVOICE_LIST_ADMIN_PATH = "/views/fxml/invoice_list_admin.fxml";
	public static final String USER_LIST_PATH = "/views/fxml/user_list.fxml";

	public static final String PAY_SCREEN_PATH = "/views/fxml/pay.fxml";

	public static Font REGULAR_FONT = Font.font("Segoe UI", FontWeight.NORMAL, FontPosture.REGULAR, 24);

	public static String[] PROVINCES = { "Bắc Giang", "Bắc Kạn", "Cao Bằng", "Hà Giang", "Lạng Sơn", "Phú Thọ",
			"Quảng Ninh", "Thái Nguyên", "Tuyên Quang", "Yên Bái", "Điện Biên", "Hòa Bình", "Lai Châu", "Sơn La",
			"Bắc Ninh", "Hà Nam", "Hải Dương", "Hưng Yên", "Nam Định", "Ninh Bình", "Thái Bình", "Vĩnh Phúc", "Hà Nội",
			"Hải Phòng", "Hà Tĩnh", "Nghệ An", "Quảng Bình", "Quảng Trị", "Thanh Hóa", "Thừa Thiên-Huế", "Đắk Lắk",
			"Đắk Nông", "Gia Lai", "Kon Tum", "Lâm Đồng", "Bình Định", "Bình Thuận", "Khánh Hòa", "Ninh Thuận",
			"Phú Yên", "Quảng Nam", "Quảng Ngãi", "Đà Nẵng", "Bà Rịa-Vũng Tàu", "Bình Dương", "Bình Phước", "Đồng Nai",
			"Tây Ninh", "Hồ Chí Minh", "An Giang", "Bạc Liêu", "Bến Tre", "Cà Mau", "Đồng Tháp", "Hậu Giang",
			"Kiên Giang", "Long An", "Sóc Trăng", "Tiền Giang", "Trà Vinh", "Vĩnh Long", "Cần Thơ" };
	public static int SHIPPING_FEES = 30000;
	public static int SHIPPING_FEES_RUSH_ORDER = 20000;

	public static final Set<String> PROVINCES_FREE_SHIP = new HashSet<>();

	static {
		// Thêm các tỉnh/thành có free ship
		PROVINCES_FREE_SHIP.add("Hà Nội");
		PROVINCES_FREE_SHIP.add("Hồ Chí Minh");
	}
}
