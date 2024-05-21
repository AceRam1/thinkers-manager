package ThinkersUserManagement;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SelectTutorController implements Initializable {

    @FXML
    private Button btnSearch;
    @FXML
    private Button btnSelect;


    @FXML
    private ChoiceBox<String> cbSearchBy;
    private String[] arrSearch = {"ID", "Last Name", "First Name"};


    @FXML
    private TableView<Tutors> tvTutors;
    @FXML
    private TableColumn<Tutors, String> colFirstname;
    @FXML
    private TableColumn<Tutors, String> colGender;
    @FXML
    private TableColumn<Tutors, Integer> colId;
    @FXML
    private TableColumn<Tutors, String> colLastname;
    @FXML
    private TableColumn<Tutors, String> colSubject;
    @FXML
    private TextField tfSearchBox;

    private String selectedSubject = TuteeController.selectedSubject;

    static int tutor_id;


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

        public void showTutors(){
        ObservableList<Tutors> list = getTutorsList();
         
        colId.setCellValueFactory(new PropertyValueFactory<Tutors, Integer>("tutorID"));
        colLastname.setCellValueFactory(new PropertyValueFactory<Tutors, String>("lastName"));
        colFirstname.setCellValueFactory(new PropertyValueFactory<Tutors, String>("firstName"));
        colSubject.setCellValueFactory(new PropertyValueFactory<Tutors, String>("subject"));
        colGender.setCellValueFactory(new PropertyValueFactory<Tutors, String>("gender"));
    
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
    private void handleMouseAction(MouseEvent event) {
        Tutors tutor = tvTutors.getSelectionModel().getSelectedItem();// Get the selected tutor
        if (tutor != null) { // Check if a tutor is selected
            tutor_id = tutor.getTutorID();
            System.out.println("Clicked on row. ID: " + tutor_id);
        } else {
            System.out.println("No tutor selected.");
        }
    }
    

    private void filterTutorsBySubject() {
        ObservableList<Tutors> filteredList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT * FROM tutors WHERE ";
    
        // Define the condition based on the selected subject
        switch (selectedSubject) {
            case "Reading and Math":
                query += "Reading_And_Math = 1";
                break;
            case "Science Class":
                query += "Science_Class = 1";
                break;
            case "Academic Assistance":
                query += "Academic_Assistance = 1";
                break;
            case "English Conversation Class":
                query += "English_Conversation_Class = 1";
                break;
            default:
                // If no subject is selected, return all tutors
                query = "SELECT * FROM tutors";
                break;
        }
    
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Get subject values from the database
                    int readingAndMath = rs.getInt("Reading_And_Math");
                    int scienceClass = rs.getInt("Science_Class");
                    int academicAssistance = rs.getInt("Academic_Assistance");
                    int englishConversationClass = rs.getInt("English_Conversation_Class");
    
                    // Concatenate all subjects into a single string
                    StringBuilder subjects = new StringBuilder();
                    if (readingAndMath == 1) {
                        subjects.append("Reading and Math \n");
                    }
                    if (scienceClass == 1) {
                        subjects.append("Science Class \n");
                    }
                    if (academicAssistance == 1) {
                        subjects.append("Academic Assistance \n");
                    }
                    if (englishConversationClass == 1) {
                        subjects.append("English Conversation Class \n");
                    }
    
                    // Remove the trailing comma and space if the subjects string is not empty
                    String allSubjects = subjects.toString();
                    if (!allSubjects.isEmpty()) {
                        allSubjects = allSubjects.substring(0, allSubjects.length() - 1);
                    }
    
                    // Create a tutors object with all subjects
                    Tutors tutors = new Tutors(
                            rs.getInt("tutor_id"),
                            rs.getString("lastName"),
                            rs.getString("firstName"),
                            allSubjects,
                            rs.getString("gender"));
                    filteredList.add(tutors);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tvTutors.setItems(filteredList);
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
        }
    
        ObservableList<Tutors> filteredList = FXCollections.observableArrayList();
    
        if (category != null && !searchText.isEmpty()) {
            Connection conn = getConnection();
            String query = "SELECT * FROM tutors WHERE " + category + " LIKE ? AND subject LIKE ?";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, "%" + searchText + "%");
                ps.setString(2, "%" + selectedSubject + "%"); // Filter by selected subject
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Tutors tutors = new Tutors(rs.getInt("tutor_id"),
                                rs.getString("lastName"),
                                rs.getString("firstName"),
                                rs.getString("subject"),
                                rs.getString("gender"));
                        filteredList.add(tutors);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            filterTutorsBySubject(); // Apply subject filter
        }
        tvTutors.setItems(filteredList);
    }
    
    // Method to close the window
    public void closeWindow() {
        // Get the reference to the stage
        Stage stage = (Stage) cbSearchBy.getScene().getWindow();
        stage.close(); // Close the stage
    }

@FXML
private void selectTutor(ActionEvent event) {

    if(tutor_id != 0){

    closeWindow();
    switchToSched();
    }
}

    public void switchToSched() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SetDateScene.fxml"));
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbSearchBy.getItems().addAll(arrSearch);
        cbSearchBy.setValue(arrSearch[0]); // Set default value
        showTutors();
    
        filterTutorsBySubject();
        }
    }
    

