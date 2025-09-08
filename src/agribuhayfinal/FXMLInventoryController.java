/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package agribuhayfinal;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author PC
 */
public class FXMLInventoryController implements Initializable {

    private AnchorPane menuPane;
    
    @FXML
    private Button refreshBtn;

    @FXML
    private Button searchBtn;

    @FXML
    private TextField searchField;
    
     @FXML
    private ComboBox<String> filterCategory;
    
    @FXML
    private Button addProduct;
    
    @FXML
    private Button editProduct;
    
    @FXML
    private Button deleteBtn;

    @FXML
    private TableColumn<productData, String> col_category;

    @FXML
    private TableColumn<productData, String> col_cost;

    @FXML
    private TableColumn<productData, String> col_price;

    @FXML
    private TableColumn<productData, String> col_productID;

    @FXML
    private TableColumn<productData, String> col_productName;

    @FXML
    private TableColumn<productData, String> col_status;

    @FXML
    private TableColumn<productData, String> col_stock;

    @FXML
    private TableView<productData> fruit_tableView;

    @FXML
    private AnchorPane inventory;
    
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    public ObservableList<productData> fruitsDataList() {
        ObservableList<productData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM productinfo";

        connect = (Connection) database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            productData prodData;

            while (result.next()) {
                prodData = new productData(
                        result.getString("productID"),
                        result.getString("productName"),
                        result.getString("productCategory"),
                        result.getInt("productStock"),
                        result.getDouble("productCost"),
                        result.getDouble("productPrice"),
                        result.getString("productStatus"));

                listData.add(prodData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listData;
    }
    
    public ObservableList<productData> fruitsListData;
    public void fruitsShowData() {
        fruitsListData = fruitsDataList();
        
        col_productID.setCellValueFactory(new PropertyValueFactory<>("productID"));
        col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        col_category.setCellValueFactory(new PropertyValueFactory<>("productCategory"));
        col_stock.setCellValueFactory(new PropertyValueFactory<>("productStock"));
        col_cost.setCellValueFactory(new PropertyValueFactory<>("productCost"));
        col_price.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        col_status.setCellValueFactory(new PropertyValueFactory<>("productStatus"));
        
        filteredProductList = new FilteredList<>(fruitsListData, p -> true);
        fruit_tableView.setItems(filteredProductList);

    }

    @FXML
    void addProduct(ActionEvent event) {
        try {
            if (AppContext.mainPane != null) {
                Region overlay = new Region();
                overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
                overlay.setPrefSize(AppContext.mainPane.getWidth(), AppContext.mainPane.getHeight());
                AppContext.mainPane.getChildren().add(overlay);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLAddProducts.fxml"));
                Parent root = loader.load();
                
                FXMLAddProductsController addController = loader.getController();
                addController.setInventoryController(this);

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
    
    public void inventorySelectData() {
        productData selectedData = null;

        if (fruit_tableView.getSelectionModel().getSelectedItem() != null) {
            selectedData = fruit_tableView.getSelectionModel().getSelectedItem();
        }

        if (selectedData == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLEditProducts.fxml"));
            Parent root = loader.load();

            FXMLEditProductsController editController = loader.getController();
            editController.setInventoryController(this);
            editController.setProductData(selectedData);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    void editProduct(ActionEvent event) {
        productData selectedData = null;

        if (fruit_tableView.getSelectionModel().getSelectedItem() != null) {
            selectedData = fruit_tableView.getSelectionModel().getSelectedItem();
        }

        if (selectedData == null) return;

        try {
            if (AppContext.mainPane != null) {
                Region overlay = new Region();
                overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
                overlay.setPrefSize(AppContext.mainPane.getWidth(), AppContext.mainPane.getHeight());

                AppContext.mainPane.getChildren().add(overlay);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLEditProducts.fxml"));
                Parent root = loader.load();

                FXMLEditProductsController editController = loader.getController(); 

                if (editController != null) {
                    editController.setInventoryController(this);
                    editController.setProductData(selectedData);
                } else {
                    System.out.println("Controller not initialized properly.");
                }

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
    
    public void clearSelectedProduct() {
        fruit_tableView.getSelectionModel().clearSelection();
    }
    
    public void deleteProduct() {
        productData selectedData = null;

        if (fruit_tableView.getSelectionModel().getSelectedItem() != null) {
            selectedData = fruit_tableView.getSelectionModel().getSelectedItem();
        }

        if (selectedData == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText(null);
        confirm.setContentText("Are you sure you want to DELETE Product ID: " + selectedData.getProductID() + "?");

        Optional<ButtonType> option = confirm.showAndWait();

        if (option.isPresent() && option.get() == ButtonType.OK) {
            String deleteSQL = "DELETE FROM productinfo WHERE productID = ?";
            try (Connection connect = database.connectDB();
                 PreparedStatement statement = connect.prepareStatement(deleteSQL)) {

                statement.setString(1, selectedData.getProductID());
                statement.executeUpdate();

                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Success");
                success.setHeaderText(null);
                success.setContentText("Product successfully deleted!");
                success.showAndWait();
                
                fruitsShowData();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert cancelled = new Alert(Alert.AlertType.INFORMATION);
            cancelled.setTitle("Cancelled");
            cancelled.setHeaderText(null);
            cancelled.setContentText("Deletion cancelled.");
            cancelled.showAndWait();
        }
    }
    
    private FilteredList<productData> filteredProductList;
    
    private void handleSearch(ActionEvent event) {
        String searchTerm = searchField.getText().toLowerCase();

        filteredProductList.setPredicate(product -> {
            if (searchTerm == null || searchTerm.isEmpty()) {
                return true;
            }
            return product.getProductName().toLowerCase().contains(searchTerm);
        });
    }
    
    public void handleRefresh() {
        fruitsShowData();
        searchField.clear();
        filterCategory.setValue("All");
        filteredProductList.setPredicate(p -> true);
    }
    
    private final String[] filterList = {"Fruits", "Vegetables", "Grains"};

    public void addCategoryList() {
        List<String> categoryL = new ArrayList<>();

        categoryL.add("All");
        for (String data : filterList) {
            categoryL.add(data);
        }

        ObservableList<String> listData = FXCollections.observableArrayList(categoryL);
        filterCategory.setItems(listData);
    }
    
    public void applyFilters() {
        //String searchTerm = searchField.getText().toLowerCase();
        String selectedCategory = filterCategory.getValue();

        filteredProductList.setPredicate(product -> {
            boolean matchesCategory = selectedCategory == null || selectedCategory.equals("All") || product.getProductCategory().equalsIgnoreCase(selectedCategory);
            //boolean matchesSearch = searchTerm == null || searchTerm.isEmpty() || product.getProductName().toLowerCase().contains(searchTerm);
            return matchesCategory; // && matchesSearch;
        });
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fruitsShowData();
        addCategoryList();
        
        searchBtn.setOnAction(this::handleSearch);
        
        filterCategory.setValue("All");

        // searchBtn.setOnAction(e -> applyFilters());
        filterCategory.setOnAction(e -> applyFilters());

        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilters());

        fruit_tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            editProduct.setDisable(newSelection == null);
            deleteBtn.setDisable(newSelection == null);
        });

        refreshBtn.setOnAction(e -> handleRefresh());
    }
}
