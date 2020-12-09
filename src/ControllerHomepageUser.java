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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    private TableView<String> tableView;

    @FXML
    private TableColumn<Wine, String> name_year_column;

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
	
	@FXML
    void addToCart(ActionEvent event) {

    }

	@FXML
	@SuppressWarnings("unchecked")
	void search(ActionEvent event) throws IOException, ClassNotFoundException {
		Socket socket = new Socket("localhost", 4316);

		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(outputStream);
		String[] to_be_sent = { "search", searchboxName.getText(), yearboxName.getText() };
		out.writeObject(to_be_sent);

		InputStream inputStream = socket.getInputStream();
		ObjectInputStream in = new ObjectInputStream(inputStream);

		ArrayList<Wine> search_result = (ArrayList<Wine>) in.readObject();
		// TODO
		System.out.println("search result:");
		for (Wine wine : search_result) {
			System.out.println(wine.getName() + "  " + wine.getYear());
		}
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
