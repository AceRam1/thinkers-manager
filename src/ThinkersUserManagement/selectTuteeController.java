package ThinkersUserManagement;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class selectTuteeController {

    @FXML
    private Button btnPayment;

    @FXML
    private TextField tutee_id;

    static int payment_tuteeid;

    @FXML
    public void Payment() {
        if (tutee_id.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Insert a Tutee ID First");
            alert.showAndWait();
        } else {
            payment_tuteeid = Integer.parseInt(tutee_id.getText());
            switchToPayment();
        }
    }

    @FXML
    public void switchToPayment() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PaymentView.fxml"));
            Parent root = loader.load();
    
            // Create a new stage
            Stage TutorStage = new Stage();
            TutorStage.initModality(Modality.APPLICATION_MODAL);
            TutorStage.setTitle("Tutor Manager");
            TutorStage.setScene(new Scene(root));
    
            // Show the stage
            TutorStage.show();
            closeCurrentStage();
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void switchToPrompt(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Prompt.fxml"));
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

        private void closeCurrentStage(ActionEvent event) {
        Node sourceNode = (Node) event.getSource();
        Stage currentStage = (Stage) sourceNode.getScene().getWindow();
        currentStage.close();
    }

    private void closeCurrentStage() {
        Node sourceNode = (Node) btnPayment;
        Stage currentStage = (Stage) sourceNode.getScene().getWindow();
        currentStage.close();
    }
}