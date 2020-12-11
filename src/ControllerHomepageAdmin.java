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
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * Controller for the Admin Homepage.
 */
public class ControllerHomepageAdmin implements Controller {

	private User currentUser;

	@FXML
	private AnchorPane rootPane;

	@FXML
	private TreeView<String> treeView;

	@FXML
	private Text name;

	/**
	 * Initialize {@code this.currentUser} with the passed value. This method is
	 * made to be called from another controller, using the {@code load} method in
	 * {@code Loader} class.
	 * 
	 * @param user the {@code User} we want to pass. [User]
	 * @see Loader
	 */
	public void initData(User user) {
		this.currentUser = user;
		this.name.setText(this.currentUser.getName());
	}

	/**
	 * Displays all the employees in the TreeView.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws UnknownHostException if the IP address of the host could not be
	 *                              determined.
	 * @throws IOException          if an I/O error occurs when creating the socket.
	 * @see User
	 */
	@FXML
	@SuppressWarnings("unchecked")
	void displayEmployees(ActionEvent event) throws UnknownHostException, IOException {
		if (this.currentUser.getPermission() > 2) {

			// user is authorized to perform the action
			Socket socket = new Socket("localhost", 4316);

			// client -> server
			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(outputStream);
			String[] toBeSent = { "get_employees" };
			out.writeObject(toBeSent);

			// server -> client
			InputStream inputStream = socket.getInputStream();
			ObjectInputStream in = new ObjectInputStream(inputStream);

			try {
				ArrayList<User> employees = (ArrayList<User>) in.readObject();
				TreeItem<String> rootItem = new TreeItem<String>("Employees");

				for (User employee : employees) {
					TreeItem<String> rootEmployee = new TreeItem<String>(employee.getEmail());
					TreeItem<String> name = new TreeItem<String>("Name: " + employee.getName());
					TreeItem<String> surname = new TreeItem<String>("Surname: " + employee.getSurname());
					rootEmployee.getChildren().addAll(name, surname);
					rootItem.getChildren().add(rootEmployee);
				}
				treeView.setRoot(rootItem);
				treeView.setShowRoot(false);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			socket.close();
		} else {
			// user is not authorized to perform the action
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Not authorized");
			alert.setHeaderText("You are not allowed to perform this action.");
			alert.showAndWait();
		}
	}

	/**
	 * Displays all the orders in the TreeView.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws UnknownHostException if the IP address of the host could not be
	 *                              determined.
	 * @throws IOException          if an I/O error occurs when creating the socket.
	 * @see Order
	 */
	@FXML
	@SuppressWarnings("unchecked")
	void displayOrders(ActionEvent event) throws IOException {
		if (this.currentUser.getPermission() > 2) {
			// user is authorized to perform the action
			Socket socket = new Socket("localhost", 4316);

			// client -> server
			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(outputStream);
			String[] toBeSent = { "get_orders" };
			out.writeObject(toBeSent);

			// server ->client
			InputStream inputStream = socket.getInputStream();
			ObjectInputStream in = new ObjectInputStream(inputStream);

			try {
				ArrayList<Order> orders = (ArrayList<Order>) in.readObject();
				TreeItem<String> rootItem = new TreeItem<String>("Orders");

				for (Order order : orders) {
					TreeItem<String> rootOrder = new TreeItem<String>(Integer.toString(order.getId()));
					TreeItem<String> id = new TreeItem<String>("Order ID: " + order.getId());
					TreeItem<String> status = new TreeItem<String>("Status: " + order.getStatus());
					TreeItem<String> customer = new TreeItem<String>("Customer: " + order.getCustomer());
					rootOrder.getChildren().addAll(id, status, customer);

					for (Wine wine : order.getWines()) {
						TreeItem<String> rootProduct = new TreeItem<String>(
								String.format("%d - %s %s", wine.getProductId(), wine.getName(), wine.getYear()));
						TreeItem<String> quantity = new TreeItem<String>("Quantity: " + wine.getQuantity());
						rootProduct.getChildren().add(quantity);
						rootOrder.getChildren().add(rootProduct);
					}
					rootItem.getChildren().add(rootOrder);
				}
				treeView.setRoot(rootItem);
				treeView.setShowRoot(false);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			socket.close();
		} else {
			// user is not authorized to perform the action
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Not authorized");
			alert.setHeaderText("You are not allowed to perform this action.");
			alert.showAndWait();
		}
	}

	/**
	 * Displays all the users in the TreeView.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws UnknownHostException if the IP address of the host could not be
	 *                              determined.
	 * @throws IOException          if an I/O error occurs when creating the socket.
	 * @see User
	 */
	@FXML
	@SuppressWarnings("unchecked")
	void displayUsers(ActionEvent event) throws UnknownHostException, IOException {
		if (this.currentUser.getPermission() > 2) {
			// user is authorized to perform the action
			Socket socket = new Socket("localhost", 4316);

			// client -> server
			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(outputStream);
			String[] toBeSent = { "get_users" };
			out.writeObject(toBeSent);

			// server -> client
			InputStream inputStream = socket.getInputStream();
			ObjectInputStream in = new ObjectInputStream(inputStream);

			try {
				ArrayList<User> users = (ArrayList<User>) in.readObject();
				TreeItem<String> rootItem = new TreeItem<String>("Users");

				for (User user : users) {
					TreeItem<String> rootUser = new TreeItem<String>(user.getEmail());
					TreeItem<String> name = new TreeItem<String>("Name: " + user.getName());
					TreeItem<String> surname = new TreeItem<String>("Surname: " + user.getSurname());
					rootUser.getChildren().addAll(name, surname);
					rootItem.getChildren().add(rootUser);
				}
				treeView.setRoot(rootItem);
				treeView.setShowRoot(false);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			socket.close();
		} else {
			// user is not authorized to perform the action
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Not authorized");
			alert.setHeaderText("You are not allowed to perform this action.");
			alert.showAndWait();
		}
	}

	/**
	 * Displays all the wines in the TreeView.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws UnknownHostException if the IP address of the host could not be
	 *                              determined.
	 * @throws IOException          if an I/O error occurs when creating the socket.
	 * @see Wine
	 */
	@FXML
	@SuppressWarnings("unchecked")
	void displayWines(ActionEvent event) throws UnknownHostException, IOException {
		if (this.currentUser.getPermission() > 2) {
			// user is authorized to perform the action
			Socket socket = new Socket("localhost", 4316);

			// client -> server
			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(outputStream);
			String[] toBeSent = { "get_wines" };
			out.writeObject(toBeSent);

			// server -> client
			InputStream inputStream = socket.getInputStream();
			ObjectInputStream in = new ObjectInputStream(inputStream);

			try {
				ArrayList<Wine> wines = (ArrayList<Wine>) in.readObject();
				TreeItem<String> rootItem = new TreeItem<String>("Wines");

				for (Wine wine : wines) {
					TreeItem<String> rootWine = new TreeItem<String>(wine.getName());
					TreeItem<String> productId = new TreeItem<String>(
							"Product ID: " + Integer.toString(wine.getProductId()));
					TreeItem<String> producer = new TreeItem<String>("Producer: " + wine.getProducer());
					TreeItem<String> year = new TreeItem<String>("Year: " + Integer.toString(wine.getYear()));
					TreeItem<String> grapes = new TreeItem<String>("Grapewines: " + wine.getGrapewines());
					TreeItem<String> notes = new TreeItem<String>("Notes: " + wine.getNotes());
					TreeItem<String> quantity = new TreeItem<String>(
							"Quantity: " + Integer.toString(wine.getQuantity()));
					rootWine.getChildren().addAll(productId, producer, year, grapes, notes, quantity);
					rootItem.getChildren().add(rootWine);
				}
				treeView.setRoot(rootItem);
				treeView.setShowRoot(false);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			socket.close();
		} else {
			// user is not authorized to perform the action
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Not authorized");
			alert.setHeaderText("You are not allowed to perform this action.");
			alert.showAndWait();
		}
	}

	/**
	 * Goes to the employee homepage.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws IOException if the file can't be read.
	 */
	@FXML
	void loadEmployee(ActionEvent event) throws IOException {
		Loader loader = new Loader(this.currentUser, this.rootPane);
		loader.load("homepage_employee");
	}

	/**
	 * Goes back to the employee homepage.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws IOException if the file can't be read.
	 */
	@FXML
	void loadUser(ActionEvent event) throws IOException {
		Loader loader = new Loader(this.currentUser, this.rootPane);
		loader.load("homepage_user");
	}

	/**
	 * Goes to the add employee page.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws IOException if the file can't be read.
	 */
	@FXML
	void loadAddEmployee(ActionEvent event) throws IOException {
		Loader loader = new Loader(this.currentUser, this.rootPane);
		loader.load("add_employee");
	}

	/**
	 * Goes back to the login page.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws IOException if the file can't be read.
	 */
	@FXML
	void logout(ActionEvent event) throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("./login.fxml"));
		rootPane.getChildren().setAll(pane);
	}

}
