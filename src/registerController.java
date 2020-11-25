
// import java.io.IOException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
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

    /**
     * Checks if the provided String is an email or not. This method
     * uses RegEx. 
     * 
     * @param mail the mail when need to check. [String]
     * @return true if the string is an email, else false. [Boolean]
     * 
     * @see java.util.regex.Pattern
     * @see java.util.regex.Matcher
     */
    public Boolean isMail(String mail){
        
        String mail_regex = "\\w+@\\w+\\.\\w+";
        Pattern mail_validator = Pattern.compile(mail_regex);
        Matcher mail_matcher = mail_validator.matcher(mail);
        return mail_matcher.matches();
        
    }

    @FXML
    void newUser(ActionEvent event) throws IOException {

        String nam = name.getText();
        String sur = surname.getText();
        String mail = email.getText();
        String pass = password.getText();

        //The flag value is used to not prompt 2 alerts at the same time.
        Boolean flag = true;

        if(nam.length() == 0 || sur.length() == 0 || mail.length() == 0 || pass.length() == 0){
            flag = false;
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("All fields must be filled");
            alert.setHeaderText("Please fill all the fields");
            alert.showAndWait();
        }

        if(!isMail(mail) && flag){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Email not valid");
            alert.setHeaderText("The provided email is not valid, please retry.");
            alert.showAndWait();
        }

        //TODO: add new user to DB

        //TODO: login
        Boolean login_flag=true;

        if(login_flag){
            AnchorPane pane = FXMLLoader.load(getClass().getResource("./homepage_user.fxml"));
            rootPane.getChildren().setAll(pane);
        }
    }
}