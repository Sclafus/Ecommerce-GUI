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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

//TODO javadoc
public class ControllerAddWine implements Controller {

	private User current_user;

	@FXML
	private AnchorPane rootPane;

	@FXML
	private TextField name;

	@FXML
	private TextField year;

	@FXML
	private TextField producer;

	@FXML
	private TextField grapes;

	@FXML
	private TextArea notes;

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

	/**
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws UnknownHostException if the IP address of the host could not be
	 *                              determined.
	 * @throws IOException          if an I/O error occurs when creating the socket.
	 */
	@FXML
	@SuppressWarnings("unused")
	public void addWine(ActionEvent event) throws UnknownHostException, IOException {
		int yea = 0;
		String nam = name.getText();
		String yea_tmp = year.getText();
		String pro = producer.getText();
		String gra = grapes.getText();
		String not = notes.getText();

		if (nam.length() == 0 || yea_tmp.length() == 0 || pro.length() == 0 || gra.length() == 0 || not.length() == 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("All fields must be filled");
			alert.setHeaderText("Please fill all the fields");
			alert.showAndWait();
		} else {
			try {
				yea = Integer.parseInt(yea_tmp);
			} catch (NumberFormatException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Invalid Year");
				alert.setHeaderText("Please insert a valid year.");
				alert.showAndWait();
			} finally {
				if (this.current_user.getPermission() > 1) {
					// user is authorized to perform the action
					Socket socket = new Socket("localhost", 4316);

					OutputStream outputStream = socket.getOutputStream();
					ObjectOutputStream out = new ObjectOutputStream(outputStream);
					String[] to_be_sent = { "add_wine", nam, yea_tmp, pro, gra, not };
					out.writeObject(to_be_sent);

					InputStream inputStream = socket.getInputStream();
					ObjectInputStream in = new ObjectInputStream(inputStream);

					try {
						Wine new_wine = (Wine) in.readObject();
						Alert alert = new Alert(AlertType.INFORMATION);
						if (new_wine.getProductId() != -1) {

							alert.setTitle("Wine added!");
							alert.setHeaderText(
									String.format("Wine %s (%d) by %s has been added.\nID: %d", new_wine.getName(),
											new_wine.getYear(), new_wine.getProducer(), new_wine.getProductId()));
						} else {
							alert.setTitle("Wine already inserted");
							alert.setHeaderText("Wine is already present in the database.");
						}
						alert.showAndWait();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}

					socket.close();
				} else {
					// user is not authorized to perform the action
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Not authorized");
					alert.setHeaderText("You are not allowed to perform this action.");
					alert.showAndWait();
				}
			}

		}
	}

	/**
	 * Goes back to the employee homepage.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws IOException if the filename cannot be read.
	 */
	@FXML
	public void back(ActionEvent event) throws IOException {
		Loader loader = new Loader(this.current_user, this.rootPane);
		loader.load("homepage_employee");
	}

}
