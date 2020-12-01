import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class Homepage_admin_controller {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TreeView<String> treeView;

    @FXML
    private Text name;

    @FXML
    void addEmployee(ActionEvent event) throws IOException{
        AnchorPane pane = FXMLLoader.load(getClass().getResource("./add_employee.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    void displayEmployees(ActionEvent event) {

    }

    @FXML
    void displayOrders(ActionEvent event) {

    }

    @FXML
    void displayUsers(ActionEvent event) {

    }

    @FXML
    void displayWines(ActionEvent event) {

    }

    @FXML
    void loadEmployee(ActionEvent event) throws IOException{
        AnchorPane pane = FXMLLoader.load(getClass().getResource("./homepage_employee.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    void loadShop(ActionEvent event) throws IOException{
        AnchorPane pane = FXMLLoader.load(getClass().getResource("./homepage_user.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    void logout(ActionEvent event) throws IOException{
        AnchorPane pane = FXMLLoader.load(getClass().getResource("./login.fxml"));
        rootPane.getChildren().setAll(pane);
    }

}
