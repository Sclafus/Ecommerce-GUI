import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Controller for Cart, page accessible by {@code User} with permission > 0 (aka
 * everyone)
 */
public class ControllerCart implements Controller {

	private User current_user;

	@FXML
	private AnchorPane rootPane;

	@FXML
	private TableView<Wine> tableView;

	@FXML
	private TableColumn<Wine, String> name_column;

	@FXML
	private TableColumn<Wine, Integer> year_column;

	@FXML
	private TableColumn<Wine, String> producer_column;

	@FXML
	private TableColumn<Wine, Integer> quantity_column;


	/**
	 * Initialize {@code this.current_user} with the passed value. This method is
	 * made to be called from another controller, using the {@code load} method in
	 * {@code Loader} class.
	 * 
	 * @param user the {@code User} we want to pass. [User]
	 * @see Loader
	 */
	@SuppressWarnings("unchecked")
	public void initData(User user) {
		this.current_user = user;

		try{
			Socket socket = new Socket("localhost", 4316);

			// client -> server
			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(outputStream);
			String[] to_be_sent = { "display_cart", this.current_user.getEmail()};
			out.writeObject(to_be_sent);

			// server -> client
			InputStream inputStream = socket.getInputStream();
			ObjectInputStream in = new ObjectInputStream(inputStream);

			ArrayList<Wine> cart_result = (ArrayList<Wine>) in.readObject();
			
			addToTable(cart_result);
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void back(ActionEvent event) throws IOException {
		Loader loader = new Loader(this.current_user, this.rootPane);
		loader.load("homepage_user");
	}

	/**
	 * Allows the {@code User} to buy the items in his cart.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws UnknownHostException if the IP address of the host could not be
	 *                              determined.
	 * @throws IOException          if an I/O error occurs when creating the socket.
	 * @see User
	 */
	@FXML
	void buy(ActionEvent event) throws IOException, UnknownHostException {

		if (this.current_user.getPermission() > 0) {
			// user is authorized to perform the action
			Socket socket = new Socket("localhost", 4316);

			// client -> server
			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(outputStream);
			String[] to_be_sent = { "new_order", this.current_user.getEmail() };
			out.writeObject(to_be_sent);

			// server -> client
			InputStream inputStream = socket.getInputStream();
			ObjectInputStream in = new ObjectInputStream(inputStream);
			
			try {
				Order new_order = (Order) in.readObject();
				initData(this.current_user);
				if(new_order.getId()==0){
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Order failed!");
					alert.setHeaderText(String.format("Your order has been not been placed!\nWe do not enough in stock. We will send a notification once we restock"));
					alert.showAndWait();
				} else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Order submitted!");
					alert.setHeaderText(String.format("Your order has been placed!\nOrder ID: %d", new_order.getId()));
					alert.setContentText("(We are out of stock of the wines remaining in the cart or you are trying to order too many and we do not have enough, you will receive a notification once we restock!)");
					alert.showAndWait();
				}
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


	public void addToTable(ArrayList<Wine> wines) {
		// set up the columns in the table
		this.name_column.setCellValueFactory(new PropertyValueFactory<Wine, String>("Name"));
		this.year_column.setCellValueFactory(new PropertyValueFactory<Wine, Integer>("Year"));
		this.producer_column.setCellValueFactory(new PropertyValueFactory<Wine, String>("Producer"));
		this.quantity_column.setCellValueFactory(new PropertyValueFactory<Wine, Integer>("Quantity"));
		

		ObservableList<Wine> oListWine = FXCollections.observableArrayList(wines);

		// load data
		tableView.setItems(oListWine);

	}

	/**
	 * Allows anyone to search for wines.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws UnknownHostException if the IP address of the host could not be
	 *                              determined.
	 * @throws IOException          if an I/O error occurs when creating the socket.
	 */
	@FXML
	@SuppressWarnings("unchecked")
	void displayCart(ActionEvent event) throws IOException, ClassNotFoundException {
		Socket socket = new Socket("localhost", 4316);

		// client -> server
		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(outputStream);
		String[] to_be_sent = { "display_cart", this.current_user.getEmail()};
		out.writeObject(to_be_sent);

		// server -> client
		InputStream inputStream = socket.getInputStream();
		ObjectInputStream in = new ObjectInputStream(inputStream);

		ArrayList<Wine> cart_result = (ArrayList<Wine>) in.readObject();
		
		addToTable(cart_result);
		socket.close();

	}


	/**
	 * Allows the {@code User} to remove the wines to his cart.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws UnknownHostException if the IP address of the host could not be
	 *                              determined.
	 * @throws IOException          if an I/O error occurs when creating the socket.
	 * @see User
	 */
	@FXML
	
	void removeFromCart(ActionEvent event) throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", 4316);
		try {
			// getting selection of the tableview
			Wine wine = tableView.getSelectionModel().getSelectedItem();
			// client -> server
			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(outputStream);
			String[] to_be_sent = { "remove_from_cart", this.current_user.getEmail(), String.valueOf(wine.getProductId())};
			out.writeObject(to_be_sent);

			// server -> client
			InputStream inputStream = socket.getInputStream();
			ObjectInputStream in = new ObjectInputStream(inputStream);

			Boolean remove_result = (Boolean) in.readObject();
			if (remove_result) {
				initData(this.current_user);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(String.format("Removed from cart"));
				alert.setHeaderText(String.format("Removed %s from cart.", wine.getName()));
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle(String.format("Select a wine"));
				alert.setHeaderText("You have to click on a Wine and then Remove.");
				alert.showAndWait();

			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
		socket.close();

	}

}
