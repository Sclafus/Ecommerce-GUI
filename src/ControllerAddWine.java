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

/**
 * Controller for Add Wine, page accessible by {@code User} with permission > 1
 * (aka employees and administrators)
 */
public class ControllerAddWine implements Controller {

	private User currentUser;

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
		String yeaTmp = year.getText();
		String pro = producer.getText();
		String gra = grapes.getText();
		String not = notes.getText();

		if (nam.length() == 0 || yeaTmp.length() == 0 || pro.length() == 0 || gra.length() == 0 || not.length() == 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("All fields must be filled");
			alert.setHeaderText("Please fill all the fields");
			alert.showAndWait();
		} else {
			try {
				yea = Integer.parseInt(yeaTmp);
			} catch (NumberFormatException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Invalid Year");
				alert.setHeaderText("Please insert a valid year.");
				alert.showAndWait();
			} finally {
				if (this.currentUser.getPermission() > 1) {
					// user is authorized to perform the action
					Socket socket = new Socket("localhost", 4316);

					// client -> server
					OutputStream outputStream = socket.getOutputStream();
					ObjectOutputStream out = new ObjectOutputStream(outputStream);
					String[] toBeSent = { "add_wine", nam, yeaTmp, pro, gra, not };
					out.writeObject(toBeSent);

					// server -> client
					InputStream inputStream = socket.getInputStream();
					ObjectInputStream in = new ObjectInputStream(inputStream);

					try {
						Wine newWine = (Wine) in.readObject();
						Alert alert = new Alert(AlertType.INFORMATION);
						if (newWine.getProductId() != -1) {
							alert.setTitle("Wine added!");
							alert.setHeaderText(
									String.format("Wine %s (%d) by %s has been added.\nID: %d", newWine.getName(),
											newWine.getYear(), newWine.getProducer(), newWine.getProductId()));
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
	 * @throws IOException if the file can't be accessed.
	 */
	@FXML
	public void back(ActionEvent event) throws IOException {
		Loader loader = new Loader(this.currentUser, this.rootPane);
		loader.load("homepage_employee");
	}

}
