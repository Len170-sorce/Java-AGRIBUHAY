/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package agribuhayfinal;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Region;


/**
 * FXML Controller class
 *
 * @author Reiniel
 */
public class FXMLCustomerOrderController implements Initializable {

    @FXML
    private Button buttonCustLayout;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
private void goToCustLayout(ActionEvent event) {
    try {
        if (AppContext.mainPane != null) {

            // Create an overlay effect
            Region overlay = new Region();
            overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
            overlay.setPrefSize(AppContext.mainPane.getWidth(), AppContext.mainPane.getHeight());

            // Add overlay to the main pane
            AppContext.mainPane.getChildren().add(overlay);

            // Load the Create Order window
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLCreateOrder.fxml"));
            Parent root = loader.load();

            // Get the controller of the Create Order window
            FXMLCreateOrderController receiptController = loader.getController();

            // You can pass data to receiptController if needed, for example:
            // receiptController.setData(productName, quantity); // Call your methods to send data

            // Create and show the new window as a modal
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // This makes it modal (blocks interaction with the main window)

            // Remove the overlay when the modal is closed
            stage.setOnHidden(e -> {
                AppContext.mainPane.getChildren().remove(overlay);
            });

            // Show the new window
            stage.show();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}



}
