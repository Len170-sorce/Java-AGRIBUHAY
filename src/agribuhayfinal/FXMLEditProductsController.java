/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package agribuhayfinal;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
public class FXMLEditProductsController implements Initializable {

    @FXML
    private Button editCancelBtn;

    @FXML
    private Button editProductBtn;

    @FXML
    private ComboBox<String> edit_category;

    @FXML
    private TextField edit_productID;

    @FXML
    private TextField edit_productName;

    @FXML
    private ComboBox<String> edit_status;

    @FXML
    private Spinner<Integer> edit_stock;
    
    @FXML
    private RadioButton edit_radio_tsp, edit_radio_pkg;

    @FXML
    private Spinner<Double> edit_ProdCost, edit_TotSP, edit_Price;

    private ToggleGroup editPriceGroup = new ToggleGroup();
    
    private productData data;
    
    public void setInventoryController(FXMLInventoryController inventoryController) {
        this.inventoryController = inventoryController;
    }
    
    private FXMLInventoryController inventoryController;
    
    public void setProductData(productData data) {
        this.data = data;

        edit_productID.setText(data.getProductID());
        edit_productName.setText(data.getProductName());
        edit_category.getSelectionModel().select(data.getProductCategory());
        edit_status.getSelectionModel().select(data.getProductStatus());
        edit_ProdCost.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000.0, data.getProductCost(), 1.0));
        edit_stock.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, data.getProductStock(), 1));

        // Auto-derive cost and price logic
        double existingPrice = data.getProductPrice();
        double stock = data.getProductStock();

        edit_ProdCost.getValueFactory().setValue(data.getProductCost());

        if (stock > 0) {
            // If price aligns with total selling price
            edit_TotSP.getValueFactory().setValue(existingPrice * stock);
            edit_radio_tsp.setSelected(true);
            edit_TotSP.setDisable(false);
            edit_Price.setDisable(true);
        } else {
            edit_Price.getValueFactory().setValue(existingPrice);
            edit_radio_pkg.setSelected(true);
            edit_TotSP.setDisable(true);
            edit_Price.setDisable(false);
        }
    }
    
    public void editEdit() {
        if (edit_category.getSelectionModel().getSelectedItem() == null 
                || edit_status.getSelectionModel().getSelectedItem() == null 
                || edit_ProdCost.getValue() == null 
                || edit_stock.getValue() == null 
                || edit_Price.getValue() == null
                || edit_productID.getText().isEmpty()
                || edit_productName.getText().isEmpty()) {
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            String editData = "UPDATE productinfo SET "
                    + "productName = ?, "
                    + "productCategory = ?, "
                    + "productStock = ?, "
                    + "productCost = ?, "
                    + "productPrice = ?, "
                    + "productStatus = ? "
                    + "WHERE productID = ?"; 
            
            Connection connect = database.connectDB();
            
            try {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to EDIT Product ID: " + edit_productID.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();
                
                if (option.get().equals(ButtonType.OK)) {
                    PreparedStatement prepare = connect.prepareStatement(editData);
                    
                    prepare.setString(1, edit_productName.getText());
                    prepare.setString(2, edit_category.getSelectionModel().getSelectedItem());
                    prepare.setInt(3, edit_stock.getValue());
                    double totalProdCost = edit_ProdCost.getValue();
                        int stock = edit_stock.getValue();
                        double costPerItem = stock > 0 ? totalProdCost / stock : 0.0;
                        prepare.setDouble(4, costPerItem);

                        double computedPrice;
                        if (edit_radio_tsp.isSelected()) {
                            double totalSP = edit_TotSP.getValue();
                            computedPrice = stock > 0 ? totalSP / stock : 0.0;
                        } else {
                            computedPrice = edit_Price.getValue();
                        }
                        prepare.setDouble(5, computedPrice);
                    prepare.setString(6, edit_status.getSelectionModel().getSelectedItem());
                    prepare.setString(7, edit_productID.getText());
                    
                    prepare.executeUpdate();
                    
                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Edited!");
                    alert.showAndWait();
                    
                    Stage stage = (Stage) editProductBtn.getScene().getWindow();
                    stage.close();
                    
                    if (inventoryController != null) {
                        inventoryController.fruitsShowData();  
                    } 
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Cancelled.");
                    alert.showAndWait();
                    
                    Stage stage = (Stage) editProductBtn.getScene().getWindow();
                    stage.close();
                } 
                
            } catch (Exception e) {e.printStackTrace();}
        }
    }
    
    public void editCancel() {
        if (inventoryController != null) {
            inventoryController.clearSelectedProduct();
        }
        
        Stage stage = (Stage) editCancelBtn.getScene().getWindow();
        stage.close();
    }
    
    private final String[] categoryList = {"Fruits", "Vegetables", "Grains"};

    private final String[] statusList = {"Ok", "Low", "Out Of Stock"};

    public void editCategoryList() {
        ObservableList<String> categoryL = FXCollections.observableArrayList(categoryList);
        edit_category.setItems(categoryL);
    }

    public void editStatusList() {
        ObservableList<String> statusL = FXCollections.observableArrayList(statusList);
        edit_status.setItems(statusL);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("FXMLEditProductsController initialized.");
        editCategoryList();
        editStatusList();
        setupRadioButtons();
        setupSpinners();
    }

    private void setupRadioButtons() {
        edit_radio_tsp.setToggleGroup(editPriceGroup);
        edit_radio_pkg.setToggleGroup(editPriceGroup);

        // Default disable both price spinners
        edit_TotSP.setDisable(true);
        edit_Price.setDisable(true);

        // Listeners for enabling correct field
        edit_radio_tsp.setOnAction(e -> {
            edit_TotSP.setDisable(false);
            edit_Price.setDisable(true);
        });

        edit_radio_pkg.setOnAction(e -> {
            edit_TotSP.setDisable(true);
            edit_Price.setDisable(false);
        });
    }
    
    private void setupSpinners() {
        edit_ProdCost.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100000.0, 0.0, 1.0));
        edit_TotSP.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100000.0, 0.0, 1.0));
        edit_Price.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000.0, 0.0, 1.0));
    }
    
}
