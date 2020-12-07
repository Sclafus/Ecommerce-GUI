import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;

public class ControllerNotifications {

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
    void remove(ActionEvent event) {

    }

}
