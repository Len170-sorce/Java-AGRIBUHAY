package agribuhayfinal;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * FXML Controller class
 *
 * @author Reiniel
 */
public class FXMLEditCustomerController implements Initializable {

    @FXML
    private Button confirmCusto;
    @FXML
    private TextField customer_id;
    @FXML
    private TextField customer_name;
    @FXML
    private TextField customer_add;
    @FXML
    private TextField deliveryAdd;
    @FXML
    private ComboBox<String> customerTypeCombo;
    @FXML
    private CheckBox addCheckB;
    @FXML
    private TextField customer_contact;

    private Screen55Controller screen55Controller;
    private productData currentCustomer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize customer types if needed, example:
        customerTypeCombo.getItems().addAll("Regular", "Senior Citizen", "PWD", "Student"); // Add customer types
    }

    // This method will be used to set the reference to Screen55Controller
    public void setScreen55Controller(Screen55Controller controller) {
        this.screen55Controller = controller;
    }

    // Method to set the selected customer for editing
    public void setCustomer(productData selectedCustomer) {
        this.currentCustomer = selectedCustomer;

        // Set the fields with the current customer's data
        customer_id.setText(selectedCustomer.getCustomerID());
        customer_name.setText(selectedCustomer.getCustomerName());
        customer_add.setText(selectedCustomer.getCurrentAddress());
        deliveryAdd.setText(selectedCustomer.getDeliveryAddress());
        customer_contact.setText(selectedCustomer.getContactNo());

        // Set the customer type, for example:
        customerTypeCombo.setValue(selectedCustomer.getCustomerType()); // Assuming it's a string in the model
    }

    @FXML
    private void cancelNewCustomer(ActionEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void confirmCusto(ActionEvent event) {
        if (currentCustomer != null) {
            // Here, update the customer data with the new values
            currentCustomer.setCustomerName(customer_name.getText());
            currentCustomer.setCurrentAddress(customer_add.getText());
            currentCustomer.setDeliveryAddress(deliveryAdd.getText());
            currentCustomer.setContactNo(customer_contact.getText());
            currentCustomer.setCustomerType(customerTypeCombo.getValue());

            // Database update logic
            try {
                // Assuming a method to get the DB connection
                Connection connect = database.connectDB();
                
                // SQL update query
                String updateQuery = "UPDATE customer_table SET customerName = ?, contactNo = ?, "
                        + "currentAddress = ?, deliveryAddress = ?, customerType = ? WHERE customerID = ?";

                PreparedStatement prepare = connect.prepareStatement(updateQuery);
                prepare.setString(1, customer_name.getText());
                prepare.setString(2, customer_contact.getText());
                prepare.setString(3, customer_add.getText());
                prepare.setString(4, deliveryAdd.getText());
                prepare.setString(5, customerTypeCombo.getValue());
                prepare.setString(6, customer_id.getText());

                int rowsAffected = prepare.executeUpdate();

                // Check if the update was successful
                if (rowsAffected > 0) {
                    // Optionally, refresh the table in Screen55Controller
                    screen55Controller.updateTableView();

                    // Show success message
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Customer Updated");
                    alert.setHeaderText(null);
                    alert.setContentText("The customer data has been successfully updated!");
                    alert.showAndWait();
                } else {
                    // Show failure message
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Update Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to update customer data.");
                    alert.showAndWait();
                }
            } catch (Exception e) {e.printStackTrace();}

            // Close the edit window
            cancelNewCustomer(event);
        }
    }
}
