package agribuhayfinal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.beans.binding.StringBinding;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;

public class Screen55Controller implements Initializable {

    @FXML
    private Button addCustomer;

    @FXML
    private TableColumn<productData, String> customerID;

    @FXML
    private TableColumn<productData, String> customerName;

    @FXML
    private TableColumn<productData, String> costumerNo;

    @FXML
    private TableColumn<productData, String> currentAddress;

    @FXML
    private TableColumn<productData, String> deliveryAddress;

    @FXML
    private TableColumn<productData, String> customerType;

    @FXML
    private TableView<productData> customer_table;

    @FXML
    private Button delete_customer;

    @FXML
    private Button edit_customer;

    @FXML
    private AnchorPane inventory; // Your TableView

    @FXML
    private TextField searchCustomerField; // The search text field

    @FXML
    private Button searchCustomerBtn; // The search button
    
    @FXML
    private ComboBox<String> filterCustomer;
    
    @FXML
    private Button refreshCustomerBtn; 
    
    private Connection connect;
    private Statement statement;
    private ResultSet result;
    private PreparedStatement preparedStatement;

    // A filtered list for searching
    private FilteredList<productData> filteredCustomerList;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customerList = FXCollections.observableArrayList();
        
        customerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        costumerNo.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        currentAddress.setCellValueFactory(new PropertyValueFactory<>("currentAddress"));
        deliveryAddress.setCellValueFactory(new PropertyValueFactory<>("deliveryAddress"));
        customerType.setCellValueFactory(new PropertyValueFactory<>("customerType"));

        filteredCustomerList = new FilteredList<>(customerList, p -> true);
        customer_table.setItems(filteredCustomerList);

        searchCustomerBtn.setOnAction(this::handleSearch);
        
        customer_table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // A row is selected, enable edit and delete buttons
                edit_customer.setDisable(false);
                delete_customer.setDisable(false);
            } else {
                // No row selected, disable edit and delete buttons
                edit_customer.setDisable(true);
                delete_customer.setDisable(true);
            }
        });
        filterCustomer.setItems(FXCollections.observableArrayList("All", "Regular", "Senior Citizen", "PWD", "Student"));

        filterCustomer.valueProperty().addListener((observable, oldValue, newValue) -> handleFilterByCustomerType(newValue));
    }

    private void handleSearch(ActionEvent event) {
        String searchTerm = searchCustomerField.getText().toLowerCase();

        // Apply a filter that only allows customers whose names contain the search term
        filteredCustomerList.setPredicate(customer -> {
            if (searchTerm == null || searchTerm.isEmpty()) {
                return true; // Show all customers if the search term is empty
            }
            return customer.getCustomerName().toLowerCase().contains(searchTerm); // Filter based on name
        });
    }
    
    private void handleFilterByCustomerType(String customerType) {
        filteredCustomerList.setPredicate(customer -> {
            if (customerType == null || customerType.equals("All")) {
                return true; // Show all customers if "All" is selected
            }
            return customer.getCustomerType().equals(customerType); // Filter based on the customer type
        });
    }
    
    @FXML
    private void addCustomer(ActionEvent event) {
        try {
            if (AppContext.mainPane != null) {
                // Create the overlay
                Region overlay = new Region();
                overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);"); // Semi-transparent black with 50% opacity
                overlay.setPrefSize(AppContext.mainPane.getWidth(), AppContext.mainPane.getHeight());

                AppContext.mainPane.getChildren().add(overlay);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLAddCustomer.fxml"));
                Parent root = loader.load();

                // Get the controller for FXMLAddCustomer and set the Screen55Controller reference
                FXMLAddCustomerController addCustomerController = loader.getController();
                addCustomerController.setScreen55Controller(this); // Set reference to Screen55Controller
                addCustomerController.setCustomerList(this.customerList); // Pass the customerList to the controller

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);

                stage.setOnHidden(e -> {
                    AppContext.mainPane.getChildren().remove(overlay);
                });

                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void edit_customer(ActionEvent event) {
        productData selectedCustomer = customer_table.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            try {
                if (AppContext.mainPane != null) {
                    // Create the overlay
                    Region overlay = new Region();
                    overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);"); // Semi-transparent black with 50% opacity
                    overlay.setPrefSize(AppContext.mainPane.getWidth(), AppContext.mainPane.getHeight());

                    AppContext.mainPane.getChildren().add(overlay);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLEditCustomer.fxml"));
                    Parent root = loader.load();

                    // Get the controller for FXMLEditCustomer and set the Screen55Controller reference
                    FXMLEditCustomerController EditCustomerController = loader.getController();
                    EditCustomerController.setScreen55Controller(this); // Set reference to Screen55Controller

                    // Pass the selected customer to FXMLEditCustomerController
                    EditCustomerController.setCustomer(selectedCustomer); // Pass the selected customer for editing

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.APPLICATION_MODAL);

                    stage.setOnHidden(e -> {
                        AppContext.mainPane.getChildren().remove(overlay);
                    });

                    stage.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void delete_customer(ActionEvent event) {
        productData selectedCustomer = customer_table.getSelectionModel().getSelectedItem();
        if (selectedCustomer != null) {
            // Confirmation dialog
            Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to delete this customer?");
            alert.setHeaderText(null);
            alert.setTitle("Delete Confirmation");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    customerList.remove(selectedCustomer); // Remove the customer from the list
                    updateTableView(); // Refresh the table
                }
            });
        } else {
            // No customer selected, show a warning
            Alert alert = new Alert(AlertType.WARNING, "Please select a customer to delete.");
            alert.setTitle("No Customer Selected");
            alert.showAndWait();
        }
    }

    public ObservableList<productData> getCustomerList() {
        return customerList;
    }

    // The observable list of customers
    private ObservableList<productData> customerList;
    
    private ObservableList<productData> loadCustomerData() {
        ObservableList<productData> updatedCustomerList = FXCollections.observableArrayList();

        try (Connection connect = database.connectDB();
             Statement statement = connect.createStatement();
             ResultSet result = statement.executeQuery("SELECT * FROM customer_table")) {

            while (result.next()) {
                // Assuming you have a constructor that takes the necessary fields
                updatedCustomerList.add(new productData(
                    result.getString("customerID"),
                    result.getString("customerName"),
                    result.getString("costumerNo"),
                    result.getString("currentAddress"),
                    result.getString("deliveryAddress"),
                    result.getString("customerType")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return updatedCustomerList;
    }

    public void updateTableView() {
        // Reload the data from the database
        customerList.clear();
        customerList.addAll(loadCustomerData()); // Assuming loadCustomerData() fetches the updated data from the database

        // Refresh the table with the new data
        customer_table.refresh();
    }
}
