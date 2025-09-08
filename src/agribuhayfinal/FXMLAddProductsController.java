/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package agribuhayfinal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author PC
 */
public class FXMLAddProductsController implements Initializable {

    @FXML
    private ComboBox<String> add_category;

    @FXML
    private ComboBox<String> add_status;

    @FXML
    private Spinner<Integer> add_stock;

    @FXML
    private Spinner<Double> add_price;

    @FXML
    private TextField add_productID;

    @FXML
    private TextField add_productName;
    
    @FXML
    private Button addProductbtn;
    
    @FXML
    private Button addCancelBtn;
    
    @FXML
    private RadioButton radio_tsp;

    @FXML
    private RadioButton radio_pkg;

    @FXML
    private Spinner<Double> add_TotSP;

    @FXML
    private Spinner<Double> add_ProdCost;
    
    public void setInventoryController(FXMLInventoryController inventoryController) {
        this.inventoryController = inventoryController;
    }
    
    private FXMLInventoryController inventoryController;
    
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    
    final double INITIAL_VALUE = 0.0;
    final int initial_value = 0;
    
    private static int productCounter = 1;
    
    private String generateProductId() {
        return String.format("PROD-%04d", productCounter++);
    }
    
    private int getLastProductCount() {
        int lastProductId = 0;
        String query = "SELECT MAX(SUBSTRING(productID, 6)) FROM productinfo";
        try {
            connect = database.connectDB();
            statement = connect.createStatement();
            result = statement.executeQuery(query);
            if (result.next()) {
                lastProductId = result.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lastProductId;
    }
    
    public void inventoryAddBtn() {
        if (add_category.getSelectionModel().getSelectedItem() == null 
                || add_status.getSelectionModel().getSelectedItem() == null 
                || add_stock.getValue() == null 
                || add_productID.getText().isEmpty()
                || add_productName.getText().isEmpty()
                || (!radio_tsp.isSelected() && !radio_pkg.isSelected())) {

            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields and select a pricing option.");
            alert.showAndWait();
            return;
        }

        String checkproductID = "SELECT productID FROM productinfo WHERE productID = '" 
                + add_productID.getText() + "'";

        connect = database.connectDB();

        try {
            statement = connect.createStatement();
            result = statement.executeQuery(checkproductID);

            if (result.next()) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText(add_productID.getText() + " is already taken");
                alert.showAndWait();
            } else {
                int stock = add_stock.getValue();
                double costPerKg = 0;
                double pricePerKg = 0;

                // Compute Cost per kg
                double totalProdCost = add_ProdCost.getValue();
                if (stock == 0) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Stock cannot be zero when calculating cost.");
                    alert.showAndWait();
                    return;
                }
                costPerKg = totalProdCost / stock;

                // Determine which price option was selected
                if (radio_tsp.isSelected()) {
                    double totalSellingPrice = add_TotSP.getValue();
                    pricePerKg = totalSellingPrice / stock;
                } else if (radio_pkg.isSelected()) {
                    pricePerKg = add_price.getValue();
                }

                String insertData = "INSERT INTO productinfo "
                        + "(productID, productName, productCategory, productStock, productCost, productPrice, productStatus) "
                        + "VALUES(?,?,?,?,?,?,?)";

                prepare = connect.prepareStatement(insertData);
                prepare.setString(1, add_productID.getText());
                prepare.setString(2, add_productName.getText());
                prepare.setString(3, add_category.getSelectionModel().getSelectedItem());
                prepare.setInt(4, stock);
                prepare.setDouble(5, costPerKg);  // productCost
                prepare.setDouble(6, pricePerKg); // productPrice
                prepare.setString(7, add_status.getSelectionModel().getSelectedItem());

                prepare.executeUpdate();

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Product successfully added to inventory!");
                alert.showAndWait();

                Stage stage = (Stage) addProductbtn.getScene().getWindow();
                stage.close();

                if (inventoryController != null) {
                    inventoryController.fruitsShowData();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private final String[] categoryList = {"Fruits", "Vegetables", "Grains"};

    public void addCategoryList() {
        List<String> categoryL = new ArrayList<>();

        for (String data : categoryList) {
            categoryL.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(categoryL);
        add_category.setItems(listData);
    }

    private final String[] statusList = {"Ok", "Low", "Out Of Stock"};

    public void addStatusList() {
        List<String> statusL = new ArrayList<>();

        for (String data : statusList) {
            statusL.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(statusL);
        add_status.setItems(listData);
    }
    
    public void handleAddCancel() {
        Stage stage = (Stage) addCancelBtn.getScene().getWindow();
        stage.close();
        
        if (inventoryController != null) {
            inventoryController.fruitsShowData();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productCounter = getLastProductCount() + 1;
        add_productID.setText(generateProductId());
        addCategoryList();
        addStatusList();
        
        // Spinner initializations
        add_price.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000.0, INITIAL_VALUE, 1.0));
        add_stock.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, initial_value, 1));
        add_ProdCost.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100000.0, INITIAL_VALUE, 1.0));
        add_TotSP.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100000.0, INITIAL_VALUE, 1.0));

        // Disable price spinners initially
        add_price.setDisable(true);
        add_TotSP.setDisable(true);

        // Toggle group (optional)
        ToggleGroup priceToggle = new ToggleGroup();
        radio_tsp.setToggleGroup(priceToggle);
        radio_pkg.setToggleGroup(priceToggle);

        // Add event listeners
        radio_tsp.setOnAction(event -> {
            add_TotSP.setDisable(false);
            add_price.setDisable(true);
            add_price.getValueFactory().setValue(0.0);
        });

        radio_pkg.setOnAction(event -> {
            add_price.setDisable(false);
            add_TotSP.setDisable(true);
            add_TotSP.getValueFactory().setValue(0.0);
        });
    }

}
