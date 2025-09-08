// Rename your custom class to avoid the conflict
package agribuhayfinal;
import javafx.scene.layout.Pane;
import java.net.URL;
import javafx.fxml.FXMLLoader; // Import the actual JavaFX FXMLLoader

public class ViewLoader {
    
   public Pane getPage(String fileName) {
        try {
            URL fileUrl = AGRIBUHAYFINAL.class.getResource("/agribuhayfinal/" + fileName + ".fxml");
            if (fileUrl == null) {
                System.out.println("File URL is null for: " + fileName);
                System.out.println("Absolute path attempt: " + AGRIBUHAYFINAL.class.getResource("").toString() + fileName + ".fxml");
                throw new java.io.FileNotFoundException("FXML file can't be found for " + fileName);
            }
            
            System.out.println("Loading FXML from: " + fileUrl.toString());
            
            // Use the proper JavaFX FXMLLoader with better error handling
            FXMLLoader loader = new FXMLLoader(fileUrl);
            return loader.load();
        } catch (Exception e) {
            System.out.println("Error loading " + fileName + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}