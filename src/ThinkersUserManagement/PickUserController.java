package ThinkersUserManagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class PickUserController {
    
    @FXML
    private Button btnAdmin;

    @FXML
    private Button btnTutor;

    @FXML
    void switchToAdmin(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show(); 
    }

    @FXML
    void switchToTutor(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("TutorLogin.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show(); 
    }

}


