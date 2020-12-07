import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class Homepage_admin_controller {
	private User current_user;
	
	@FXML
	private AnchorPane rootPane;

	@FXML
	private TreeView<String> treeView;

	@FXML
	private Text name;
	
	public void initData(User user){
		current_user = user;
		System.out.println(current_user.getEmail() + current_user.getPermission());
	}
	

	@FXML
	void addEmployee(ActionEvent event) throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("./add_employee.fxml"));
		rootPane.getChildren().setAll(pane);
	}

	// TODO use output
	@FXML
	void displayEmployees(ActionEvent event) throws UnknownHostException, IOException {
			Socket socket = new Socket("localhost", 4316);

			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(outputStream);
			String[] to_be_sent = {"get_employees"};
			out.writeObject(to_be_sent);

			InputStream inputStream = socket.getInputStream();
			ObjectInputStream in = new ObjectInputStream(inputStream);
			
			socket.close();
		}
	

	@FXML
	void displayOrders(ActionEvent event) {

	}

	@FXML
	void displayUsers(ActionEvent event) {

	}

	@FXML
	void displayWines(ActionEvent event) {

	}

	
	//TODO: Dynamic button to go back
	@FXML
	void loadEmployee(ActionEvent event) throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("./homepage_employee.fxml"));
		rootPane.getChildren().setAll(pane);
	}

	//TODO: Dynamic button to go back
	@FXML
	void loadShop(ActionEvent event) throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("./homepage_user.fxml"));
		rootPane.getChildren().setAll(pane);
	}

	@FXML
	void logout(ActionEvent event) throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("./login.fxml"));
		rootPane.getChildren().setAll(pane);
	}

}
