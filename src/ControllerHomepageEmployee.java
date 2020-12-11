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
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 * Controller for the Employee Homepage.
 */
public class ControllerHomepageEmployee implements Controller {

	private User current_user;

	@FXML
	private AnchorPane rootPane; // TODO Fix this

	@FXML
	private TreeView<String> treeView; // TODO Fix this

	@FXML
	private TextField searchID; // TODO Fix this

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
		name.setText(this.current_user.getName());
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
		Loader loader = new Loader(this.current_user, this.rootPane); // TODO Fix this
		loader.load("add_wine");
	}

	//TODO javadoc
	@FXML
	void loadShop(ActionEvent event) throws IOException {
		Loader loader = new Loader(this.current_user, this.rootPane); // TODO Fix this
		loader.load("homepage_user");
	}

	// TODO javadoc
	@FXML
	void loadRestockWine(ActionEvent event) throws IOException {
		Loader loader = new Loader(this.current_user, this.rootPane); // TODO Fix this
		loader.load("restock");
	}

	// TODO javadoc
	@FXML
	void shipOrder(ActionEvent event) throws UnknownHostException, IOException {
		if (this.current_user.getPermission() > 1) {
			// user is authorized to perform the action

			TreeItem<String> selected_item = treeView.getSelectionModel().getSelectedItem();
			if (selected_item != null) {
				while (selected_item.getParent() != treeView.getRoot()) {
					selected_item = selected_item.getParent();
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
			OutputStream output_stream = socket.getOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(output_stream);
			String[] to_be_sent = { "ship_order", selected_item.getValue() };
			out.writeObject(to_be_sent);

			// server ->client
			InputStream input_stream = socket.getInputStream();
			ObjectInputStream in = new ObjectInputStream(input_stream);
			try {
				Boolean shipped = (Boolean) in.readObject();
				if (shipped) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Shipping successfull");
					alert.setHeaderText(
							String.format("Order %d has been shipped", Integer.parseInt(selected_item.getValue())));
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
			initData(this.current_user);
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
		this.rootPane.getChildren().setAll(pane); // TODO Fix this
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

		if (this.current_user.getPermission() > 1) {
			// user is authorized to perform the action
			Socket socket = new Socket("localhost", 4316);

			// client -> server
			OutputStream output_stream = socket.getOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(output_stream);
			String[] to_be_sent = { "get_orders_employee", this.current_user.getEmail() };
			out.writeObject(to_be_sent);

			// server ->client
			InputStream input_stream = socket.getInputStream();
			ObjectInputStream in = new ObjectInputStream(input_stream);

			try {
				ArrayList<Order> orders = (ArrayList<Order>) in.readObject();
				TreeItem<String> root_item = new TreeItem<String>("Orders");

				for (Order order : orders) {
					TreeItem<String> root_order = new TreeItem<String>(Integer.toString(order.getId()));
					TreeItem<String> id = new TreeItem<String>("Order ID: " + order.getId());
					TreeItem<String> status = new TreeItem<String>("Status: " + order.getStatus());
					TreeItem<String> customer = new TreeItem<String>("Customer: " + order.getCustomer());
					root_order.getChildren().addAll(id, status, customer);

					for (Wine wine : order.getWines()) {
						TreeItem<String> root_product = new TreeItem<String>(
								String.format("%d - %s %s", wine.getProductId(), wine.getName(), wine.getYear()));
						TreeItem<String> quantity = new TreeItem<String>("Quantity: " + wine.getQuantity());
						root_product.getChildren().add(quantity);
						root_order.getChildren().add(root_product);

					}

					root_item.getChildren().add(root_order);
				}
				treeView.setRoot(root_item);
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