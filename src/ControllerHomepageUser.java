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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class ControllerHomepageUser implements Controller {

	private User current_user;

	@FXML
    private AnchorPane rootPane;

    @FXML
    private Text name1;

    @FXML
    private Text year1;

    @FXML
    private Text notes1;

    @FXML
    private Text name2;
	
    @FXML
    private Text year2;
	
    @FXML
    private Text notes2;
	
	@FXML
    private Text name3;

    @FXML
    private Text year3;

    @FXML
    private Text notes3;

    @FXML
    private Text name4;

    @FXML
    private Text year4;

    @FXML
    private Text notes4;
	
    @FXML
    private Text name5;

    @FXML
    private Text year5;

    @FXML
	private Text notes5;
	
    @FXML
    private Text name6;

    @FXML
    private Text year6;

    @FXML
    private Text notes6;

    @FXML
    private TextField searchboxName;

    @FXML
    private TextField yearboxName;

	public void initData(User user){
		current_user = user;
	}

	@FXML
	void logout(ActionEvent event) throws IOException{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("./login.fxml"));
		rootPane.getChildren().setAll(pane);
	}

	@FXML
	void nextPage(ActionEvent event) {

	}

	@FXML
	void previousPage(ActionEvent event) {

	}

	@FXML
	void search(ActionEvent event) throws IOException, ClassNotFoundException {
		Socket socket = new Socket("localhost", 4316);

		OutputStream outputStream = socket.getOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(outputStream);
		String[] to_be_sent = {"search", searchboxName.getText(), yearboxName.getText()};
		out.writeObject(to_be_sent);

		InputStream inputStream = socket.getInputStream();
		ObjectInputStream in = new ObjectInputStream(inputStream);
		
		ArrayList<Wine> search_result = (ArrayList<Wine>) in.readObject();
		System.out.println("search result:");
		for(Wine wine : search_result){
			System.out.println(wine.getName() + "  " + wine.getYear());
		}
		socket.close();

	}

	@FXML
	void showCart(ActionEvent event) throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("./cart.fxml"));
		rootPane.getChildren().setAll(pane);
	}

	@FXML
	void showNotifications(ActionEvent event) throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("./notifications.fxml"));
		rootPane.getChildren().setAll(pane);
	}

	@FXML
	void showOrders(ActionEvent event) throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("./orders.fxml"));
		rootPane.getChildren().setAll(pane);
	}

}
