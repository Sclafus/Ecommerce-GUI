//TODO JAVADOC DONE
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
//TODO class javadoc
public class ControllerLogin {
	
	private User current_user;

	@FXML
	private AnchorPane rootPane;

	@FXML
	private TextField email;

	@FXML
	private PasswordField password;

	/**
	 * Goes to the register page.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws IOException if the file can't be accessed.
	 */
	@FXML
	private void loadRegister(ActionEvent event) throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("./register.fxml"));
		rootPane.getChildren().setAll(pane);
	}

	/**
	 * Checks if the provided String is an email or not. This method uses RegEx.
	 * 
	 * @param mail the mail when need to check. [String]
	 * @return true if the string is an email, else false. [Boolean]
	 * 
	 * @see java.util.regex.Pattern
	 * @see java.util.regex.Matcher
	 */
	public Boolean isMail(String mail) {
		String mail_regex = "\\w+@\\w+\\.\\w+";
		Pattern mail_validator = Pattern.compile(mail_regex);
		Matcher mail_matcher = mail_validator.matcher(mail);
		return mail_matcher.matches();
	}

	/**
	 * Login with the data provided by the user. Server responds with the correct 
	 * {@code User} (permission>=1) else it will respond with {@code nullUser} (permission=0)
	 * @param event GUI event. [ActionEvent]
	 * @throws IOException if an I/O error occurs when creating the socket.
	 * @see User
	 */
	@FXML
	private void login(ActionEvent event) throws IOException {
		// gets the informations
		String mail = email.getText();
		String pass = password.getText();

		// socket stuff
		try {

			// client -> server
			Socket socket = new Socket("localhost", 4316);
			if (mail.length() > 0 && pass.length() > 0) {

				if (isMail(mail)) {

					OutputStream outputStream = socket.getOutputStream();
					ObjectOutputStream out = new ObjectOutputStream(outputStream);
					String[] to_be_sent = {"login", mail, pass};
					out.writeObject(to_be_sent);

					// server -> client
					InputStream inputStream = socket.getInputStream();
					ObjectInputStream in = new ObjectInputStream(inputStream);
					User user = (User) in.readObject();
					int permission = user.getPermission();
					this.current_user = user;
					Loader loader = new Loader(current_user, rootPane);
					switch (permission) {
						case 1:
							loader.load("homepage_user");
							break;

						case 2:
							loader.load("homepage_employee");
							break;

						case 3:
							loader.load("homepage_admin");
							break;

						default:
							//notifies the user if a wrong email or password are inserted
							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("Wrong login");
							alert.setHeaderText("Email or password are wrong. Please retry.");
							alert.showAndWait();
							password.clear();
							break;
					}

					socket.close();
				} else {
					//notifies the user if the email is not valid
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Email not valid");
					alert.setHeaderText("The provided email is not valid, please retry.");
					alert.showAndWait();
				}
			} else {
				//notifies if some fields are not filled
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("All fields must be filled");
				alert.setHeaderText("Please fill all the fields");
				alert.showAndWait();
			}

		} catch (ConnectException e) {
			//notifies if the server can not be reached
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Cannot connect to server");
			alert.setHeaderText("Server is unreachable. Try again later.");
			alert.showAndWait();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
}

