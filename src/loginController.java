import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

public class loginController implements Initializable {
    
    @FXML
    private AnchorPane rootPane;

    @Override
    public void initialize(URL url, ResourceBundle rb){

    }

    @FXML
    private void loadRegister(ActionEvent event) throws IOException{
        AnchorPane pane = FXMLLoader.load(getClass().getResource("./register.fxml"));
        rootPane.getChildren().setAll(pane);
    }
}
