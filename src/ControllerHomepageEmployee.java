import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
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
	private AnchorPane rootPane;

	@FXML
	private TreeView<String> treeView;

	@FXML
	private TextField searchID;

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
	}

	/**
	 * Goes to page for adding new {@code Wine}s.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws IOException if the file can't be accessed.
	 */
	@FXML
	void loadAddWine(ActionEvent event) throws IOException {
		Loader loader = new Loader(this.current_user, this.rootPane);
		loader.load("add_wine");
	}

	@FXML
	void search(ActionEvent event) {
		// TODO (use "search_order" as msg[0]")
	}

	@FXML
	void loadShop(ActionEvent event) throws IOException {
		Loader loader = new Loader(this.current_user, this.rootPane);
		loader.load("homepage_user");
	}

	@FXML
	void loadRestockWine(ActionEvent event) throws IOException {
		Loader loader = new Loader(this.current_user, this.rootPane);
		loader.load("restock");
	}

	@FXML
	void shipOrder(ActionEvent event) {
		// TODO
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
	 * @param event GUI event. [ActionEvent]
	 * @throws UnknownHostException if the IP address of the host could not be
	 *                              determined.
	 * @throws IOException          if an I/O error occurs when creating the socket.
	 * @see Order
	 */
	@FXML
	@SuppressWarnings("unchecked")
	void displayOrders(ActionEvent event) throws IOException {

		if (this.current_user.getPermission() > 1) {
			// user is authorized to perform the action
			Socket socket = new Socket("localhost", 4316);

			// client -> server
			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(outputStream);
			String[] to_be_sent = { "get_orders" };
			out.writeObject(to_be_sent);

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
}