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

	private User current_user;

	@FXML
	private AnchorPane rootPane; //TODO Fix this

	@FXML
	private TreeView<String> treeView; //TODO Fix this

	@FXML
	private Text name;

	/**
	 * Initialize {@code this.current_user} with the passed value. This method is
	 * made to be called from another controller, using the {@code load} method in
	 * {@code Loader} class.
	 * 
	 * @param user the {@code User} we want to pass. [User]
	 * @see Loader
	 */
	public void initData(User user) {
		this.current_user = user;
		this.name.setText(this.current_user.getName());
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

		if (this.current_user.getPermission() > 2) {

			// user is authorized to perform the action
			Socket socket = new Socket("localhost", 4316);

			// client -> server
			OutputStream output_stream = socket.getOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(output_stream);
			String[] to_be_sent = { "get_employees" };
			out.writeObject(to_be_sent);

			// server -> client
			InputStream input_stream = socket.getInputStream();
			ObjectInputStream in = new ObjectInputStream(input_stream);

			try {
				ArrayList<User> employees = (ArrayList<User>) in.readObject();
				TreeItem<String> rootItem = new TreeItem<String>("Employees"); //TODO Fix this

				for (User employee : employees) {
					TreeItem<String> rootEmployee = new TreeItem<String>(employee.getEmail()); //TODO Fix this
					TreeItem<String> name = new TreeItem<String>("Name: " + employee.getName());
					TreeItem<String> surname = new TreeItem<String>("Surname: " + employee.getSurname());
					rootEmployee.getChildren().addAll(name, surname); //TODO Fix this
					rootItem.getChildren().add(rootEmployee); //TODO Fix this
				}
				treeView.setRoot(rootItem); //TODO Fix this
				treeView.setShowRoot(false); //TODO Fix this

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

		if (this.current_user.getPermission() > 2) {
			// user is authorized to perform the action
			Socket socket = new Socket("localhost", 4316);

			// client -> server
			OutputStream output_stream = socket.getOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(output_stream);
			String[] to_be_sent = { "get_orders" };
			out.writeObject(to_be_sent);

			// server ->client
			InputStream input_stream = socket.getInputStream();
			ObjectInputStream in = new ObjectInputStream(input_stream);

			try {
				ArrayList<Order> orders = (ArrayList<Order>) in.readObject();
				TreeItem<String> rootItem = new TreeItem<String>("Orders"); //TODO Fix this

				for (Order order : orders) {
					TreeItem<String> rootOrder = new TreeItem<String>(Integer.toString(order.getId())); //TODO Fix this
					TreeItem<String> id = new TreeItem<String>("Order ID: " + order.getId());
					TreeItem<String> status = new TreeItem<String>("Status: " + order.getStatus());
					TreeItem<String> customer = new TreeItem<String>("Customer: " + order.getCustomer());
					rootOrder.getChildren().addAll(id, status, customer); //TODO Fix this

					for (Wine wine : order.getWines()) {
						TreeItem<String> rootProduct = new TreeItem<String>(
								String.format("%d - %s %s", wine.getProductId(), wine.getName(), wine.getYear())); //TODO Fix this
						TreeItem<String> quantity = new TreeItem<String>("Quantity: " + wine.getQuantity());
						rootProduct.getChildren().add(quantity); //TODO Fix this
						rootOrder.getChildren().add(rootProduct); //TODO Fix this
					}

					rootItem.getChildren().add(rootOrder); //TODO Fix this
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

		if (this.current_user.getPermission() > 2) {
			// user is authorized to perform the action
			Socket socket = new Socket("localhost", 4316);

			// client -> server
			OutputStream output_stream = socket.getOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(output_stream);
			String[] to_be_sent = { "get_users" };
			out.writeObject(to_be_sent);

			// server -> client
			InputStream input_stream = socket.getInputStream();
			ObjectInputStream in = new ObjectInputStream(input_stream);

			try {
				ArrayList<User> users = (ArrayList<User>) in.readObject();
				TreeItem<String> rootItem = new TreeItem<String>("Users"); //TODO Fix this

				for (User user : users) {
					TreeItem<String> rootUser = new TreeItem<String>(user.getEmail()); //TODO Fix this
					TreeItem<String> name = new TreeItem<String>("Name: " + user.getName());
					TreeItem<String> surname = new TreeItem<String>("Surname: " + user.getSurname());
					rootUser.getChildren().addAll(name, surname); //TODO Fix this
					rootItem.getChildren().add(rootUser); //TODO Fix this
				}
				treeView.setRoot(rootItem); //TODO Fix this
				treeView.setShowRoot(false); //TODO Fix this

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

		if (this.current_user.getPermission() > 2) {
			// user is authorized to perform the action
			Socket socket = new Socket("localhost", 4316);

			// client -> server
			OutputStream output_stream = socket.getOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(output_stream);
			String[] to_be_sent = { "get_wines" };
			out.writeObject(to_be_sent);

			// server -> client
			InputStream input_stream = socket.getInputStream();
			ObjectInputStream in = new ObjectInputStream(input_stream);

			try {
				ArrayList<Wine> wines = (ArrayList<Wine>) in.readObject();
				TreeItem<String> rootItem = new TreeItem<String>("Wines"); //TODO Fix this

				for (Wine wine : wines) {
					TreeItem<String> rootWine = new TreeItem<String>(wine.getName()); //TODO Fix this
					TreeItem<String> product_id = new TreeItem<String>(
							"Product ID: " + Integer.toString(wine.getProductId()));
					TreeItem<String> producer = new TreeItem<String>("Producer: " + wine.getProducer());
					TreeItem<String> year = new TreeItem<String>("Year: " + Integer.toString(wine.getYear()));
					TreeItem<String> grapes = new TreeItem<String>("Grapewines: " + wine.getGrapewines());
					TreeItem<String> notes = new TreeItem<String>("Notes: " + wine.getNotes());
					TreeItem<String> quantity = new TreeItem<String>(
							"Quantity: " + Integer.toString(wine.getQuantity()));

					rootWine.getChildren().addAll(product_id, producer, year, grapes, notes, quantity); //TODO Fix this
					rootItem.getChildren().add(rootWine); //TODO Fix this
				}
				treeView.setRoot(rootItem); //TODO Fix this
				treeView.setShowRoot(false); //TODO Fix this

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
	 * @throws IOException if the file cannot be read.
	 */
	@FXML
	void loadEmployee(ActionEvent event) throws IOException {
		Loader loader = new Loader(this.current_user, this.rootPane);
		loader.load("homepage_employee");
	}

	/**
	 * Goes back to the employee homepage.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws IOException if the filename cannot be read.
	 */
	@FXML
	void loadUser(ActionEvent event) throws IOException {
		Loader loader = new Loader(this.current_user, this.rootPane);
		loader.load("homepage_user");
	}

	/**
	 * loads add_employee.fxml Adds a new employee to the database:
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws IOException if the client can't open the file.
	 */
	@FXML
	void loadAddEmployee(ActionEvent event) throws IOException {
		Loader loader = new Loader(this.current_user, this.rootPane);
		loader.load("add_employee");
	}

	/**
	 * Goes back to the login page.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws IOException if the filename cannot be read.
	 */
	@FXML
	void logout(ActionEvent event) throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("./login.fxml"));
		rootPane.getChildren().setAll(pane);
	}

}
