import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;


/**
 * Controller for Cart, page accessible by {@code User} 
 * with permission > 0 (aka everyone)
 */
public class ControllerCart implements Controller {

	private User current_user;

	@FXML
	private AnchorPane rootPane;

	@FXML
	private TreeView<String> treeview;

	/**
	 * Initialize {@code this.current_user} with the passed value. This method is
	 * made to be called from another controller, using the {@code load} method in
	 * {@code Loader} class.
	 * 
	 * @param user the {@code User} we want to pass. [User]
	 * @see Loader
	 */
	public void initData(User user) {
		this.current_user = user;
	}

	@FXML
	void back(ActionEvent event) throws IOException {
		Loader loader = new Loader(this.current_user, this.rootPane);
		loader.load("homepage_user");
	}

	@FXML
	void buy(ActionEvent event) {
		// TODO
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Order submitted!");
		alert.setHeaderText("Your order has been placed!");
		alert.showAndWait();
	}

}
