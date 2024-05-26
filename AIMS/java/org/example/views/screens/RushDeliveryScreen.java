package org.example.views.screens;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.controllers.RushDeliveryController;
import org.example.exceptions.AddressNotSupportRushDeliveryException;
import org.example.exceptions.NoRushMediaException;
import org.example.models.Media;
import org.example.models.Order;
import org.example.views.media_items.RushDeliveryMediaItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RushDeliveryScreen {
    public VBox mediaContainer;
    public Button confirmButton;
    public Label cartAmountsLabel;
    public Label shipAmountsLabel;
    public Label totalAmountsLabel;
    private RushDeliveryController rushDeliveryController = new RushDeliveryController();

    // no coupling
    public void confirmRushDelivery(ActionEvent e) {
        Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage.close();
    }

    //data coupling
    private void loadData(){
        try {
            totalAmountsLabel.setText(rushDeliveryController.getTotalAmounts() + " đồng");
            cartAmountsLabel.setText(rushDeliveryController.getCartAmounts() +" đồng");
            shipAmountsLabel.setText(rushDeliveryController.getRushShipAmounts() +" đồng");
        } catch (AddressNotSupportRushDeliveryException | NoRushMediaException e) {
            throw new RuntimeException(e);
        }
    }

    //no coupling
    //Functional cohesion
    //S principle
    public void initData() {
        mediaContainer.getChildren().addListener((ListChangeListener.Change<? extends Node> change) -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    loadData();
                }
            }
        });

        loadData();

        List<HashMap<Media,Integer>> mediaItems = rushDeliveryController.getMediaItems();

        List<Pane> paneList = new ArrayList<>();
        mediaItems.forEach(mediaMap->{
            try {
                FXMLLoader mediaLoader = new FXMLLoader(getClass().getResource("/fxml/rush_delivery_media.fxml"));
                Pane item = mediaLoader.load();
                RushDeliveryMediaItem media = mediaLoader.getController();
                media.setInfo(mediaMap);
                media.setItemContainer(mediaContainer);
                media.setController(rushDeliveryController);
                paneList.add(item);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        mediaContainer.getChildren().addAll(paneList);
    }
    //control coupling
    //no cohesion
    public void setOrder(Order order) {
        if (order != null) {
            rushDeliveryController.setOrder(order);
        } else {
            throw new IllegalArgumentException("Order cannot be null");
        }
    }
}
