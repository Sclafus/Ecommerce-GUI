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

	private User currentUser;

	@FXML
	private AnchorPane rootPane;

	@FXML
	private TextField searchboxName;

	@FXML
	private TextField yearboxName;

	@FXML
	private TableView<Wine> tableView;

	@FXML
	private TableColumn<Wine, String> nameColumn;

	@FXML
	private TableColumn<Wine, Integer> yearColumn;

	@FXML
	private TableColumn<Wine, String> producerColumn;

	@FXML
	private TableColumn<Wine, String> grapesColumn;

	@FXML
	private TableColumn<Wine, String> notesColumn;

	@FXML
	private TextField quantity;

	// TODO Fix javadoc & comments
	/**
	 * Initialize {@code this.currentUser} with the passed value. This method is
	 * made to be called from another controller, using the {@code load} method in
	 * {@code Loader} class.
	 * 
	 * @param user the {@code User} we want to pass. [User]
	 * @see Loader
	 */
	@SuppressWarnings("unchecked")
	public void initData(User user) {
		this.currentUser = user;

		try {
			// Fill the frontpage with wines.
			Socket socket = new Socket("localhost", 4316);

			// client -> server
			OutputStream outputStream = socket.getOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(outputStream);
			String[] toBeSent = { "get_wines" };
			out.writeObject(toBeSent);

			// server ->client
			InputStream inputStream = socket.getInputStream();
			ObjectInputStream in = new ObjectInputStream(inputStream);
			ArrayList<Wine> wines = (ArrayList<Wine>) in.readObject();

			addToTable(wines);
			socket.close();

			// Checks notifications.
			Socket socket2 = new Socket("localhost", 4316);

			// client -> server
			OutputStream outputStream2 = socket2.getOutputStream();
			ObjectOutputStream out2 = new ObjectOutputStream(outputStream2);
			String[] toBeSentNotifications = { "get_notifications", this.currentUser.getEmail() };
			out2.writeObject(toBeSentNotifications);

			// server -> client
			InputStream inputStream2 = socket2.getInputStream();
			ObjectInputStream in2 = new ObjectInputStream(inputStream2);
			ArrayList<Wine> notification = (ArrayList<Wine>) in2.readObject();

			displayNotifications(notification);
			socket2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// TODO javadoc
	public void addToTable(ArrayList<Wine> wines) {
		// set up the columns in the table
		nameColumn.setCellValueFactory(new PropertyValueFactory<Wine, String>("Name"));
		yearColumn.setCellValueFactory(new PropertyValueFactory<Wine, Integer>("Year"));
		producerColumn.setCellValueFactory(new PropertyValueFactory<Wine, String>("Producer"));
		grapesColumn.setCellValueFactory(new PropertyValueFactory<Wine, String>("Grapewines"));
		notesColumn.setCellValueFactory(new PropertyValueFactory<Wine, String>("Notes"));
		ObservableList<Wine> oListWine = FXCollections.observableArrayList(wines); 
		// load data
		tableView.setItems(oListWine);
	}

	// TODO javadoc
	public void displayNotifications(ArrayList<Wine> wines) {
		if (wines.size() > 0) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Some wines have been restocked");
			alert.setHeaderText("These wines have been restocked:");
			StringBuilder winesSb = new StringBuilder();

			for (Wine wine : wines) {
				winesSb.append(String.format("%s (%d)\n", wine.getName(), wine.getYear()));
			}
			String winesString = winesSb.toString();
			System.out.format("'%s'", winesString);
			alert.setContentText(winesString);
			alert.showAndWait();
		}
	}

	// TODO comments
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
	@SuppressWarnings("unused")
	void addToCart(ActionEvent event) throws UnknownHostException, IOException {
		if (this.currentUser.getPermission() > 0) {
			Socket socket = new Socket("localhost", 4316);

			try {
				int quantity = Integer.parseInt(this.quantity.getText());
				// getting selection of the tableview
				Wine wine = tableView.getSelectionModel().getSelectedItem();

				// client -> server
				OutputStream outputStream = socket.getOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(outputStream);
				String[] toBeSent = { "add_to_cart", this.currentUser.getEmail(),
						String.valueOf(wine.getProductId()), this.quantity.getText() };
				out.writeObject(toBeSent);

				// server -> client
				InputStream inputStream = socket.getInputStream();
				ObjectInputStream in = new ObjectInputStream(inputStream);
				Boolean addResult = (Boolean) in.readObject();

				if (addResult) {
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
			} catch (NumberFormatException e) {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle(String.format("Insert quantity"));
				alert.setHeaderText("Please insert the quantity.");
				alert.showAndWait();

			}
			socket.close();
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Please login");
			alert.setHeaderText("You need to login to perform this action.");
			alert.showAndWait();
		}
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
		
		// client -> server
		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(outputStream);
		String[] toBeSent = { "search", searchboxName.getText(), yearboxName.getText() };
		out.writeObject(toBeSent);

		// server -> client
		InputStream inputStream = socket.getInputStream();
		ObjectInputStream in = new ObjectInputStream(inputStream);
		ArrayList<Wine> searchResult = (ArrayList<Wine>) in.readObject();

		addToTable(searchResult);
		socket.close();
	}

	//TODO fix javadoc
	/**
	 * Goes to the cart page.
	 * 
	 * @param event GUI event. [ActionEvent]
	 * @throws IOException if the file can't be accessed.
	 */
	@FXML
	void showCart(ActionEvent event) throws IOException {
		if (this.currentUser.getPermission() > 0) {
			Loader loader = new Loader(this.currentUser, this.rootPane);
			loader.load("cart");
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Please login");
			alert.setHeaderText("You need to login to perform this action.");
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
		rootPane.getChildren().setAll(pane);
	}
}