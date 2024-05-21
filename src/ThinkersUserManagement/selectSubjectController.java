package ThinkersUserManagement;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class selectSubjectController {
    
@FXML
private RadioButton subjAcademicAssistance;
@FXML
private RadioButton subjEnglishConversationClass;
@FXML
private RadioButton subjReadingAndMath;
@FXML
private RadioButton subjScienceClass;

@FXML
private ToggleGroup radiosubject;

static String subject;

@FXML
private void handleMouseAction(MouseEvent event) {
    RadioButton selectedRadioButton = (RadioButton) radiosubject.getSelectedToggle();

    if (selectedRadioButton != null) {
        String selectedSubject = selectedRadioButton.getText();
        
        // Check if the selected subject is "English Conversation"
        if (selectedSubject.equals("English Conversation")) {
            // Modify the selected subject to "English Conversation Class"
            selectedSubject = "English Conversation Class";
        }
        
        TuteeController.selectedSubject = selectedSubject;
        System.out.println(TuteeController.selectedSubject);
    }
}

public void switchToSelect() {
    try {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("select-tutor.fxml"));
    Parent root = loader.load();

    // Create a new stage
    Stage tuteeStage = new Stage();
    tuteeStage.initModality(Modality.APPLICATION_MODAL);
    tuteeStage.setTitle("Select a Tutor");
    tuteeStage.setScene(new Scene(root));
    // Show the stage
    tuteeStage.show();

    } catch (IOException e) {
        e.printStackTrace();
    }}

}
