import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class ControllerHomepageEmployee implements Controller {

	private User current_user;

	@FXML
	private AnchorPane rootPane;

	@FXML
	private TreeView<String> treeView;

	@FXML
	private TextField searchID;
	
	@FXML
	private Text name;

	public void initData(User user){
		current_user = user;
		name.setText(user.getName());
	}

	@FXML
	void addWine(ActionEvent event) throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("./add_wine.fxml"));
		this.rootPane.getChildren().setAll(pane);
	}

	@FXML
	void display(ActionEvent event) {
		
	}

	@FXML
	void loadShop(ActionEvent event) throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("./homepage_user.fxml"));
		this.rootPane.getChildren().setAll(pane);
	}

	@FXML
	void restockWine(ActionEvent event) throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("./restock.fxml"));
		this.rootPane.getChildren().setAll(pane);
	}

	@FXML
	void shipOrder(ActionEvent event) {

	}
	
	@FXML
	void logout(ActionEvent event) throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("./login.fxml"));
		this.rootPane.getChildren().setAll(pane);
	}
}