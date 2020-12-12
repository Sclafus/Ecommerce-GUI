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

/**
 * Controller for Login, page accessible by {@code User} with permission > 0
 * (aka everyone)
 */
public class ControllerLogin {

	private User currentUser;

	@FXML
	private AnchorPane rootPane;

	@FXML
	private TextField email;

	@FXML
	private PasswordField password;

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
		String mailRegex = "\\w+@\\w+\\.\\w+";
		Pattern mailValidator = Pattern.compile(mailRegex);
		Matcher mailMatcher = mailValidator.matcher(mail);
		return mailMatcher.matches();
	}

	/**
	 * Goes to the register page.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws IOException if the file can't be accessed.
	 */
	@FXML
	public void loadRegister(ActionEvent event) throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("./register.fxml"));
		rootPane.getChildren().setAll(pane);
	}

	/**
	 * Login with the data provided by the user. Server responds with the correct
	 * {@code User} (permission>=1) else it will respond with {@code nullUser}
	 * (permission=0)
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws IOException if an I/O error occurs when creating the socket.
	 * @see User
	 */
	@FXML
	public void login(ActionEvent event) throws IOException {
		// gets the informations
		String mail = email.getText();
		String pass = password.getText();

		try {
			Socket socket = new Socket("localhost", 4316);
			// checks if the email and the password have been inserted
			if (mail.length() > 0 && pass.length() > 0) {
				// checks if the email is valid
				if (isMail(mail)) {
					// if the email is valid the client receives the object User from the server
					// client -> server
					OutputStream outputStream = socket.getOutputStream();
					ObjectOutputStream out = new ObjectOutputStream(outputStream);
					String[] toBeSent = { "login", mail, pass };
					out.writeObject(toBeSent);

					// server -> client
					InputStream inputStream = socket.getInputStream();
					ObjectInputStream in = new ObjectInputStream(inputStream);
					User user = (User) in.readObject();

					int permission = user.getPermission();
					this.currentUser = user;
					Loader loader = new Loader(this.currentUser, this.rootPane);

					// different cases are distinguished based on the User's permission
					switch (permission) {
						// permission = 1 ->it's a user and the user's homepage is loaded
						case 1:
							loader.load("homepage_user");
							break;

						// permission = 2 ->it's an employee and the employee's homepage is loaded
						case 2:
							loader.load("homepage_employee");
							break;

						// permission = 3 ->it's an admin and the admin's homepage is loaded
						case 3:
							loader.load("homepage_admin");
							break;

						default:
							// notifies the user if a wrong email or password are inserted
							Alert alert = new Alert(AlertType.WARNING);
							alert.setTitle("Wrong login");
							alert.setHeaderText("Email or password are wrong. Please retry.");
							alert.showAndWait();
							password.clear();
							break;
					}
					socket.close();
				} else {
					// notifies the user if the email is not valid
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Email not valid");
					alert.setHeaderText("The provided email is not valid, please retry.");
					alert.showAndWait();
				}
			} else {
				// notifies if some fields are not filled
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("All fields must be filled");
				alert.setHeaderText("Please fill all the fields");
				alert.showAndWait();
			}
		} catch (ConnectException e) {
			// notifies if the server can not be reached
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Cannot connect to server");
			alert.setHeaderText("Server is unreachable. Try again later.");
			alert.showAndWait();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Opens the user's homepage once somebody clicks on the "Continue as Guest"
	 * button in the
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws IOException if an I/O error occurs when creating the socket.
	 * @see User
	 */
	@FXML
	public void guestLogin(ActionEvent event) throws IOException {
		try {
			// client -> server
			Socket socket = new Socket("localhost", 4316);
			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(outputStream);
			String[] toBeSent = { "guest" };
			out.writeObject(toBeSent);

			// server -> client
			InputStream inputStream = socket.getInputStream();
			ObjectInputStream in = new ObjectInputStream(inputStream);
			User user = (User) in.readObject();
			this.currentUser = user;

			Loader loader = new Loader(this.currentUser, this.rootPane);
			loader.load("homepage_user");
			socket.close();
		} catch (ConnectException e) {
			// notifies if the server can not be reached
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Cannot connect to server");
			alert.setHeaderText("Server is unreachable. Try again later.");
			alert.showAndWait();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
}
