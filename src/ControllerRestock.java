import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

/**
 * Controller for Restock, page accessible by {@code User} with permission > 1
 * (aka employees and administrators)
 */
public class ControllerRestock implements Controller {

	private User currentUser;

	@FXML
	private AnchorPane rootPane;

	@FXML
	private TextField id;

	@FXML
	private TextField quantity;

	/**
	 * Initialize {@code this.currentUser} with the passed value. This method is
	 * made to be called from another controller, using the {@code load} method in
	 * {@code Loader} class.
	 * 
	 * @param user the {@code User} we want to pass. [User]
	 * @see Loader
	 */
	public void initData(User user) {
		this.currentUser = user;
	}

	//TODO javadoc
	@FXML
	@SuppressWarnings("unused")
	void restockWine(ActionEvent event) throws UnknownHostException, IOException {
		if (this.currentUser.getPermission() > 1) {
			// user is authorized to perform the action
			try {
				int idInt = Integer.parseInt(id.getText());
				int quantityInt = Integer.parseInt(quantity.getText());

				// client -> server
				Socket socket = new Socket("localhost", 4316);
				OutputStream outputStream = socket.getOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(outputStream);
				String[] toBeSent = { "restock_wine", id.getText(), quantity.getText() };
				out.writeObject(toBeSent);

				// server -> client
				InputStream inputStream = socket.getInputStream();
				ObjectInputStream in = new ObjectInputStream(inputStream);
				Boolean restocked = (Boolean) in.readObject();

				if (restocked) {
					// success
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Success");
					alert.setHeaderText("Wine has been restocked successfully.");
					alert.showAndWait();
				} else {
					// failure
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Failed");
					alert.setHeaderText("Something went wrong. Are you sure the ID is correct?");
					alert.showAndWait();
				}
				socket.close();
			} catch (NumberFormatException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Not authorized");
				alert.setHeaderText("You are not allowed to perform this action.");
				alert.showAndWait();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			// user is not authorized to perform the action
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Not authorized");
			alert.setHeaderText("You are not allowed to perform this action.");
			alert.showAndWait();
		}
	}
	
	//TODO javadoc
	@FXML
	void back(ActionEvent event) throws IOException {
		Loader loader = new Loader(this.currentUser, this.rootPane);
		loader.load("homepage_employee");
	}

}
