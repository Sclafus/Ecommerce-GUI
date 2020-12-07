import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

public class ControllerAddEmployee  implements Controller {

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
	
//TODO CHECK IF JAVADOC IS NECESSARY
	public void initData(User user){
		current_user = user;
		System.out.println(current_user.getEmail() + current_user.getPermission());
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

	@FXML
	void addEmployee(ActionEvent event) throws UnknownHostException, IOException {

		String nam = name.getText();
		String sur = surname.getText();
		String mail = email.getText();
		String pass = password.getText();

		if (nam.length() == 0 || sur.length() == 0 || mail.length() == 0 || pass.length() == 0) {

			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("All fields must be filled");
			alert.setHeaderText("Please fill all the fields.");
			alert.showAndWait();

		} else if (!isMail(mail)) {

			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Email not valid");
			alert.setHeaderText("The provided email is not valid, please retry.");
			alert.showAndWait();

		} else {

			Socket socket = new Socket("localhost", 4316);

			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(outputStream);
			String[] to_be_sent = {"register", nam, sur, mail, pass};
			out.writeObject(to_be_sent);

			InputStream inputStream = socket.getInputStream();
			ObjectInputStream in = new ObjectInputStream(inputStream);

			try {
				int permission = (int) in.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			socket.close();

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("New Employee added");
			alert.setHeaderText("The new employee has been registered.");
			alert.showAndWait();

			AnchorPane pane = FXMLLoader.load(getClass().getResource("./homepage_admin.fxml"));
			rootPane.getChildren().setAll(pane);

		}

	}

	@FXML
	void back(ActionEvent event) throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("./homepage_admin.fxml"));
		rootPane.getChildren().setAll(pane);
	}
}