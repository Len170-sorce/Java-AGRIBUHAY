/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package agribuhayfinal;

import java.net.URL;
import javafx.fxml.Initializable;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
        

/**
 * FXML Controller class
 *
 * @author Reiniel
 */
public class FXMLMenuController implements Initializable {

    @FXML
    private BorderPane mainPane;
    
    @FXML
    private AnchorPane menuPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ViewLoader object = new ViewLoader();
        Pane view = object.getPage("FXMLHome");
        if (view != null) {
            mainPane.setCenter(view);
        }
        
        AppContext.mainPane = menuPane;
    }
    
    @FXML
    private void handleButton1Action(ActionEvent event) {
        System.out.println("You clicked me!");
        ViewLoader object = new ViewLoader();
        Pane view = object.getPage("FXMLHome");
        if (view != null) {
            mainPane.setCenter(view);
        }
    }

    @FXML
    private void handleButton2Action(ActionEvent event) {
        System.out.println("You clicked me!");
        ViewLoader object = new ViewLoader();
        Pane view = object.getPage("FXMLDocument");
        if (view != null) {
            mainPane.setCenter(view);
        }
    }
    
    @FXML
    private void handleButton3Action(ActionEvent event) {
        System.out.println("You clicked me!");
        ViewLoader object = new ViewLoader();
        Pane view = object.getPage("FXMLInventory");
        if (view != null) {
            mainPane.setCenter(view);
        }
    }
    
    @FXML
    private void handleButton4Action(ActionEvent event) {
        System.out.println("You clicked me!");
        ViewLoader object = new ViewLoader();
        Pane view = object.getPage("FXMLCustomerOrder");
        if (view != null) {
            mainPane.setCenter(view);
        }
    }
    
    @FXML
    private void handleButton5Action(ActionEvent event) {
        System.out.println("You clicked me!");
        ViewLoader object = new ViewLoader();
        Pane view = object.getPage("Screen55");
        if (view != null) {
            mainPane.setCenter(view);
        }
    }   
    
}
