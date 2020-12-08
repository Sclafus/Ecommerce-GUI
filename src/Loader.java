import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

/**
 * Helper class, used to pass data between FXML files.
 */
public class Loader {

	private AnchorPane rootPane;
	private User current_user;

	/**
	 * Loader constructor for helper class, used to pass data between FXML files.
	 * @param current_user the User that needs to be passed. [User]
	 * @param rootPane rootpane of the FXML scene. [AnchorPane]
	 */
	public Loader(User current_user, AnchorPane rootPane){
		this.rootPane = rootPane; 
		this.current_user = current_user;
	}

	/**
	 * Loads the {@code filename}, access the controller and calls 
	 * {@code initData} method. The controller needs to implement the
	 * {@code Controller} interface in order to be used properly.
	 * @param filename the FXML we want to access.
	 * @throws IOException if the {@filename} cannot be read.
	 * @see Controller
	 */
	public void load(String filename) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(filename + ".fxml"));
		AnchorPane parent = loader.load();
		
		Controller controller = loader.getController();
		controller.initData(this.current_user);
		this.rootPane.getChildren().setAll(parent);
	}
}
