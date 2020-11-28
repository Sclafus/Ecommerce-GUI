import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
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

public class login_controller {
	
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
		Boolean sent_alert = false;

		//socket stuff
		try{
			//client -> server
			Socket socket = new Socket("localhost", 4316);
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
			printWriter.println(mail);

			//server -> client
			BufferedReader bufferReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String response = bufferReader.readLine();
			System.out.println(response);
			socket.close();
		} catch(ConnectException e){
			sent_alert = true;
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Cannot connect to server");
			alert.setHeaderText("Server is unreachable. Try again later.");
			alert.showAndWait();
		}

		
		if((mail.length() == 0 || pass.length() == 0) && !sent_alert){
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("All fields must be filled");
			alert.setHeaderText("Please fill all the fields");
			alert.showAndWait();
		}

		//tmp stuff
		Boolean isAdmin = true;
		Boolean isEmployee = false;
		//~tmp stuff
		
		
		if (isAdmin){
			AnchorPane pane = FXMLLoader.load(getClass().getResource("./homepage_admin.fxml"));
			rootPane.getChildren().setAll(pane);
		} else if(isEmployee){
			AnchorPane pane = FXMLLoader.load(getClass().getResource("./homepage_employee.fxml"));
			rootPane.getChildren().setAll(pane);
		} else {
			AnchorPane pane = FXMLLoader.load(getClass().getResource("./homepage_user.fxml"));
			rootPane.getChildren().setAll(pane);
		}

	}
}
