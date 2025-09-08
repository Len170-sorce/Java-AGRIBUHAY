package agribuhayfinal;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

public class FXMLAddCustomerController implements Initializable {
    
    @FXML
    private TextField customer_add;
    @FXML
    private TextField deliveryAdd;
    @FXML
    private CheckBox addCheckB;
    @FXML
    private TextField customer_id;
    @FXML
    private ComboBox<String> customerTypeCombo;
    @FXML
    private Button confirmCusto;
    @FXML
    private TextField customer_name;
    @FXML
    private TextField customer_contact;
    
    private Connection connect;
    private Statement statement;
    private ResultSet result;
    private PreparedStatement preparedStatement;
    
    @FXML
    private TableView<productData> customer_table; // Reference to your TableView
    private ObservableList<productData> customerList; // List to hold customer data
    
    private static int customerCounter = 1;

   private Screen55Controller screen55Controller;
    private ObservableList<productData> sharedCustomerList;

    // Set reference to Screen55Controller
    public void setScreen55Controller(Screen55Controller controller) {
      this.screen55Controller = controller;
      this.sharedCustomerList = controller.getCustomerList(); // Shared list
    }

    private String generateCustomerId() {
        return String.format("CUSTO-%04d", customerCounter++); // customer0001, customer0002, ...
    }

    public void setCustomerList(ObservableList<productData> customerList) {
        this.customerList = customerList;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customer_id.setText(generateCustomerId());
        
        addCheckB.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
            if (isNowSelected) {
                // Copy current address to delivery address
                deliveryAdd.setText(customer_add.getText());
            } else {
                // Clear the delivery address if unchecked (optional)
                deliveryAdd.clear();
            }
        });

        customer_add.textProperty().addListener((obs, oldVal, newVal) -> {
            if (addCheckB.isSelected()) {
                deliveryAdd.setText(newVal);
            }
        });

        customerTypeCombo.setItems(FXCollections.observableArrayList(
            "Regular",
            "Senior Citizen",
            "PWD",
            "Student"
        ));
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
        if (customer_name.getText().isEmpty() 
                || customer_contact.getText().isEmpty() 
                || customer_add.getText().isEmpty() 
                || deliveryAdd.getText().isEmpty() 
                || customer_id.getText().isEmpty()
                || customerTypeCombo.getValue() == null) {
            // Show error alert if any field is empty
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
        } else {
            String checkProductID = "SELECT customerID FROM customer_table WHERE customerID = '" 
                    + customer_id.getText() + "'";

            connect = database.connectDB();

            try {
                statement = connect.createStatement();
                result = statement.executeQuery(checkProductID);

                if (result.next()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(customer_id.getText() + " is already taken");
                    alert.showAndWait();
                } else {
                    String insertData = "INSERT INTO customer_table "
                            + "(customerID, customerName, costumerNo, currentAddress, deliveryAddress, customerType) "
                            + "VALUES(?,?,?,?,?,?)";

                    preparedStatement = connect.prepareStatement(insertData);

                    // Set parameters for the prepared statement
                    preparedStatement.setString(1, customer_id.getText());
                    preparedStatement.setString(2, customer_name.getText());
                    preparedStatement.setString(3, customer_contact.getText());
                    preparedStatement.setString(4, customer_add.getText());
                    preparedStatement.setString(5, deliveryAdd.getText());
                    preparedStatement.setString(6, customerTypeCombo.getSelectionModel().getSelectedItem());

                    // Execute the insert query
                    int rowsAffected = preparedStatement.executeUpdate();

                    // Check if data was added
                    if (rowsAffected > 0) {
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Customer Added");
                        alert.setHeaderText(null);
                        alert.setContentText("The customer data has been successfully saved to the database!");
                        alert.showAndWait();

                        // Reload the customer list and update the table
                        screen55Controller.updateTableView(); // Refresh table view
                    } else {
                        Alert alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Insertion Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Failed to add customer data to the database.");
                        alert.showAndWait();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Close the current window
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }
}