import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class Cart_controller {

    @FXML
	private AnchorPane rootPane;
	
    @FXML
    private TreeView<String> treeview;

    @FXML
    void back(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("./homepage_user.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    void buy(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Order submitted!");
		alert.setHeaderText("Your order has been placed!");
		alert.showAndWait();
    }

}
