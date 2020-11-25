import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class loginController implements Initializable {
    
    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @Override
    public void initialize(URL url, ResourceBundle rb){

    }

    @FXML
    private void loadRegister(ActionEvent event) throws IOException{
        AnchorPane pane = FXMLLoader.load(getClass().getResource("./register.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    private void login(ActionEvent event) throws IOException{
        String mail = email.getText();
        String pass = password.getText();
        //TODO: DB query

        //tmp stuff
        Boolean isAdmin = false;
        Boolean isEmployee = false;

        if(mail.length() == 0 || pass.length() == 0){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("All fields must be filled");
            alert.setHeaderText("Please fill all the fields");
            alert.showAndWait();
        }
        //~tmp stuff


        if (isAdmin){
            AnchorPane pane = FXMLLoader.load(getClass().getResource("./homepage_admin.fxml"));
            rootPane.getChildren().setAll(pane);
        } else if(isEmployee){
            AnchorPane pane = FXMLLoader.load(getClass().getResource("./homepage_employee.fxml"));
            rootPane.getChildren().setAll(pane);
        } else {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("./homepage_user.fxml"));
            rootPane.getChildren().setAll(pane);
        }

    }
}
