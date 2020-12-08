import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

//TODO javadoc
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
		name.setText(this.current_user.getName());
	}

	/**
	 * Goes to page for adding new {@code Wine}s.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws IOException if the file can't be accessed.
	 */
	@FXML
	void loadAddWine(ActionEvent event) throws IOException {
		Loader loader = new Loader(this.current_user, this.rootPane);
		loader.load("add_wine");
	}

	@FXML
	void display(ActionEvent event) {
		// TODO
	}

	@FXML
	void loadShop(ActionEvent event) throws IOException {
		Loader loader = new Loader(this.current_user, this.rootPane);
		loader.load("homepage_user");
	}

	@FXML
	void loadRestockWine(ActionEvent event) throws IOException {
		Loader loader = new Loader(this.current_user, this.rootPane);
		loader.load("restock");
	}

	@FXML
	void shipOrder(ActionEvent event) {

	}

	/**
	 * Goes back to the login page.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws IOException if the filename cannot be read.
	 */
	@FXML
	void logout(ActionEvent event) throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("./login.fxml"));
		this.rootPane.getChildren().setAll(pane);
	}
}