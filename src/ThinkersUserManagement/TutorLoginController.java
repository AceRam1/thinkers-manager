package ThinkersUserManagement;

import java.io.IOException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TutorLoginController {
   
    //initialization
    @FXML
    private Button login;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    private Label wrongPassword;
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    //method to switch to the next scene
    public void switchToScene(ActionEvent event) throws IOException{
        
        //checks if it matches the said account in the login page (change if you want)
        if(username.getText().toString().equals("tutor") && password.getText().toString().equals("tutor")){//
           Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
           stage = (Stage)((Node)event.getSource()).getScene().getWindow();
           scene = new Scene(root);
           stage.setScene(scene);
           stage.show();        
        }else if(username.getText().toString().isEmpty() && password.getText().toString().isEmpty()){
            wrongPassword.setText("Please Fill in The Data");
        }else{
            wrongPassword.setText("Wrong Username or Password!");
        }
        
    }
    
    //method to exit the application
    public void exitApplication(ActionEvent event) {
        Platform.exit();
    }

}

