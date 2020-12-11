import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

/**
 * Helper class, used to pass data between FXML files.
 */
public class Loader {

	private AnchorPane rootPane;
	private User currentUser;

	/**
	 * Loader constructor for helper class, used to pass data between FXML files.
	 * 
	 * @param currentUser the User that needs to be passed. [User]
	 * @param rootPane     rootpane of the FXML scene. [AnchorPane]
	 */
	public Loader(User currentUser, AnchorPane rootPane) {
		this.rootPane = rootPane;
		this.currentUser = currentUser;
	}

	/**
	 * Loads the {@code filename}, access the controller and calls {@code initData}
	 * method. The controller needs to implement the {@code Controller} interface in
	 * order to be used properly.
	 * 
	 * @param filename the FXML we want to access.
	 * @throws IOException if the {@code filename} cannot be read.
	 * @see Controller
	 */
	public void load(String filename) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(filename + ".fxml"));
		AnchorPane parent = loader.load();

		Controller controller = loader.getController();
		controller.initData(this.currentUser);
		this.rootPane.getChildren().setAll(parent);
	}
}
