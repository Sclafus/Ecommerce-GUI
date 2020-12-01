import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.AnchorPane;

public class Restock_controller {

	@FXML
	private AnchorPane rootPane;

	@FXML
	private MenuButton wine;

	@FXML
	void addWine(ActionEvent event) {

	}

	@FXML
	void back(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("./homepage_employee.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    void setText(ActionEvent event) {

    }

}

