package views.screen.authentication;

import controller.AuthenticationController;
import entity.user.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.SessionManager;
import views.screen.home.HomeScreenHandler;
import views.screen.home_admin.AdminHomeScreenHandler;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class AuthenticationHandler implements Initializable {

    @FXML
    private Button login_btn;

    @FXML
    private Button login_createAccount;

    @FXML
    private AnchorPane login_form;

    @FXML
    private PasswordField login_password;

    @FXML
    private CheckBox login_selectshowPassword;

    @FXML
    private TextField login_showPassword;

    @FXML
    private TextField login_username;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button signup_btn;

    @FXML
    private PasswordField signup_cPassword;

    @FXML
    private TextField signup_email;

    @FXML
    private AnchorPane signup_form;

    @FXML
    private Button signup_loginAccount;

    @FXML
    private PasswordField signup_password;

    @FXML
    private TextField signup_username;

    private AuthenticationController authenticationController;

    public void login() throws IOException {
        if (authenticationController == null) authenticationController = new AuthenticationController();

        alertMessage alert = new alertMessage();
        if (login_username.getText().isEmpty() ||
                (login_selectshowPassword.isSelected() && login_showPassword.getText().isEmpty()) ||
                (!login_selectshowPassword.isSelected() && login_password.getText().isEmpty())) {
            alert.errorMessage("Username and Password are Required");

        } else {
            try {
                String password = login_selectshowPassword.isSelected() ? login_showPassword.getText() : login_password.getText();
                User user = authenticationController.loginWithUsernameAndPassword(login_username.getText(), password);
                if (user == null) {
                    alert.errorMessage("Incorrect Username/Password");
                } else {
                    SessionManager.setLoggedIn(true);
                    SessionManager.setUserId(user.getId()); // Store userId in session

                    Stage stage = (Stage) login_btn.getScene().getWindow();
                    if (user.getId() != 0) {
                        HomeScreenHandler homeHandler = new HomeScreenHandler(stage, Configs.HOME_PATH);
                        homeHandler.setScreenTitle("Home Screen Customer");
                        homeHandler.setImage();
                        homeHandler.show();
                    } else {
                        AdminHomeScreenHandler homeHandler = new AdminHomeScreenHandler(stage, Configs.HOME_ADMIN_PATH);
                        homeHandler.setScreenTitle("Home Screen Admin");
                        homeHandler.setImage();
                        homeHandler.show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Login error: " + e.getMessage());
            }
        }
    }

    public void register() throws SQLException {
        if (authenticationController == null) authenticationController = new AuthenticationController();

        alertMessage alert = new alertMessage();

        // Check for empty fields
        if (signup_email.getText().isEmpty() || signup_username.getText().isEmpty()
                || signup_password.getText().isEmpty() || signup_cPassword.getText().isEmpty()) {
            alert.errorMessage("All fields are necessary to be filled!");
        } // Check matched password
        else if (!Objects.equals(signup_cPassword.getText(), signup_password.getText())) {
            alert.errorMessage("Password does not match!");
        } // Check password length
        else if (signup_password.getText().length() < 8) {
            alert.errorMessage("Invalid Password, at least 8 characters or more!");
        } else {
            int result = authenticationController.registerNewUser(signup_email.getText(), signup_username.getText(), signup_password.getText());
            if (result == 0) {
                alert.errorMessage(signup_username.getText() + " is already existed or some error had happened");
            } else {
                alert.successMessage("Registered Successfully!!");
                clearRegisterField();

                signup_form.setVisible(false);
                login_form.setVisible(true);
            }
        }
    }

    public void clearRegisterField() {
        signup_username.setText("");
        signup_email.setText("");
        signup_cPassword.setText("");
        signup_password.setText("");
    }

    public void switchForm(ActionEvent e) {
        if (e.getSource() == signup_loginAccount) {
            signup_form.setVisible(false);
            login_form.setVisible(true);
        } else if (e.getSource() == login_createAccount) {
            signup_form.setVisible(true);
            login_form.setVisible(false);
        }
    }

    public void showPassword() {
        if (login_selectshowPassword.isSelected()) {
            login_showPassword.setText(login_password.getText());
            login_showPassword.setVisible(true);
            login_password.setVisible(false);
        } else {
            login_password.setText(login_showPassword.getText());
            login_showPassword.setVisible(false);
            login_password.setVisible(true);
        }
    }

    public void openApp() {
        login_form.setVisible(true);
        signup_form.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        openApp();
    }
}
