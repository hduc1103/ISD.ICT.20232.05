package org.example.views.media_items;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.controllers.CartController;
import org.example.exceptions.InvalidQuantityException;
import org.example.exceptions.NotEnoughQuantityException;
import org.example.models.Media;
import org.example.utils.MessageBox;

import java.net.URL;
import java.util.ResourceBundle;

public class HomeMediaItem implements Initializable {
    private CartController cartController = new CartController();
    private Media media;
    public ImageView imageView;
    public Button incrementButton;
    public Button decrementButton;
    public Button addToCartButton;
    public Label mediaQuantityLabel;
    public Label mediaLabel;
    public Label priceLabel;
    public Label availableLabel;
    public Label typeLabel;


    //data coupling
    // Functional cohesion
    //S principle

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        incrementButton.setOnAction(e-> {
            mediaQuantityLabel.textProperty().set(String.valueOf(Integer.parseInt(mediaQuantityLabel.getText()) + 1));

        });
        decrementButton.setOnAction(e-> {
            if (Integer.parseInt(mediaQuantityLabel.getText()) > 0) {
                mediaQuantityLabel.textProperty().set(String.valueOf(Integer.parseInt(mediaQuantityLabel.getText()) - 1));
            } else {
                System.err.println("Cannot decrease item with 1 quantity");
            }
        });
        addToCartButton.setOnAction(e->{
            try {
                cartController.addMedia(media, Integer.parseInt(mediaQuantityLabel.getText()));
                MessageBox.showAlert("Added media","Media has been added","Added " + mediaQuantityLabel.getText() + " media " + media.getName(), Alert.AlertType.INFORMATION);
            } catch (NotEnoughQuantityException ex) {
                throw new RuntimeException(ex);
            } catch (InvalidQuantityException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    //data coupling
    //Coincidental
    public void setInfo(Media media) {
        this.media = media;
        try {
            // Load the image from the resources folder
            Image image = new Image(getClass().getResourceAsStream("/" + media.getImageUrl()));
            imageView.setImage(image);
            imageView.setPreserveRatio(true);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the error, e.g., log it or set a default image
        }
        mediaLabel.textProperty().set(media.getName());
        priceLabel.textProperty().set(media.getPrice() + " đồng");
        availableLabel.textProperty().set(String.valueOf(media.getAvailable()));
        typeLabel.textProperty().set(media.getCategory());
    }
}
