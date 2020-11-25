import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;

public class add_wine_controller {

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
    void addWine(ActionEvent event) {
        int yea;
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
        }

        try {
            yea = Integer.parseInt(yea_tmp);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Year");
            alert.setHeaderText("Please insert a valid year.");
            alert.showAndWait();
        }
    }

    @FXML
    void back(ActionEvent event) throws IOException{
        AnchorPane pane = FXMLLoader.load(getClass().getResource("./homepage_employee.fxml"));
        rootPane.getChildren().setAll(pane);
    }

}
