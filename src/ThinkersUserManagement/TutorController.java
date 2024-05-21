package ThinkersUserManagement;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class TutorController implements Initializable {
    
    
    //initialization
    private Stage stage;
    
    private Scene scene;
    
    private Parent root;

    @FXML
    private Button addButton;
    
    @FXML
    private Button resetbutton;

    @FXML
    private Button backButton;

    @FXML
    private Button btnSearch;

    @FXML
    private ChoiceBox<String> cbAvailability;
    
    private String[] arravailability = {"available", "unavailable"};

    @FXML
    private TableColumn<Tutors, String> colAddress;

    @FXML
    private TableColumn<Tutors, String> colAvailability;

    @FXML
    private TableColumn<Tutors, String> colContactInformation;

    @FXML
    private TableColumn<Tutors, String> colEducationalQualification;

    @FXML
    private TableColumn<Tutors, String> colFirstName;

    @FXML
    private TableColumn<Tutors, String> colGender;

    @FXML
    private TableColumn<Tutors, Integer> colID;

    @FXML
    private TableColumn<Tutors, String> colLastName;

    @FXML
    private TableColumn<Tutors, String> colSubject;

    @FXML
    private TableColumn<Tutors, Integer> colTeachingExperience;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField tfAddress;

    @FXML
    private TextField tfContactInformation;

    @FXML
    private TextField tfEducationalQualification;

    @FXML
    private TextField tfFirstName;

    @FXML
    private ChoiceBox<String> cbGender;
    
    private String[] arrgender = {"male", "female"};

    @FXML
    private TextField tfID;

    @FXML
    private TextField tfLastName;

    @FXML
    private ChoiceBox<String> cbSubject;
    
    private String[] arrsubject = {"Reading and Math", "Science Class", "Academic Assistance", "English Conversation Class"};

    @FXML
    private TextField tfTeachingExperience;

    @FXML
    private TextField tfSearchBox;

    @FXML
    private TableView<Tutors> tvTutors;

    @FXML
    private Button updateButton;
    
    @FXML
    private CheckBox subjAcademicAssistance;

    @FXML
    private CheckBox subjEnglishConversationClass;

    @FXML
    private CheckBox subjReadingAndMath;

    @FXML
    private CheckBox subjScienceClass;

    @FXML
    private ChoiceBox<String> cbSearchBy;
    private String[] arrSearch = {"ID", "Last Name", "First Name", "Subject"};
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        
        showTutors();
        
        cbGender.getItems().addAll(arrgender);
        cbAvailability.getItems().addAll(arravailability);
        cbSubject.getItems().addAll(arrsubject);
        
        setupButtonHoverEffect(addButton);
        setupButtonHoverEffect(backButton);
        setupButtonHoverEffect(updateButton);
        setupButtonHoverEffect(resetbutton);

        cbSearchBy.getItems().addAll(arrSearch);
        cbSearchBy.setValue(arrSearch[0]); // Set default value
    }
    
    //switching back to scene 1
    public void switchToPrompt(ActionEvent event) throws IOException{
        
        Parent root = FXMLLoader.load(getClass().getResource("Prompt.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();            
    }
    
    //button hover effects
    private void setupButtonHoverEffect(Button button) {
        
        button.setStyle("-fx-background-color:#ff914d; -fx-text-fill: #ffffff;");//standard color

        button.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
            button.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #2b2b2b;");//color on mouse hover
        });

        button.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
            button.setStyle("-fx-background-color: #ff914d; -fx-text-fill: #ffffff;");// color on mouse 
        });
    }
    
    //setting up the connection
    public Connection getConnection(){
        Connection conn;        
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/thinkersdb", "root","");//getting the database change if necessary
            return conn;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }            
    } 
    
    
    //showuser method to show the values of the user class within the observablelist
    public void showTutors(){
        ObservableList<Tutors> list = getTutorsList();
         
        colID.setCellValueFactory(new PropertyValueFactory<Tutors, Integer>("tutorID"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<Tutors, String>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<Tutors, String>("lastName"));
        colSubject.setCellValueFactory(new PropertyValueFactory<Tutors, String>("subject"));
        colGender.setCellValueFactory(new PropertyValueFactory<Tutors, String>("gender"));
        colAddress.setCellValueFactory(new PropertyValueFactory<Tutors, String>("address"));
        colContactInformation.setCellValueFactory(new PropertyValueFactory<Tutors, String>("contactInformation"));
        colEducationalQualification.setCellValueFactory(new PropertyValueFactory<Tutors, String>("educationalQualification"));
        colTeachingExperience.setCellValueFactory(new PropertyValueFactory<Tutors, Integer>("teachingExperience"));
        colAvailability.setCellValueFactory(new PropertyValueFactory<Tutors, String>("availability"));
    
        tvTutors.setItems(list);
    }
    
    public ObservableList<Tutors> getTutorsList(){
        ObservableList<Tutors> tutorList = FXCollections.observableArrayList();//instantiation
        Connection conn = getConnection();//setting up connection
        String query = "SELECT tutor_id, firstName, lastName, Reading_And_Math, Science_Class, Academic_Assistance, English_Conversation_Class, gender, address, contact_information, educational_qualification, teaching_experience, availability FROM tutors";//setting up the query
        Statement st;//setting up the statement
        ResultSet rs;//setting up the resultset
        
        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);
            Tutors tutors;
            while(rs.next()){//while the result set has next value keep on reading
                // Get subject values from the database
                int readingAndMath = rs.getInt("Reading_And_Math");
                int scienceClass = rs.getInt("Science_Class");
                int academicAssistance = rs.getInt("Academic_Assistance");
                int englishConversationClass = rs.getInt("English_Conversation_Class");
    
                // Concatenate subjects into a single string
                StringBuilder subjects = new StringBuilder();
                if (readingAndMath == 1) {
                    subjects.append("Reading and Math\n");
                }
                if (scienceClass == 1) {
                    subjects.append("Science Class\n");
                }
                if (academicAssistance == 1) {
                    subjects.append("Academic Assistance\n");
                }
                if (englishConversationClass == 1) {
                    subjects.append("English Conversation Class\n");
                }

                // Convert StringBuilder to String
                String subjectsString = subjects.toString();

                // Remove the trailing newline character if the subjects string is not empty
                if (subjectsString.endsWith("\n")) {
                    subjectsString = subjectsString.substring(0, subjectsString.length() - 1);
                }
    
                // Create Tutors object with concatenated subjects
                tutors = new Tutors(
                    rs.getInt("tutor_id"), 
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    subjects.toString(), 
                    rs.getString("gender"), 
                    rs.getString("address"), 
                    rs.getString("contact_information"), 
                    rs.getString("educational_qualification"), 
                    rs.getInt("teaching_experience"), 
                    rs.getString("availability")
                );        
                tutorList.add(tutors);//adding the values to the userlist obj
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return tutorList;//returns the list of the users
    }
    
    @FXML
    private Label warning;//initialized the warning label here since it somewhat doesnt get read if it's on top with the others
    
    private ObservableList<Tutors> tutorList;//setting up an observable list to be used in add account
    
    public void addAccount(){//add an account to the database
        String id = tfID.getText();
        
        if(tfID.getText().isBlank() || 
                tfFirstName.getText().isBlank() || 
                tfLastName.getText().isBlank() || 
                tfAddress.getText().isBlank() || 
                tfContactInformation.getText().isBlank() || 
                tfEducationalQualification.getText().isBlank() || 
                tfTeachingExperience.getText().isBlank() || 
                cbGender.getValue() == null || 
//                cbSubject.getValue() == null || 
                cbAvailability.getValue() == null){//authentication for blank fields
            
            warning.setText("Please fill in every data!");
        }else{
            addUser();//calling up the add user method
            resetFields();//calling up the reset field method to reset the field after click
            warning.setText("");//reseting back the text from the warning
            Alert alert = new Alert(Alert.AlertType.INFORMATION);//alerting the user if the operation was successful
            alert.setTitle("Account Addition");
            alert.setHeaderText("Account Added Successfully!");
            alert.showAndWait();
        }
    }

    @FXML
    private void searchTutors() {
        String searchBy = cbSearchBy.getValue();
        String searchText = tfSearchBox.getText();
        String category = null;
    
        if ("ID".equals(searchBy)) {
            category = "tutor_id";
        } else if ("Last Name".equals(searchBy)) {
            category = "lastName";
        } else if ("First Name".equals(searchBy)) {
            category = "firstName";
        } else if ("Subject".equals(searchBy)) {
            category = "subject";
        }
    
        ObservableList<Tutors> filteredList = FXCollections.observableArrayList();
    
        if (category != null && !searchText.isEmpty()) {
            Connection conn = getConnection();
            String query = "SELECT * FROM tutors WHERE " + category + " LIKE ?";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, "%" + searchText + "%");
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Tutors tutors = new Tutors(rs.getInt("tutor_id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("subject"), 
                        rs.getString("gender"), 
                        rs.getString("address"), 
                        rs.getString("contact_information"), 
                        rs.getString("educational_qualification"), 
                        rs.getInt("teaching_experience"), 
                        rs.getString("availability"));   
                        filteredList.add(tutors);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else {
            Connection conn = getConnection();
            String query = "SELECT * FROM tutors";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Tutors tutors = new Tutors(rs.getInt("tutor_id"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("subject"), 
                        rs.getString("gender"), 
                        rs.getString("address"), 
                        rs.getString("contact_information"), 
                        rs.getString("educational_qualification"), 
                        rs.getInt("teaching_experience"), 
                        rs.getString("availability"));   
                        filteredList.add(tutors);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }        }
        tvTutors.setItems(filteredList);
    }
     
    private void resetFields(){//method for reseting fields
        tfID.clear();
        tfFirstName.clear();
        tfLastName.clear();
        tfAddress.clear();
        cbAvailability.getSelectionModel().clearSelection();
        cbGender.getSelectionModel().clearSelection();
        cbSubject.getSelectionModel().clearSelection();
        tfContactInformation.clear();
        tfEducationalQualification.clear();
        tfTeachingExperience.clear();
        subjReadingAndMath.setSelected(false);
        subjScienceClass.setSelected(false);
        subjAcademicAssistance.setSelected(false);
        subjEnglishConversationClass.setSelected(false);

    }
    
    private void addUser() {
        String availability = cbAvailability.getValue();
        String gender = cbGender.getValue();
        int readingAndMath = subjReadingAndMath.isSelected() ? 1 : 0;
        int scienceClass = subjScienceClass.isSelected() ? 1 : 0;
        int academicAssistance = subjAcademicAssistance.isSelected() ? 1 : 0;
        int englishConversationClass = subjEnglishConversationClass.isSelected() ? 1 : 0;
    
        String query = "INSERT INTO tutors (tutor_id, firstName, lastName, availability, gender, Reading_And_Math, Science_Class, Academic_Assistance, English_Conversation_Class, address, contact_information, educational_qualification, teaching_experience) VALUES ('" 
        + tfID.getText() + "','" 
        + tfFirstName.getText() + "','" 
        + tfLastName.getText() + "','" 
        + availability + "','" 
        + gender + "'," 
        + readingAndMath + "," 
        + scienceClass + "," 
        + academicAssistance + "," 
        + englishConversationClass + ",'" 
        + tfAddress.getText() + "','" 
        + tfContactInformation.getText() + "','" 
        + tfEducationalQualification.getText() + "','" 
        + tfTeachingExperience.getText() + "')";
    
        executeQuery(query);
        showTutors();
    }

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
    
    public void updateRecord(){//updating the database
        String availability = cbAvailability.getValue();
        String gender = cbGender.getValue();
//        String subject = cbSubject.getValue();   
        String subject = "";

        if (subjReadingAndMath.isSelected()) {
            subject += "Reading and Math, ";
        }

        if (subjScienceClass.isSelected()) {
            subject += "Science Class, ";
        }

        if (subjAcademicAssistance.isSelected()) {
            subject += "Academic Assistance, ";
        }

        if (subjEnglishConversationClass.isSelected()) {
            subject += "English Conversation Class, ";
        }

        // Remove the trailing comma and space if the subject is not empty
        if (!subject.isEmpty()) {
            subject = subject.substring(0, subject.length() - 2);
        }


        if(tfID.getText().isBlank() || 
                tfFirstName.getText().isBlank() || 
                tfLastName.getText().isBlank() || 
                tfAddress.getText().isBlank() || 
                tfContactInformation.getText().isBlank() || 
                tfEducationalQualification.getText().isBlank() || 
                tfTeachingExperience.getText().isBlank() || 
                cbGender.getValue() == null || 
//                cbSubject.getValue() == null || 
                cbAvailability.getValue() == null){//authentication for blank fields
            
            warning.setText("Please fill in every data!");
        }else{
            
            String query = "UPDATE tutors SET " +
            "tutor_id = '" + tfID.getText() + "', " +
            "firstName = '" + tfFirstName.getText() + "', " +
            "lastName = '" + tfLastName.getText() + "', " +
            "availability = '" + availability + "', " +
            "gender = '" + gender + "', " +
            "Reading_And_Math = " + (subjReadingAndMath.isSelected() ? 1 : 0) + ", " +
            "Science_Class = " + (subjScienceClass.isSelected() ? 1 : 0) + ", " +
            "Academic_Assistance = " + (subjAcademicAssistance.isSelected() ? 1 : 0) + ", " +
            "English_Conversation_Class = " + (subjEnglishConversationClass.isSelected() ? 1 : 0) + ", " +
            "address = '" + tfAddress.getText() + "', " +
            "contact_information = '" + tfContactInformation.getText() + "', " +
            "educational_qualification = '" + tfEducationalQualification.getText() + "', " +
            "teaching_experience = '" + tfTeachingExperience.getText() + "' " +
            "WHERE tutor_id = '" + tfID.getText() + "'";

        executeQuery(query);//executing the query passing up the query string made earlier
        showTutors();//showing the updated list
        resetFields();//reseting the field to reset the text fields after use
        warning.setText("");//reseting the label back
        Alert alert = new Alert(Alert.AlertType.INFORMATION);//alert
        alert.setTitle("Account Deletion");
        alert.setHeaderText("Account Updated Successfully!");
        alert.showAndWait();
        }
    }
    
    public void deleteUser(){//deleting a user in the database
        
         if(tfID.getText().isBlank() || 
            tfFirstName.getText().isBlank() || 
            tfLastName.getText().isBlank() || 
            tfAddress.getText().isBlank() || 
            tfContactInformation.getText().isBlank() || 
            tfEducationalQualification.getText().isBlank() || 
            tfTeachingExperience.getText().isBlank() || 
            cbGender.getValue() == null || 
            cbSubject.getValue() == null || 
            cbAvailability.getValue() == null){//authentication for blank fields
            
            warning.setText("Please fill in every data!");
        }else{
            String query = "DELETE FROM tutors WHERE tutor_id = '" + tfID.getText() + "'";//setting up the query
            executeQuery(query);//executing the query
            showTutors();//showing the updated list
            resetFields();//reseting the fields
            warning.setText("");//reseting the warning text
            Alert alert = new Alert(Alert.AlertType.INFORMATION);//alerting the user
            alert.setTitle("Account Deletion");
            alert.setHeaderText("Account Deleted Successfully!");
            alert.showAndWait();
            
        }
    }
    
    @FXML
    private void handleMouseAction(MouseEvent event){//for handling mouse event if the user clicks on the row on the tableview it gets displayed on the textfields
        Tutors tutor = tvTutors.getSelectionModel().getSelectedItem();//setting up the user getting it's values
        tfID.setText("" +tutor.getTutorID());//setting it's value to the textfield
        tfFirstName.setText("" +tutor.getFirstName());//setting it's value to the textfield
        tfLastName.setText("" +tutor.getLastName());//setting it's value to the textfield
        tfAddress.setText("" +tutor.getAddress());//setting it's value to the textfield
        tfContactInformation.setText("" +tutor.getContactInformation());//setting it's value to the textfield
        tfEducationalQualification.setText("" +tutor.getEducationalQualification());//setting it's value to the textfield
        tfTeachingExperience.setText("" +tutor.getTeachingExperience());//setting it's value to the textfield     
        cbAvailability.setValue("" + tutor.getAvailability());//setting it's value to the textfield
        cbGender.setValue("" + tutor.getGender());//setting it's value to the textfield
        cbSubject.setValue("" + tutor.getSubject());//setting it's value to the textfield
        
    }
    
    public void reset(){
        tfID.clear();
        tfFirstName.clear();
        tfLastName.clear();
        tfAddress.clear();
        cbAvailability.getSelectionModel().clearSelection();
        cbGender.getSelectionModel().clearSelection();
        cbSubject.getSelectionModel().clearSelection();
        tfContactInformation.clear();
        tfEducationalQualification.clear();
        tfTeachingExperience.clear();
        subjReadingAndMath.setSelected(false);
        subjScienceClass.setSelected(false);
        subjAcademicAssistance.setSelected(false);
        subjEnglishConversationClass.setSelected(false);
    }

    public void init(Stage signUpStage) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'init'");
    }
    
}//end of class
