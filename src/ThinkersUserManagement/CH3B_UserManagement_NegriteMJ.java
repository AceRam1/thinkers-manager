package ThinkersUserManagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CH3B_UserManagement_NegriteMJ extends Application {
    
    //initializing the login scene
    //USERNAME AND PASSWORD IS AT Scene1Controller.java at LINE: 47
    @Override
    public void start(Stage stage) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Thinkers User Management App");
        stage.setResizable(false);
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
     
}
