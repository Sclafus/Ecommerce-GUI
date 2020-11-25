import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class registerController implements Initializable{
    
    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField name;
    
    @FXML
    private TextField surname;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private Text message;

    @Override
    public void initialize(URL url, ResourceBundle rb){

    }

    @FXML
    void newUser(ActionEvent event) {

        String nam = name.getText();
        String sur = surname.getText();
        String mail = email.getText();
        String pass = password.getText();
        String mail_regex = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$";
        Pattern pattern = Pattern.compile(mail_regex);

        if( nam.length() == 0|| sur.length() == 0 || mail.length() == 0 || pass.length() == 0){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("All fields must be filled");
            alert.setHeaderText("Please fill all the fields");
            alert.showAndWait();
        }

        //TODO: FIX REGEX FOR MAIL
        if(mail_regex.matches(mail)){
            System.out.println("good job");
        } else {
            System.out.println("lol");
        }

        //TODO: add new user to DB
        //TODO: login
        
    } 
}
