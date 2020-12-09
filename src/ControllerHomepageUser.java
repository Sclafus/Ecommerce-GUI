import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

/**
 * Controller for the User Homepage.
 */
public class ControllerHomepageUser implements Controller {

	private User current_user;

	@FXML
	private AnchorPane rootPane;

	@FXML
	private TextField searchboxName;

	@FXML
	private TextField yearboxName;

	@FXML
	private TableView<Wine> tableView;

	@FXML
	private TableColumn<Wine, String> name_column;

	@FXML
	private TableColumn<Wine, Integer> year_column;

	@FXML
	private TableColumn<Wine, String> producer_column;

	@FXML
	private TableColumn<Wine, String> grapes_column;

	@FXML
	private TableColumn<Wine, String> notes_column;

	@FXML
	private TextField quantity;

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
		// TODO fill frontpage
	}    
	
	public void addToTable(ArrayList<Wine> wines){
		//set up the columns in the table
		name_column.setCellValueFactory(new PropertyValueFactory<Wine, String>("Name"));
		year_column.setCellValueFactory(new PropertyValueFactory<Wine, Integer>("Year"));
		producer_column.setCellValueFactory(new PropertyValueFactory<Wine, String>("Producer"));
		grapes_column.setCellValueFactory(new PropertyValueFactory<Wine, String>("Grapes"));
		notes_column.setCellValueFactory(new PropertyValueFactory<Wine, String>("Notes"));

		ObservableList<Wine> oListWine = FXCollections.observableArrayList(wines);
		
		//load data
		tableView.setItems(oListWine);

	}
	/**
	 * Allows the {@code User} to add the wines to his cart.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws UnknownHostException if the IP address of the host could not be
	 *                              determined.
	 * @throws IOException          if an I/O error occurs when creating the socket.
	 * @see User
	 */
	@FXML
	// @SuppressWarnings("unchecked")
	void addToCart(ActionEvent event) throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost", 4316);
		int quantity = Integer.parseInt(this.quantity.getText());
		//getting selection of the treeview
		Wine wine = tableView.getSelectionModel().getSelectedItem();
		// client -> server
		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(outputStream);
		String[] to_be_sent = { "add_to_cart", this.current_user.getEmail(), String.valueOf(wine.getProductId()), this.quantity.getText() };
		out.writeObject(to_be_sent);

		// server -> client
		InputStream inputStream = socket.getInputStream();
		ObjectInputStream in = new ObjectInputStream(inputStream);

		try {
			Boolean add_result = (Boolean) in.readObject();
			if(add_result){
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle(String.format("Added to cart"));
				alert.setHeaderText(String.format("Added %s to cart.", wine.getName()));
				alert.showAndWait();
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle(String.format("Select a wine"));
				alert.setHeaderText("You have to click on a Wine, enter the quantity and then Add.");
				alert.showAndWait();

			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		socket.close();

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
	void search(ActionEvent event) throws IOException, ClassNotFoundException {
		Socket socket = new Socket("localhost", 4316);
		//client -> server
		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(outputStream);
		String[] to_be_sent = { "search", searchboxName.getText(), yearboxName.getText() };
		out.writeObject(to_be_sent);

		//server -> client
		InputStream inputStream = socket.getInputStream();
		ObjectInputStream in = new ObjectInputStream(inputStream);

		ArrayList<Wine> search_result = (ArrayList<Wine>) in.readObject();

		addToTable(search_result);
		/*
		System.out.println("search result:");
		for (Wine wine : search_result) {
			System.out.println(wine.getName() + "  " + wine.getYear());
		}*/
		socket.close();

	}

	/**
	 * Goes to the cart page.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws IOException if the file can't be accessed.
	 */
	@FXML
	void showCart(ActionEvent event) throws IOException {
		Loader loader = new Loader(this.current_user, this.rootPane);
		loader.load("cart");
	}

	/**
	 * Goes to the orders page.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws IOException if the file can't be accessed.
	 */
	@FXML
	void showOrders(ActionEvent event) throws IOException {
		Loader loader = new Loader(this.current_user, this.rootPane);
		loader.load("orders");
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