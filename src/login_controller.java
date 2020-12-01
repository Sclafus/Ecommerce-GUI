import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class Login_controller {
	
	@FXML
	private AnchorPane rootPane;

	@FXML
	private TextField email;

	@FXML
	private PasswordField password;


	@FXML
	private void loadRegister(ActionEvent event) throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("./register.fxml"));
		rootPane.getChildren().setAll(pane);
	}

	@FXML
	private void login(ActionEvent event) throws IOException{
		//gets the informations
		String mail = email.getText();
		String pass = password.getText();

		//socket stuff
		try{

			//client -> server
			Socket socket = new Socket("localhost", 4316);
			if(mail.length() > 0 && pass.length() > 0){
				OutputStream outputStream = socket.getOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(outputStream);
				String[] to_be_sent = {"login", mail, pass};
				out.writeObject(to_be_sent);

				//server -> client
				InputStream inputStream = socket.getInputStream();
				ObjectInputStream in = new ObjectInputStream(inputStream);
				int permission = (int) in.readObject();

				switch (permission) {
					case 1:
						AnchorPane pane1 = FXMLLoader.load(getClass().getResource("./homepage_user.fxml"));
						rootPane.getChildren().setAll(pane1);
						break;
					case 2:
						AnchorPane pane2 = FXMLLoader.load(getClass().getResource("./homepage_employee.fxml"));
						rootPane.getChildren().setAll(pane2);
						break;
					case 3:
						AnchorPane pane3 = FXMLLoader.load(getClass().getResource("./homepage_admin.fxml"));
						rootPane.getChildren().setAll(pane3);
						break;
					default:
						AnchorPane pane_default = FXMLLoader.load(getClass().getResource("./homepage_user.fxml"));
						rootPane.getChildren().setAll(pane_default);
						break;
				}

				socket.close();
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("All fields must be filled");
				alert.setHeaderText("Please fill all the fields");
				alert.showAndWait();
			}


		} catch(ConnectException e){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Cannot connect to server");
			alert.setHeaderText("Server is unreachable. Try again later.");
			alert.showAndWait();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
}
