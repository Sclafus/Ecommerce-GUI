import java.io.IOException;
import java.net.UnknownHostException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TreeItem;

/**
 * Controller for Orders, page accessible by {@code User} with permission > 0
 * (aka everyone)
 */
public class ControllerOrders implements Controller {

	private User current_user;

	@FXML
	private AnchorPane rootPane;

	@FXML
	private TreeView<String> treeView;

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
		try {
			fillTreeView();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Goes back to the user homepage.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws IOException if the file can't be accessed.
	 */
	@FXML
	void back(ActionEvent event) throws IOException {
		Loader loader = new Loader(this.current_user, this.rootPane);
		loader.load("homepage_user");
	}

	/**
	 * Fills the TreeView with the orders made by the {@code User}.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws UnknownHostException if the IP address of the host could not be
	 *                              determined.
	 * @throws IOException          if an I/O error occurs when creating the socket.
	 * @see User
	 */
	@SuppressWarnings("unchecked")
	public void fillTreeView() throws UnknownHostException, IOException {

		if (this.current_user.getPermission() > 0) {
			// user is authorized to perform the action
			Socket socket = new Socket("localhost", 4316);

			// client -> server
			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(outputStream);
			String[] to_be_sent = { "get_orders_user", this.current_user.getEmail() };
			out.writeObject(to_be_sent);

			// server -> client
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
