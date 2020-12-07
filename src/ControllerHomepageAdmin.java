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
		String[] to_be_sent = { "get_employees" };
		out.writeObject(to_be_sent);

		InputStream inputStream = socket.getInputStream();
		ObjectInputStream in = new ObjectInputStream(inputStream);

		try {
			ArrayList<User> employees = (ArrayList<User>) in.readObject();
			TreeItem rootItem = new TreeItem("Employees");

			for (User employee : employees) {
				TreeItem<String> rootEmployee = new TreeItem<String>(employee.getEmail());
				TreeItem<String> name = new TreeItem<String>("Name: " + employee.getName());
				TreeItem<String> surname = new TreeItem<String>("Surname: " + employee.getSurname());
				rootEmployee.getChildren().addAll(name, surname);
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
	 * 
	 * @param event button event on the GUI.
	 * @throws IOException if the client can't open a connection with the server.
	 * @see Order
	 */
	@FXML

	// TODO FIX CONNECTION
	void displayOrders(ActionEvent event) throws IOException {
		Socket socket = new Socket("localhost", 4316);

		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(outputStream);
		String[] to_be_sent = { "get_orders" };
		out.writeObject(to_be_sent);

		InputStream inputStream = socket.getInputStream();
		ObjectInputStream in = new ObjectInputStream(inputStream);

		try {
			ArrayList<Order> orders = (ArrayList<Order>) in.readObject();
			TreeItem rootItem = new TreeItem("Orders");

			for (Order order : orders) {
				TreeItem<String> rootOrder = new TreeItem<String>(Integer.toString(order.getId()));
				TreeItem<String> id = new TreeItem<String>("Order ID: " + order.getId());
				TreeItem<String> status = new TreeItem<String>("Status: " + order.getStatus());
				TreeItem<String> customer = new TreeItem<String>("Customer: " + order.getCustomer());

				for (Wine wine : order.getWines()) {
					TreeItem<String> rootProduct = new TreeItem<String>(
							String.format("%d - %s %s", wine.getProductId(), wine.getName(), wine.getYear()));
					TreeItem<String> quantity = new TreeItem<String>("Quantity: " + wine.getQuantity());
					rootProduct.getChildren().add(quantity);
					rootOrder.getChildren().add(rootProduct);
				}

				rootOrder.getChildren().addAll(id, status, customer);
				rootItem.getChildren().add(rootOrder);
			}
			rootItem.setExpanded(true);
			treeView.setRoot(rootItem);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		socket.close();
	}

	/**
	 * It displays all the users.
	 * 
	 * @param event button event on the GUI.
	 * @throws UnknownHostException
	 * @throws IOException          if the client can't open a connection with the
	 *                              server.
	 * @see User
	 */
	@FXML

	// TODO FIX CONNECTION

	void displayUsers(ActionEvent event) throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", 4316);

		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(outputStream);
		String[] to_be_sent = { "get_users" };
		out.writeObject(to_be_sent);

		InputStream inputStream = socket.getInputStream();
		ObjectInputStream in = new ObjectInputStream(inputStream);

		try {
			ArrayList<User> users = (ArrayList<User>) in.readObject();
			TreeItem rootItem = new TreeItem("Users");

			for (User user : users) {
				TreeItem<String> rootUser = new TreeItem<String>(user.getEmail());
				TreeItem<String> name = new TreeItem<String>("Name: " + user.getName());
				TreeItem<String> surname = new TreeItem<String>("Surname: " + user.getSurname());
				rootUser.getChildren().addAll(name, surname);
				rootItem.getChildren().add(rootUser);
			}
			rootItem.setExpanded(true);
			treeView.setRoot(rootItem);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		socket.close();
	}

	/**
	 * It displays all the wines.
	 * 
	 * @param event button event on the GUI.
	 * @throws UnknownHostException
	 * @throws IOException          if the client can't open a connection with the
	 *                              server.
	 * @see Wine
	 */
	@FXML

	// TODO FIX CONNECTION

	void displayWines(ActionEvent event) throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", 4316);

		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(outputStream);
		String[] to_be_sent = { "get_wines" };
		out.writeObject(to_be_sent);

		InputStream inputStream = socket.getInputStream();
		ObjectInputStream in = new ObjectInputStream(inputStream);

		try {
			ArrayList<Wine> wines = (ArrayList<Wine>) in.readObject();
			TreeItem rootItem = new TreeItem("Wines");

			for (Wine wine : wines) {
				TreeItem<String> rootWine = new TreeItem<String>(wine.getName());
				TreeItem<String> product_id = new TreeItem<String>("Product ID: " + wine.getProductId());
				TreeItem<String> producer = new TreeItem<String>("Producer: " + wine.getProducer());
				TreeItem<String> year = new TreeItem<String>("Year: " + wine.getYear());
				TreeItem<String> grapes = new TreeItem<String>("Grapewines: " + wine.getGrapewines());
				TreeItem<String> notes = new TreeItem<String>("Notes: " + wine.getNotes());
				TreeItem<String> quantity = new TreeItem<String>("Quantity: " + wine.getQuantity());

				rootWine.getChildren().addAll(product_id, producer, year, grapes, notes, quantity);
				rootItem.getChildren().add(rootWine);
			}
			rootItem.setExpanded(true);
			treeView.setRoot(rootItem);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		socket.close();

	}

	// TODO: Dynamic button to go back
	@FXML
	void loadEmployee(ActionEvent event) throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("./homepage_employee.fxml"));
		rootPane.getChildren().setAll(pane);
	}

	// TODO: Dynamic button to go back
	@FXML
	void loadShop(ActionEvent event) throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("./homepage_user.fxml"));
		rootPane.getChildren().setAll(pane);
	}

	@FXML
	void logout(ActionEvent event) throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("./login.fxml"));
		rootPane.getChildren().setAll(pane);
	}

}
