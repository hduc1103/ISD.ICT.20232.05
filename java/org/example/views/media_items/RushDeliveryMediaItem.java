package org.example.views.media_items;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.example.controllers.RushDeliveryController;
import org.example.models.Media;

import java.util.HashMap;

public class RushDeliveryMediaItem {
    private RushDeliveryController controller;
    private Media media;
    public ImageView imageView;
    public Label mediaQuantityLabel;
    public Button removeMediaButton;
    public Label priceLabel;
    public Label mediaLabel;
    public Label deliveryTypeLabel;
    private VBox mediaContainer;

    //no coupling
    // Functional cohesion
    public void removeMedia() {
        controller.removeMedia(media);
        if (mediaContainer != null) {
            mediaContainer.getChildren().remove(this.imageView.getParent());
        }
    }

    //no coupling
//    Coincidental
    public void setItemContainer(VBox mediaContainer) {
        this.mediaContainer = mediaContainer;
    }

    //data coupling
    // Coincidental
    public void setInfo(HashMap<Media, Integer> media) {
        Media mediaItem = media.keySet().iterator().next();
        int quantity = media.get(mediaItem);
        this.media = mediaItem;
        try {
            // Load the image from the resources folder
            Image image = new Image(getClass().getResourceAsStream("/" + mediaItem.getImageUrl()));
            imageView.setImage(image);
            imageView.setPreserveRatio(true);
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the error, e.g., log it or set a default image
        }
        mediaLabel.setText(mediaItem.getName());
        priceLabel.setText(String.valueOf(mediaItem.getPrice()));
        deliveryTypeLabel.setText(mediaItem.isRushDelivery() ? "Giao hàng nhanh" : "Giao hàng thường");
        mediaQuantityLabel.setText(String.valueOf(quantity));
    }
    //data coupling
    //Coincidental
    public void setController(RushDeliveryController controller) {
        this.controller = controller;
    }
}
