import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class Homepage_employee_controller {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TreeView<String> treeView;

    @FXML
    private TextField searchID;

    @FXML
    private Text employeeName;

    @FXML
    void addWine(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("./add_wine.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    void display(ActionEvent event) {
        
    }

    @FXML
    void loadShop(ActionEvent event) throws IOException{
        AnchorPane pane = FXMLLoader.load(getClass().getResource("./homepage_user.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    void restockWine(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("./restock.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    void shipOrder(ActionEvent event) {

    }
    
    @FXML
    void logout(ActionEvent event) throws IOException{
        AnchorPane pane = FXMLLoader.load(getClass().getResource("./login.fxml"));
        rootPane.getChildren().setAll(pane);

    }
}