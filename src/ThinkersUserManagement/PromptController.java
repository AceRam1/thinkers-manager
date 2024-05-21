package ThinkersUserManagement;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PromptController implements Initializable {
    
    @FXML
    private Button back;

    @FXML
    private Button exit;

    @FXML
    private Button tutee;

    @FXML
    private Button tutor;

    @FXML
    private Button schedule;

    @FXML
    private Button payment;

    @FXML
    private Button tutorLogin;
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupButtonHoverEffect(tutor);
        setupButtonHoverEffect(tutee);
        setupButtonHoverEffect(schedule);
        setupButtonHoverEffect(payment);
        setupButtonHoverEffect(back);
        setupButtonHoverEffect(exit);
    }    
    
    public void exitApplication(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    public void switchToTutor(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Tutor.fxml"));
            Parent root = loader.load();
    
            // Create a new stage
            Stage TutorStage = new Stage();
            TutorStage.initModality(Modality.APPLICATION_MODAL);
            TutorStage.setTitle("Tutor Manager");
            TutorStage.setScene(new Scene(root));
    
            // Show the stage
            TutorStage.show();
            closeCurrentStage(event);

    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void switchToSched(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sched1.fxml"));
            Parent root = loader.load();
    
            // Create a new stage
            Stage TutorStage = new Stage();
            TutorStage.initModality(Modality.APPLICATION_MODAL);
            TutorStage.setTitle("Tutor Manager");
            TutorStage.setScene(new Scene(root));
    
            // Show the stage
            TutorStage.show();
            closeCurrentStage(event);

    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void switchToPayment(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("select-tutee.fxml"));
            Parent root = loader.load();
    
            // Create a new stage
            Stage TutorStage = new Stage();
            TutorStage.initModality(Modality.APPLICATION_MODAL);
            TutorStage.setTitle("Tutor Manager");
            TutorStage.setScene(new Scene(root));
    
            // Show the stage
            TutorStage.show();
            closeCurrentStage(event);

    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void switchToTutee(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Tutee.fxml"));
        stage = new Stage();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
        closeCurrentStage(event);
    }
    
    @FXML
    public void switchToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        stage = new Stage();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
        closeCurrentStage(event);
    }
    
    private void setupButtonHoverEffect(Button button) {
        
        button.setStyle("-fx-background-color:#ff914d; -fx-text-fill: #ffffff;");//standard color

        button.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            button.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #2b2b2b;");//color on mouse hover
        });

        button.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            button.setStyle("-fx-background-color: #ff914d; -fx-text-fill: #ffffff;");// color on mouse 
        });
    }
    
    private void closeCurrentStage(ActionEvent event) {
        Node sourceNode = (Node) event.getSource();
        Stage currentStage = (Stage) sourceNode.getScene().getWindow();
        currentStage.close();
    }

    
}
