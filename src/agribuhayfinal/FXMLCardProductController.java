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
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author PC
 */
public class FXMLCardProductController implements Initializable {

    @FXML
    private AnchorPane cardForm;
    @FXML
    private Spinner<Integer> prod_spinner;
    @FXML
    private Button prod_addBtn;
    @FXML
    private Label prod_name;
    @FXML
    private Label prod_price;
    
    private productData prodData;
    
    

    public void setData(productData prodData) {
        this.prodData = prodData;
        
        prod_name.setText(prodData.getProductName());
        prod_price.setText(String.valueOf(prodData.getProductPrice()));
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SpinnerValueFactory<Integer> valueFactory = //
        new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1); 
        // min = 0, max = 100, initial = 1

    prod_spinner.setValueFactory(valueFactory);
        
        
    }    
    
}
