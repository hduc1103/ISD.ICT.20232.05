package org.example.subsystem.VNPaySubsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.util.concurrent.CompletableFuture;

public class VNPayView {
    /**
     * This class have only 1 responsibility for view the VNPay Screen
     */
    @FXML
    private WebView vnpayWebView;

    @FXML
    public void handleBackButton(ActionEvent e) {
        Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
        stage.fireEvent(new WindowEvent(stage,WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    public WebView getWebView() {
        return vnpayWebView;
    }
    public VNPayView() {
        this.vnpayWebView = new WebView();
    }

    public String query(String url) {
        try {
            CompletableFuture<String> responseUrlFuture = new CompletableFuture<>();

            FXMLLoader newWindowLoader = new FXMLLoader(getClass().getResource("/fxml/vnpay_payment.fxml"));
            Parent newWindowRoot = newWindowLoader.load();
            VNPayView vnPayView = newWindowLoader.getController();
            // Functional cohesion
            vnPayView.getWebView().getEngine().load(url);


            Stage newWindowStage = new Stage();
            newWindowStage.initModality(Modality.APPLICATION_MODAL);
            newWindowStage.setTitle("VNPay Payment");
            newWindowStage.setScene(new Scene(newWindowRoot));

            newWindowStage.setOnCloseRequest(event -> {
                // Functional cohesion
                String responseUrl = vnPayView.getWebView().getEngine().getLocation();
                responseUrlFuture.complete(responseUrl);
            });
            newWindowStage.showAndWait();
            return responseUrlFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
