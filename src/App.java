import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * App class, used to start the GUI.
 */
public class App extends Application {

	/**
	 * Starts the GUI.
	 * 
	 * @param stage the GUI stage. [Stage]
	 * @throws IOException if the file cannot be read.
	 */
	@Override
	public void start(Stage stage) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("./login.fxml"));

		Scene scene = new Scene(root);
		stage.setTitle("3rd Assignment");
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Main method.
	 * 
	 * @param args argvs, actually not required for our use.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}