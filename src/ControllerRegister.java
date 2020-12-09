
// import java.io.IOException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
/**
 * Controller for Register, page accessible by {@code User} 
 * with permission > 0 (aka everyone)
 */
public class ControllerRegister implements Controller {

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

	@FXML
	private Text message;

	/**
	 * Initialize {@code this.current_user} with the passed value. This method is
	 * made to be called from another controller, using the {@code load} method in
	 * {@code Loader} class.
	 * 
	 * @param user the {@code User} we want to pass. [User]
	 * @see Loader
	 */
	public void initData(User user){
		this.current_user = user;
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
	void register(ActionEvent event) throws UnknownHostException, IOException, ClassNotFoundException {

		String nam = name.getText();
		String sur = surname.getText();
		String mail = email.getText();
		String pass = password.getText();

		if (nam.length() == 0 || sur.length() == 0 || mail.length() == 0 || pass.length() == 0) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("All fields must be filled");
			alert.setHeaderText("Please fill all the fields");
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
			String[] to_be_sent = {"register_user", nam, sur, mail, pass};
			out.writeObject(to_be_sent);

			InputStream inputStream = socket.getInputStream();
			ObjectInputStream in = new ObjectInputStream(inputStream);
			
			this.current_user = (User) in.readObject();
			
			Loader loader = new Loader(this.current_user, this.rootPane);
			loader.load("homepage_user");
			socket.close();
		}

	}
}