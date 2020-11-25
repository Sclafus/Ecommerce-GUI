import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import java.util.regex.Pattern;
import java.io.IOException;
import java.util.regex.Matcher;

public class add_employeeController {

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
    void addEmployee(ActionEvent event) throws IOException{

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
            flag = false;
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Email not valid");
            alert.setHeaderText("The provided email is not valid, please retry.");
            alert.showAndWait();
        }

        if(flag) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("New Employee added");
            alert.setHeaderText("The new employee has been registered.");
            alert.showAndWait();
            AnchorPane pane = FXMLLoader.load(getClass().getResource("./homepage_admin.fxml"));
            rootPane.getChildren().setAll(pane);
        }
		
        //TODO: add new employee to DB

    }
    
    @FXML
    void back(ActionEvent event) throws IOException{
        AnchorPane pane = FXMLLoader.load(getClass().getResource("./homepage_admin.fxml"));
        rootPane.getChildren().setAll(pane);
    }
}