package views.screen.home_admin;

import controller.AdminCRUDController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import views.screen.userList.UserListHandler;
import utils.Configs;

import java.io.IOException;
import java.sql.SQLException;

public class ManageUserHandler {

    private AdminHomeScreenHandler adminHomeScreenHandler;

    public ManageUserHandler(AdminHomeScreenHandler adminHomeScreenHandler) {
        this.adminHomeScreenHandler = adminHomeScreenHandler;
    }

    @FXML
    public void handleManageUser(ActionEvent event) {
        try {
            UserListHandler userListHandler = new UserListHandler(adminHomeScreenHandler.getStage(), Configs.USER_LIST_PATH);
            userListHandler.setHomeScreenHandler(adminHomeScreenHandler);
            userListHandler.setBController(new AdminCRUDController());
            userListHandler.requestToUserList(adminHomeScreenHandler);
        } catch (IOException | SQLException ex) {
            ex.printStackTrace();
        }
    }
}
