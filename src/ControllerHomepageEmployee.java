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
 * Controller for the Employee Homepage.
 */
public class ControllerHomepageEmployee implements Controller {

	private User currentUser;

	@FXML
	private AnchorPane rootPane;

	@FXML
	private TreeView<String> treeView;

	@FXML
	private Text name;

	// TODO Fix javadoc
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
		name.setText(this.currentUser.getName());

		try {
			displayOrders();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Goes to page for adding new {@code Wine}s.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws IOException if the file can't be accessed.
	 */
	@FXML
	void loadAddWine(ActionEvent event) throws IOException {
		Loader loader = new Loader(this.currentUser, this.rootPane);
		loader.load("add_wine");
	}

	/**
	 * Goes to page for shopping new {@code Wine}s.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws IOException if the file can't be accessed.
	 */
	@FXML
	void loadShop(ActionEvent event) throws IOException {
		Loader loader = new Loader(this.currentUser, this.rootPane);
		loader.load("homepage_user");
	}

	/**
	 * Goes to page for restocking new {@code Wine}s.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws IOException if the file can't be accessed.
	 */
	@FXML
	void loadRestockWine(ActionEvent event) throws IOException {
		Loader loader = new Loader(this.currentUser, this.rootPane);
		loader.load("restock");
	}

	// TODO javadoc
	@FXML
	void shipOrder(ActionEvent event) throws UnknownHostException, IOException {
		if (this.currentUser.getPermission() > 1) {
			// user is authorized to perform the action
			TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();

			if (selectedItem != null) {

				while (selectedItem.getParent() != treeView.getRoot()) {
					selectedItem = selectedItem.getParent();
				}
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Select an order");
				alert.setHeaderText("Please select an order.");
				alert.showAndWait();
				return;
			}
			Socket socket = new Socket("localhost", 4316);

			// client -> server
			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(outputStream);
			String[] toBeSent = { "ship_order", selectedItem.getValue() };
			out.writeObject(toBeSent);

			// server ->client
			InputStream inputStream = socket.getInputStream();
			ObjectInputStream in = new ObjectInputStream(inputStream);
			try {
				// TODO add comments
				Boolean shipped = (Boolean) in.readObject();
				if (shipped) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Shipping successfull");
					alert.setHeaderText(
							String.format("Order %d has been shipped", Integer.parseInt(selectedItem.getValue())));
					alert.showAndWait();
				} else {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Shipping failed.");
					alert.setHeaderText("Unable to ship order. Try again later.");
					alert.showAndWait();
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			socket.close();
			initData(this.currentUser);
		} else {

			// user is not authorized to perform the action
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Not authorized");
			alert.setHeaderText("You are not allowed to perform this action.");
			alert.showAndWait();
		}
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
		this.rootPane.getChildren().setAll(pane);
	}

	/**
	 * Displays all the orders in the TreeView.
	 * 
	 * @throws UnknownHostException if the IP address of the host could not be
	 *                              determined.
	 * @throws IOException          if an I/O error occurs when creating the socket.
	 * @see Order
	 */
	@SuppressWarnings("unchecked")
	void displayOrders() throws IOException {
		if (this.currentUser.getPermission() > 1) {
			// user is authorized to perform the action
			Socket socket = new Socket("localhost", 4316);

			// client -> server
			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(outputStream);
			String[] toBeSent = { "get_orders_employee", this.currentUser.getEmail() };
			out.writeObject(toBeSent);

			// server ->client
			InputStream inputStream = socket.getInputStream();
			ObjectInputStream in = new ObjectInputStream(inputStream);

			try {
				//receives the ArrayList of orders from the server
				ArrayList<Order> orders = (ArrayList<Order>) in.readObject();
				//creates the TreeView's root 
				TreeItem<String> rootItem = new TreeItem<String>("Orders");

				for (Order order : orders) {
					//fills the TreeView with the orders
					TreeItem<String> rootOrder = new TreeItem<String>(Integer.toString(order.getId()));
					TreeItem<String> id = new TreeItem<String>("Order ID: " + order.getId());
					TreeItem<String> status = new TreeItem<String>("Status: " + order.getStatus());
					TreeItem<String> customer = new TreeItem<String>("Customer: " + order.getCustomer());
					rootOrder.getChildren().addAll(id, status, customer);

					//for each order it displays every wine of the order
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
}