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

	private User current_user;

	@FXML
	private AnchorPane rootPane; // TODO Fix this

	@FXML
	private TextField id;

	@FXML
	private TextField quantity;

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

	//TODO javadoc
	@FXML
	@SuppressWarnings("unused")
	void restockWine(ActionEvent event) throws UnknownHostException, IOException {
		if (this.current_user.getPermission() > 1) {
			// user is authorized to perform the action

			try {
				int id_int = Integer.parseInt(id.getText());
				int quantity_int = Integer.parseInt(quantity.getText());

				// client -> server
				Socket socket = new Socket("localhost", 4316);
				OutputStream output_stream = socket.getOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(output_stream);
				String[] to_be_sent = { "restock_wine", id.getText(), quantity.getText() };
				out.writeObject(to_be_sent);

				// server -> client
				InputStream input_stream = socket.getInputStream();
				ObjectInputStream in = new ObjectInputStream(input_stream);
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
		Loader loader = new Loader(this.current_user, this.rootPane);
		loader.load("homepage_employee");
	}

}
