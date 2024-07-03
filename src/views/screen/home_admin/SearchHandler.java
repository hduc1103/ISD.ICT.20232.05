package views.screen.home_admin;

import controller.HomeController;
import entity.media.Media;
import javafx.scene.control.TextField;
import views.screen.popup.PopupScreen;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class SearchHandler {

    private HomeController homeController;
    private AdminHomeScreenHandler adminHomeScreenHandler;

    public SearchHandler(AdminHomeScreenHandler adminHomeScreenHandler, TextField searchField) {
        this.adminHomeScreenHandler = adminHomeScreenHandler;
        this.homeController = adminHomeScreenHandler.getBController();
    }

    public void searchButtonClicked(String searchText) throws SQLException, IOException {
        List<Media> medium = homeController.getAllMedia();
        List<Media> filteredMedia = homeController.filterMediaByKeyWord(searchText, medium);
        List<MediaAdminHandler> filteredItems = adminHomeScreenHandler.convertMediaHandlerList(filteredMedia);
        adminHomeScreenHandler.checkEmpty(filteredItems);
    }
}
