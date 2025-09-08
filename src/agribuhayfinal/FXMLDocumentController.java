/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package agribuhayfinal;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

/**
 *
 * @author Reiniel
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private LineChart lineChart;
    
    
    
    @Override 
    public void initialize (URL url, ResourceBundle rb) {
        
        
        //lineChart.setTitle("Graph");
        
        XYChart.Series series =  new XYChart.Series();
        series.setName("Sales");
        
        series.getData().add(new XYChart.Data("Monday", 1000));
        series.getData().add(new XYChart.Data("Tuesday", 1500));
        series.getData().add(new XYChart.Data("Wedenesday", 2000));
        series.getData().add(new XYChart.Data("Thursday", 2500));
        series.getData().add(new XYChart.Data("Friday", 1500));
        series.getData().add(new XYChart.Data("Saturday", 4500));       
        series.getData().add(new XYChart.Data("Sunday", 5000));        
                
        lineChart.getData().add(series);
    }
    
  
}
