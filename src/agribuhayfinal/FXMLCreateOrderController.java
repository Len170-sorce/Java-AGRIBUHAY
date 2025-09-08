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
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * @author Reiniel
 */
public class FXMLCreateOrderController implements Initializable {

    @FXML
    private Button cancelButton;

    @FXML
    private GridPane menu_gridPane;
    
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    
    
    @FXML
    public void handleCancel(ActionEvent event) {

    Stage stage = (Stage) cancelButton.getScene().getWindow();
    stage.close();

}


    private ObservableList<productData> cardListData = FXCollections.observableArrayList();
    
    public ObservableList<productData> menuGetData() {
        String sql = "SELECT * FROM productinfo";
        
        ObservableList<productData> listData = FXCollections.observableArrayList();
        
        connect = database.connectDB();
        
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            
            productData prod;
            
            while (result.next()) {
                prod = new productData(
                        result.getString("productID"),
                        result.getString("productName"),
                        result.getDouble("productPrice"));

                listData.add(prod);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return listData;
    }
    
    public void menuDisplayCard() {
        if (cardListData == null) {
            cardListData = FXCollections.observableArrayList();
        } else {
            cardListData.clear();
        }

        cardListData.addAll(menuGetData());

        int row = 0;
        int column = 0;
        
        menu_gridPane.getRowConstraints().clear();
        menu_gridPane.getColumnConstraints().clear();
        menu_gridPane.getChildren().clear();
        
        for (int q = 0; q < cardListData.size(); q++) {
            
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("FXMLCardProduct.fxml"));
                AnchorPane pane = load.load();
                
                FXMLCardProductController cardC = load.getController();
                cardC.setData(cardListData.get(q));
                
                if (column == 4) {
                    column = 0;
                    row += 1;
                }
                
                menu_gridPane.add(pane, column++, row);
                
            } catch (Exception e) {e.printStackTrace();}
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        menuDisplayCard();
    } 
}
    

