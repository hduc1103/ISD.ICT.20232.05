package views.screen.home_admin;

import controller.AdminCRUDController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.util.Optional;

public class AddItemHandler {

    private AdminHomeScreenHandler adminHomeScreenHandler;

    public AddItemHandler(AdminHomeScreenHandler adminHomeScreenHandler) {
        this.adminHomeScreenHandler = adminHomeScreenHandler;
    }

    @FXML
    public void handleAddItem(ActionEvent event) throws SQLException {
        // Logic xử lý khi nút addItemBtn được nhấn
        // Nhập tiêu đề (title)
        TextInputDialog titleDialog = new TextInputDialog();
        titleDialog.setTitle("Add New Item");
        titleDialog.setHeaderText("Enter the title for the new media item:");
        titleDialog.setContentText("Title:");

        Optional<String> titleResult = titleDialog.showAndWait();
        if (!titleResult.isPresent() || titleResult.get().isEmpty()) {
            return; // Người dùng đóng hộp thoại hoặc không nhập giá trị
        }
        String title = titleResult.get();

        // Nhập danh mục (category)
        TextInputDialog categoryDialog = new TextInputDialog();
        categoryDialog.setTitle("Add New Item");
        categoryDialog.setHeaderText("Enter the category for the new media item:");
        categoryDialog.setContentText("Category:");

        Optional<String> categoryResult = categoryDialog.showAndWait();
        if (!categoryResult.isPresent() || categoryResult.get().isEmpty()) {
            return; // Người dùng đóng hộp thoại hoặc không nhập giá trị
        }
        String category = categoryResult.get();

        // Nhập giá (price)
        TextInputDialog priceDialog = new TextInputDialog();
        priceDialog.setTitle("Add New Item");
        priceDialog.setHeaderText("Enter the price for the new media item:");
        priceDialog.setContentText("Price:");

        Optional<String> priceResult = priceDialog.showAndWait();
        if (!priceResult.isPresent() || priceResult.get().isEmpty()) {
            return; // Người dùng đóng hộp thoại hoặc không nhập giá trị
        }
        int price;
        try {
            price = Integer.parseInt(priceResult.get());
        } catch (NumberFormatException e) {
            System.err.println("Invalid input for price: " + e.getMessage());
            return;
        }

        // Nhập số lượng (quantity)
        TextInputDialog quantityDialog = new TextInputDialog();
        quantityDialog.setTitle("Add New Item");
        quantityDialog.setHeaderText("Enter the quantity for the new media item:");
        quantityDialog.setContentText("Quantity:");

        Optional<String> quantityResult = quantityDialog.showAndWait();
        if (!quantityResult.isPresent() || quantityResult.get().isEmpty()) {
            return; // Người dùng đóng hộp thoại hoặc không nhập giá trị
        }
        int quantity;
        try {
            quantity = Integer.parseInt(quantityResult.get());
        } catch (NumberFormatException e) {
            System.err.println("Invalid input for quantity: " + e.getMessage());
            return;
        }

        // Nhập loại (type) using checkboxes
        Alert typeDialog = new Alert(Alert.AlertType.CONFIRMATION);
        typeDialog.setTitle("Add New Item");
        typeDialog.setHeaderText("Select the type for the new media item:");

        CheckBox bookCheckBox = new CheckBox("Book");
        CheckBox cdCheckBox = new CheckBox("CD");
        CheckBox dvdCheckBox = new CheckBox("DVD");

        VBox typeBox = new VBox(bookCheckBox, cdCheckBox, dvdCheckBox);
        typeDialog.getDialogPane().setContent(typeBox);

        Optional<ButtonType> typeResult = typeDialog.showAndWait();
        if (!typeResult.isPresent() || typeResult.get() != ButtonType.OK) {
            return; // Người dùng đóng hộp thoại hoặc không chọn giá trị
        }

        String type = "";
        if (bookCheckBox.isSelected()) {
            type = "Book";
        } else if (cdCheckBox.isSelected()) {
            type = "CD";
        } else if (dvdCheckBox.isSelected()) {
            type = "DVD";
        }

        if (type.isEmpty()) {
            System.err.println("No type selected.");
            return;
        }

        AdminCRUDController controller = new AdminCRUDController();
        controller.addNewMedia(title, type, category, "assets/images/book/book2.jpg", price, quantity);

        adminHomeScreenHandler.refreshMediaList();
    }
}
