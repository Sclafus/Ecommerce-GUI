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
	private AnchorPane rootPane; // TODO Fix this

	@FXML
	private TextField searchboxName; // TODO Fix this

	@FXML
	private TextField yearboxName; // TODO Fix this

	@FXML
	private TableView<Wine> tableView; // TODO Fix this

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

	// TODO Fix javadoc & comments
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
		try {
			// Fill the frontpage with wines.

			Socket socket = new Socket("localhost", 4316);
			// client -> server
			OutputStream output_stream = socket.getOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(output_stream);
			String[] to_be_sent = { "get_wines" };
			out.writeObject(to_be_sent);
			// server ->client
			InputStream input_stream = socket.getInputStream();
			ObjectInputStream in = new ObjectInputStream(input_stream);
			ArrayList<Wine> wines = (ArrayList<Wine>) in.readObject();
			addToTable(wines);
			socket.close();

			// Checks notifications.

			Socket socket2 = new Socket("localhost", 4316);
			// client -> server
			OutputStream output_stream2 = socket2.getOutputStream();
			ObjectOutputStream out2 = new ObjectOutputStream(output_stream2);
			String[] to_be_sent_notifications = { "get_notifications", this.current_user.getEmail() };
			out2.writeObject(to_be_sent_notifications);
			// server -> client
			InputStream input_stream2 = socket2.getInputStream();
			ObjectInputStream in2 = new ObjectInputStream(input_stream2);
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
		name_column.setCellValueFactory(new PropertyValueFactory<Wine, String>("Name"));
		year_column.setCellValueFactory(new PropertyValueFactory<Wine, Integer>("Year"));
		producer_column.setCellValueFactory(new PropertyValueFactory<Wine, String>("Producer"));
		grapes_column.setCellValueFactory(new PropertyValueFactory<Wine, String>("Grapewines"));
		notes_column.setCellValueFactory(new PropertyValueFactory<Wine, String>("Notes"));

		ObservableList<Wine> oListWine = FXCollections.observableArrayList(wines); // TODO Fix this

		// load data
		tableView.setItems(oListWine); // TODO Fix this

	}

	// TODO javadoc
	public void displayNotifications(ArrayList<Wine> wines) {
		if (wines.size() > 0) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Some wines have been restocked");
			alert.setHeaderText("These wines have been restocked:");
			StringBuilder wines_sb = new StringBuilder();
			for (Wine wine : wines) {
				wines_sb.append(String.format("%s (%d)\n", wine.getName(), wine.getYear()));
			}
			String wines_string = wines_sb.toString();
			System.out.format("'%s'", wines_string);
			alert.setContentText(wines_string);
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
		if(this.current_user.getPermission()!=0){
			Socket socket = new Socket("localhost", 4316);
			try {
				int quantity = Integer.parseInt(this.quantity.getText());
				// getting selection of the tableview
				Wine wine = tableView.getSelectionModel().getSelectedItem(); // TODO Fix this
				// client -> server
				OutputStream output_stream = socket.getOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(output_stream);
				String[] to_be_sent = { "add_to_cart", this.current_user.getEmail(), String.valueOf(wine.getProductId()),
						this.quantity.getText() };
				out.writeObject(to_be_sent);

				// server -> client
				InputStream input_stream = socket.getInputStream();
				ObjectInputStream in = new ObjectInputStream(input_stream);

				Boolean add_result = (Boolean) in.readObject();
				if (add_result) {
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
		} else{}	
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
		OutputStream output_stream = socket.getOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(output_stream);
		String[] to_be_sent = { "search", searchboxName.getText(), yearboxName.getText() };
		out.writeObject(to_be_sent);

		// server -> client
		InputStream input_stream = socket.getInputStream();
		ObjectInputStream in = new ObjectInputStream(input_stream);

		ArrayList<Wine> search_result = (ArrayList<Wine>) in.readObject();

		addToTable(search_result);
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
		if (this.current_user.getPermission() > 0) {
			Loader loader = new Loader(this.current_user, this.rootPane);
			loader.load("cart");
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