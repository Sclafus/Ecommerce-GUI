import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class Homepage_user_controller {
    
    @FXML
    private AnchorPane rootPane;

    @FXML
    private Text name1;

    @FXML
    private Text year1;

    @FXML
    private Text notes1;
    
    @FXML
    private Text name2;

    @FXML
    private Text year2;

    @FXML
    private Text notes2;

    @FXML
    private Text name3;

    @FXML
    private Text year3;

    @FXML
    private Text notes3;

    @FXML
    private Text name4;

    @FXML
    private Text year4;

    @FXML
    private Text notes4;

    @FXML
    private Text name5;

    @FXML
    private Text year5;

    @FXML
    private Text notes5;

    @FXML
    private Text name6;

    @FXML
    private Text year6;

    @FXML
    private Text notes6;


    @FXML
    void logout(ActionEvent event) throws IOException{
        AnchorPane pane = FXMLLoader.load(getClass().getResource("./login.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    void nextPage(ActionEvent event) {

    }

    @FXML
    void previousPage(ActionEvent event) {

    }

    @FXML
    void search(ActionEvent event) {

    }

    @FXML
    void showCart(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("./cart.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    void showNotifications(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("./notifications.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    void showOrders(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("./orders.fxml"));
        rootPane.getChildren().setAll(pane);
    }

}
