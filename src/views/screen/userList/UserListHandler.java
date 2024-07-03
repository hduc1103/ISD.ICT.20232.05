package views.screen.userList;

import entity.user.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;
import javafx.stage.Stage;
import utils.Utils;
import views.screen.BaseScreenAdminHandler;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class UserListHandler extends BaseScreenAdminHandler implements Initializable {

    public static Logger LOGGER = Utils.getLogger(UserListHandler.class.getName());

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, Integer> idColumn;

    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private TableColumn<User, String> phoneColumn;

    @FXML
    private TableColumn<User, String> passwordColumn;

    @FXML
    private TableColumn<User, String> addressColumn;

    @FXML
    private TableColumn<User, Void> deleteColumn;

    @FXML
    private Button backButton;

    public UserListHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        addDeleteButtonToTable();

        try {
            userTable.getItems().setAll(User.getAllUsers());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        backButton.setOnAction(event -> handleBackButton());
    }

    private void addDeleteButtonToTable() {
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    try {
                        if (User.deleteUserById(user.getId())) {
                            getTableView().getItems().remove(user);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
    }

    public void requestToUserList(BaseScreenAdminHandler prev) {
        setPreviousScreen(prev);
        show();
    }

    private void handleBackButton() {
        getPreviousScreen().show();
    }
}
