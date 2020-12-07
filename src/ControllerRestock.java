import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.AnchorPane;

public class ControllerRestock implements Controller {

	private User current_user;

	@FXML
	private AnchorPane rootPane;

	@FXML
	private MenuButton wine;

	
	public void initData(User user) {
		this.current_user = user;
	}

	@FXML
	void addWine(ActionEvent event) {
		//TODO
	}

	@FXML
	void back(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("./homepage_employee.fxml"));
        rootPane.getChildren().setAll(pane);
    }

    @FXML
    void setText(ActionEvent event) {
		//TODO FIGURE OUT WHAT THIS IS
    }

}

