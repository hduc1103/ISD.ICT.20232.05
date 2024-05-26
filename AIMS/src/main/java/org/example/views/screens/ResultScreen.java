package org.example.views.screens;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.models.Transaction;

import java.io.IOException;

public class ResultScreen {

    public Button backButton;
    public Label resultLabel;
    public Label contentLabel;
    // no coupling
    //no cohesiiono
    public void backToHome(ActionEvent e) throws IOException {
        Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        FXMLLoader homeLoader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
        stage.setScene(new Scene(homeLoader.load()));
    }

    // control coupling
    //Coincidental
    public void setResult(Transaction transaction) {
        if (transaction.getAmount() == -1) {
            resultLabel.setText("Giao dịch thất bại");
        } else {
            resultLabel.setText("Giao dịch thành công");
        }
        contentLabel.setText(transaction.getContent());
    }
}
