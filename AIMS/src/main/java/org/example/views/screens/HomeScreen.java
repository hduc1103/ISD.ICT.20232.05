package org.example.views.screens;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.controllers.HomeController;
import org.example.models.Media;
import org.example.views.media_items.HomeMediaItem;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeScreen implements Initializable {
    private static final Logger logger = Logger.getLogger(HomeScreen.class.getName());
    private final HomeController homeController;

    @FXML
    private VBox mediaContainer;
    @FXML
    private Button orderGoButton;
    @FXML
    private AnchorPane mediaAnchorPane;
    @FXML
    private TextField searchField;
    @FXML
    private ChoiceBox<String> searchType;
    @FXML
    private ScrollPane sp;
    @FXML
    private Button redirectPayBtn;

    public HomeScreen() {
        this.homeController = new HomeController();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchType.getItems().addAll("NAME", "CATEGORY", "PRICE");
        searchType.setValue("NAME");

        loadMediaItems(homeController.getMediaItems());

        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchMedia());
    }

    private void loadMediaItems(List<Media> mediaList) {
        List<Pane> mediaPanes = new ArrayList<>();
        for (Media media : mediaList) {
            try {
                FXMLLoader mediaLoader = new FXMLLoader(getClass().getResource("/fxml/home_media.fxml"));
                Pane item = mediaLoader.load();
                HomeMediaItem mediaItemController = mediaLoader.getController();
                mediaItemController.setInfo(media);
                mediaPanes.add(item);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error loading media item", e);
                throw new RuntimeException(e);
            }
        }
        mediaContainer.getChildren().setAll(mediaPanes);
        sp.setContent(mediaContainer);
    }

    @FXML
    private void searchMedia() {
        List<Media> mediaList = homeController.searchMediaList(searchType.getValue(), searchField.getText());
        if (mediaList.isEmpty()) {
            showAlert("No media found", "No media items found for the given search criteria.", Alert.AlertType.WARNING);
            loadMediaItems(homeController.getMediaItems());
        } else {
            loadMediaItems(mediaList);
        }
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void viewCart(ActionEvent e) throws IOException {
        switchScene(e, "/fxml/cart.fxml");
    }

    @FXML
    private void viewOrderSearch(ActionEvent e) throws IOException {
        switchScene(e, "/fxml/search_order.fxml");
    }

    private void switchScene(ActionEvent e, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}
