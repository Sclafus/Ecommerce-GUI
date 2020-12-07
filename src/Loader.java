import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class Loader {

	private AnchorPane rootPane;
	private User current_user;

	public Loader(User current_user, AnchorPane rootPane){
		this.rootPane = rootPane; 
		this.current_user = current_user;
	}

	public void load(String filename) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource(filename + ".fxml"));
		AnchorPane parent = loader.load();
		
		Controller controller = loader.getController();
		controller.initData(current_user);
		this.rootPane.getChildren().setAll(parent);
	}
}
