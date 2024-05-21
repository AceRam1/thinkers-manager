package ThinkersUserManagement; // Add this line with the correct package name


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TuteeProfileController {

    @FXML
    private Button btnAddStudent;
    @FXML
    private Button btnBack;
    @FXML
    private Label lbAddress;
    @FXML
    private Label lbAge;
    @FXML
    private Label lbContact;
    @FXML
    private Label lbGender;
    @FXML
    private Label lbName;
    @FXML
    private Label lbNickname;
    @FXML
    private Label lbSubject;
    @FXML
    private TextField tfId;
    @FXML
    private TextField tfTimeSlot;


    public void disableAddStudentButton(boolean disable) {
        btnAddStudent.setDisable(disable);
    }

        // Setting up the connection
    public Connection getConnection() {
        Connection conn;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/thinkersdb", "root", ""); // Change database credentials if necessary
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setTuteesData() {

        // Set the values of labels based on the selected Tutees object
        lbName.setText(AssignController.name);
        lbNickname.setText(AssignController.nickname);
        lbGender.setText(AssignController.gender);
        lbAge.setText(Integer.toString(AssignController.age)); // Convert int to String
        lbAddress.setText(AssignController.address);
        lbContact.setText(AssignController.contact);
        lbSubject.setText(AssignController.subject);

        // Set the values of text fields based on the selected Tutees object
        tfId.setText(Integer.toString(AssignController.ID)); // Convert int to String
        tfTimeSlot.setText(AssignController.timeSlot);
        // Set other text fields as needed...
    }

    public void addStudent(){

        // Check if ID is null (0)
        if (AssignController.ID == 0) {
            // Show an alert indicating that no student is selected
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Student Selected");
            alert.setHeaderText("Please Select a Student First");
            alert.showAndWait();

            Stage stage = (Stage) btnAddStudent.getScene().getWindow();

            // Add a close request handler to the stage
            stage.close();

            switchToAssign();
        } else {

        String query = "INSERT INTO accepted_students (id, name, nickname, gender, age, address, subject_taking, contact_information, time_slot) VALUES ('"
        + AssignController.ID + "','"
        + AssignController.name + "','"
        + AssignController.nickname + "','"
        + AssignController.gender + "',"
        + Integer.toString(AssignController.age) + ",'"
        + AssignController.address + "','"
        + AssignController.subject + "','"
        + AssignController.contact + "','"
        + AssignController.timeSlot + "')";

        executeQuery(query);

        if ("10am-11am".equals(tfTimeSlot.getText())) {
            String query2 = "DELETE FROM 10am_11am WHERE id = " + tfId.getText();//setting up the query
            executeQuery(query2);//executing the query
        } else if ("1pm-2pm".equals(tfTimeSlot.getText())) {
            String query2 = "DELETE FROM 1pm_2pm WHERE id = " + tfId.getText();//setting up the query
            executeQuery(query2);//executing the query
        } else if ("2pm-3pm".equals(tfTimeSlot.getText())) {
            String query2 = "DELETE FROM 2pm_3pm WHERE id = " + tfId.getText();//setting up the query
            executeQuery(query2);//executing the query
        } else if ("3pm-4pm".equals(tfTimeSlot.getText())) {
            String query2 = "DELETE FROM 3pm_4pm WHERE id = " + tfId.getText();//setting up the query
            executeQuery(query2);//executing the query
        } else if ("4pm-5pm".equals(tfTimeSlot.getText())) {
            String query2 = "DELETE FROM 4pm_5pm WHERE id = " + tfId.getText();//setting up the query
            executeQuery(query2);//executing the query
        } else {
            // Handle any other time slots or provide a default case
            // You may want to throw an exception or log a message here
            System.out.println("time error");
        }

        switchToAssign();

        Stage stage = (Stage) btnAddStudent.getScene().getWindow();

        // Add a close request handler to the stage
        stage.close();

    }}
        private void executeQuery(String query) {//executing the query
        Connection conn = getConnection();
        Statement st;
        
        try{
            st = conn.createStatement();
            st.executeUpdate(query);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

        @FXML
    public void switchToAssign() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
            Parent root = loader.load();
    
            // Create a new stage
            Stage TutorStage = new Stage();
            TutorStage.initModality(Modality.APPLICATION_MODAL);
            TutorStage.setTitle("Assignment Manager");
            TutorStage.setScene(new Scene(root));
    
            // Show the stage
            TutorStage.show();
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToAssign2(ActionEvent event) {
        switchToAssign();
    }

    @FXML
    private void initialize() {
        setTuteesData();
    }

}