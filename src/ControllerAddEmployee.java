import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import java.util.regex.Pattern;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;

/**
 * Controller for Add Employee, page accessible by {@code User} 
 * with permission > 2 (aka administrators)
 */
public class ControllerAddEmployee implements Controller {

	private User current_user;

	@FXML
	private AnchorPane rootPane;

	@FXML
	private TextField name;

	@FXML
	private TextField surname;

	@FXML
	private TextField email;

	@FXML
	private PasswordField password;

	/**
	 * Initialize {@code this.current_user} with the passed value.
	 * This method is made to be called from another controller,
	 * using the {@code load} method in {@code Loader} class.
	 * @param user the {@code User} we want to pass. [User]
	 * @see Loader
	 */
	public void initData(User user) {
		this.current_user = user;
	}

	/**
	 * Checks if the provided String is an email or not. This method uses RegEx.
	 * 
	 * @param mail the mail when need to check. [String]
	 * @return {@code true} if the string is an email, else {@code false}. [Boolean]
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
	 * Adds new employee to the ecommerce.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws UnknownHostException if the IP address of the host could not be
	 *                              determined.
	 * @throws IOException          if an I/O error occurs when creating the socket.
	 * @see User
	 */
	@FXML
	void addEmployee(ActionEvent event) throws UnknownHostException, IOException {
		// gets data
		String nam = name.getText();
		String sur = surname.getText();
		String mail = email.getText();
		String pass = password.getText();

		if (nam.length() == 0 || sur.length() == 0 || mail.length() == 0 || pass.length() == 0) {

			// all fields are required
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("All fields must be filled");
			alert.setHeaderText("Please fill all the fields.");
			alert.showAndWait();

		} else if (!isMail(mail)) {

			// email is not valid
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Email not valid");
			alert.setHeaderText("The provided email is not valid, please retry.");
			alert.showAndWait();

		} else {
			// inserted data is ok

			if (this.current_user.getPermission() > 2) {
				// user is autorized to perform the action
				Socket socket = new Socket("localhost", 4316);

				// client -> server
				OutputStream outputStream = socket.getOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(outputStream);
				String[] to_be_sent = { "register", nam, sur, mail, pass };
				out.writeObject(to_be_sent);

				// server -> client
				InputStream inputStream = socket.getInputStream();
				ObjectInputStream in = new ObjectInputStream(inputStream);

				try {
					User new_employee = (User) in.readObject();
					int permission = new_employee.getPermission();

					if (permission < 1) {
						// permission < 1 = nullUser => user is already registered.
						Alert alert = new Alert(AlertType.WARNING);
						alert.setTitle("Already registered");
						alert.setHeaderText("There is already an account with this email.");
						alert.showAndWait();

					} else {

						// employee registered successfully
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Success");
						alert.setHeaderText("Employee registered successfully.");
						alert.showAndWait();

					}
					socket.close();

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

			Loader loader = new Loader(this.current_user, this.rootPane);
			loader.load("homepage_admin");
		}

	}

	/**
	 * Goes back to the administrator homepage.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws IOException if the file can't be accessed.
	 */
	@FXML
	void back(ActionEvent event) throws IOException {
		Loader loader = new Loader(this.current_user, this.rootPane);
		loader.load("homepage_admin");
	}
}