import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ControllerHomepageAdmin implements Controller {

	private User current_user;

	@FXML
	private AnchorPane rootPane;

	@FXML
	private TreeView<String> treeView;

	@FXML
	private Text name;

	public void initData(User user) {
		this.current_user = user;
		this.name.setText(this.current_user.getName());
	}

	/**
	 * loads add_employee.fxml 
	 * Adds a new employee to the database:
	 * 
	 * @param event button event on the GUI.
	 * @throws IOException if the client can't open the file.
	 */
	@FXML
	void addEmployee(ActionEvent event) throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("./add_employee.fxml"));
		rootPane.getChildren().setAll(pane);
	}

	/**
	 * It displays all the employees.
	 * 
	 * @param event button event on the GUI.
	 * @throws IOException if the client can't open a connection with the server.
	 * @see User
	 */
	// TODO Fix javadoc
	@FXML
	void displayEmployees(ActionEvent event) throws IOException {
		Socket socket = new Socket("localhost", 4316);

		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(outputStream);
		String[] to_be_sent = {"get_employees"};
		out.writeObject(to_be_sent);

		InputStream inputStream = socket.getInputStream();
		ObjectInputStream in = new ObjectInputStream(inputStream);

		try {
			ArrayList<User> employees = (ArrayList<User>) in.readObject();
			TreeItem rootItem = new TreeItem("Employees");

			for (User employee : employees){
				TreeItem<String> rootEmployee = new TreeItem<String> (employee.getEmail());
				TreeItem<String> name = new TreeItem<String> ("Name: " + employee.getName());
				TreeItem<String> surname = new TreeItem<String> ("Surname: " + employee.getSurname());
				rootEmployee.getChildren().addAll(name,surname);
				rootItem.getChildren().add(rootEmployee);
			}
			rootItem.setExpanded(true);
			treeView.setRoot(rootItem);


		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
			socket.close();
		}
	
	/**
	 * It displays all the orders.
	 * @param event button event on the GUI.
	 * @throws IOException if the client can't open a connection with the server.
	 * @see Order
	 */
	@FXML
	void displayOrders(ActionEvent event) {

	}

	/**
	 * It displays all the users.
	 * @param event button event on the GUI.
	 * @throws IOException if the client can't open a connection with the server.
	 * @see User
	 */
	@FXML
	void displayUsers(ActionEvent event) {

	}

	/**
	 * It displays all the wines.
	 * @param event button event on the GUI.
	 * @throws IOException if the client can't open a connection with the server.
	 * @see Wine
	 */
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
