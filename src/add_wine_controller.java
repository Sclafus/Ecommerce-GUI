import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class Add_wine_controller {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField name;

    @FXML
    private TextField year;

    @FXML
    private TextField producer;

    @FXML
    private TextField grapes;

    @FXML
    private TextArea notes;

    @FXML
    void addWine(ActionEvent event) throws UnknownHostException, IOException {
        int yea = 0;
        String nam = name.getText();
        String yea_tmp = year.getText();
        String pro = producer.getText();
        String gra = grapes.getText();
        String not = notes.getText();

        if(nam.length() == 0 || yea_tmp.length() == 0 || pro.length() == 0 ||
         gra.length() == 0 || not.length() == 0){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("All fields must be filled");
            alert.setHeaderText("Please fill all the fields");
            alert.showAndWait();
        } else {
            try{
                yea = Integer.parseInt(yea_tmp);
            } catch (NumberFormatException e){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Invalid Year");
                alert.setHeaderText("Please insert a valid year.");
                alert.showAndWait();
            } finally{
                Socket socket = new Socket("localhost", 4316);

                OutputStream outputStream = socket.getOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(outputStream);
                String[] to_be_sent  = {"add_wine", nam, yea_tmp, pro, gra, not};
                out.writeObject(to_be_sent);
    
                InputStream inputStream = socket.getInputStream();
                ObjectInputStream in = new ObjectInputStream(inputStream);
    
                
                try {
                    int permission = (int) in.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                
                socket.close();
            }
            
        }
    }

    @FXML
    void back(ActionEvent event) throws IOException{
        AnchorPane pane = FXMLLoader.load(getClass().getResource("./homepage_employee.fxml"));
        rootPane.getChildren().setAll(pane);
    }

}
