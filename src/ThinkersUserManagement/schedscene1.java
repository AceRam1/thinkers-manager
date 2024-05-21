package ThinkersUserManagement;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.fxml.Initializable;


public class schedscene1 implements Initializable {

    @FXML
    private Button btnEnroll;
    @FXML
    private Button btnSearch;

    @FXML
    private TableColumn<Tutees, String> colCurrent_subject;
    @FXML
    private TableColumn<schedules, String> colEnd;
    @FXML
    private TableColumn<schedules, String> colStart;
    @FXML
    private TableColumn<schedules, String> colSubjects;
    @FXML
    private TableColumn<Tutees, Integer> colTutee_id;
    @FXML
    private TableColumn<Tutees, String> colTutee_name;

    @FXML
    private TableView<schedules> tvSubjects;
    @FXML
    private TableView<Tutees> tvTutees;
    
    @FXML
    private TextField tfSearch;
    @FXML
    private TextField tfAge;
    @FXML
    private TextField tfGender;
    @FXML
    private TextField tfID;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfNickname;


    @FXML
    private ChoiceBox<String> cbSubSort;
    private String[] arrSubSort = {"", "Reading and Math", "Science Class", "Academic Assistance", "English Conversation Class"};

    @FXML
    private ChoiceBox<String> cbTypeSort;
    private String[] arrTypeSort = {"Name", "ID"};

    static int schedTutee_id;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showTutees();
        showSubjects();
    
        cbSubSort.getItems().clear(); // Clear existing items
        cbSubSort.getItems().addAll(arrSubSort);
        cbSubSort.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            filterBySubject(); // Apply filtering when a subject is selected
        });
    
        cbTypeSort.getItems().addAll(arrTypeSort);
        cbTypeSort.setValue(arrTypeSort[0]);
    
    }
    
        //showuser method to show the values of the user class within the observablelist
        public void showTutees(){
        ObservableList<Tutees> list = getTuteesList();
         
        colTutee_id.setCellValueFactory(new PropertyValueFactory<Tutees, Integer>("tuteeId"));
        colTutee_name.setCellValueFactory(new PropertyValueFactory<Tutees, String>("name"));
        colCurrent_subject.setCellValueFactory(new PropertyValueFactory<Tutees, String>("subjectTaking"));

        tvTutees.setItems(list);
    }

    public void showSubjects() {
        ObservableList<schedules> list = getSubjectsList();
    

        colSubjects.setCellValueFactory(new PropertyValueFactory<>("subjects"));
        colStart.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        

    
        // Set the items to the TableView
        tvSubjects.setItems(list);
    
        // Set the first and last dates to the text fields
        if (!list.isEmpty()) {
            // Assuming that the schedules are ordered by date, you can get the first and last items
            schedules firstSchedule = list.get(0);
            schedules lastSchedule = list.get(list.size() - 1);
    
            // // Set the start date to the TextField
            // tfStartDate.setText(firstSchedule.getDay() + "/" + firstSchedule.getMonth() + "/" + firstSchedule.getYear());
    
            // // Set the end date to the TextField
            // tfEndDate.setText(lastSchedule.getDay() + "/" + lastSchedule.getMonth() + "/" + lastSchedule.getYear());
        }
    }

    //getting the tuteeList
public ObservableList<Tutees> getTuteesList(){
    ObservableList<Tutees> tuteeList = FXCollections.observableArrayList();//instantiation
    Connection conn = getConnection();//setting up connection
    String query = "SELECT * FROM tutees";//setting up the query
    Statement st;//setting up the statement
    ResultSet rs;//setting up the resultset
    
    try{
        st = conn.createStatement();
        rs = st.executeQuery(query);
        Tutees tutees;
        while(rs.next()){//while the result set has next value keep on reading
            // Get subject values from the database
            int readingAndMath = rs.getInt("Reading_And_Math");
            int scienceClass = rs.getInt("Science_Class");
            int academicAssistance = rs.getInt("Academic_Assistance");
            int englishConversationClass = rs.getInt("English_Conversation_Class");

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

            // Create Tutees object with concatenated subjects
            tutees = new Tutees(
                rs.getInt("tutee_id"), 
                rs.getString("name"),
                rs.getString("nickname"),
                rs.getString("gender"), 
                rs.getInt("age"), 
                rs.getString("address"), 
                rs.getString("contact_information"), 
                subjects.toString(),
                rs.getString("time_slot")
            );        
            tuteeList.add(tutees);//adding the values to the userlist obj
        }
    } catch(Exception e){
        e.printStackTrace();
    }
    return tuteeList;//returns the list of the users
}

public ObservableList<schedules> getSubjectsList() {
    ObservableList<schedules> tuteeList = FXCollections.observableArrayList();
    Connection conn = getConnection();
    String query = "SELECT * FROM schedules WHERE tutee_id = ? AND (session_count = 1 OR session_count = 22)";

    try (PreparedStatement ps = conn.prepareStatement(query)) {
        ps.setInt(1, TuteeController.ID);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String startDate = null;
                String endDate = null;
                String subjects = rs.getString("subject");

                // Determine start and end dates based on session count
                if (rs.getInt("session_count") == 1) {
                    startDate = rs.getString("day") + "/" + rs.getString("month") + "/" + rs.getString("year");
                } else if (rs.getInt("session_count") == 22) {
                    endDate = rs.getString("day") + "/" + rs.getString("month") + "/" + rs.getString("year");
                }

                schedules schedule = new schedules(subjects, startDate, endDate);
                tuteeList.add(schedule);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle the exception according to your application's requirements
    } finally {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    return tuteeList;
}

        @FXML
        private void handleMouseAction(MouseEvent event) {
            Tutees tutee = tvTutees.getSelectionModel().getSelectedItem();//setting up the user getting its values
            tfID.setText("" + tutee.getTuteeId());//setting its value to the textfield
            tfName.setText("" + tutee.getName());//setting its value to the textfield
            tfNickname.setText("" + tutee.getNickname());//setting its value to the textfield
            tfAge.setText("" + tutee.getAge());//setting its value to the textfield
            tfGender.setText("" + tutee.getGender());//setting its value to the textfield

            schedTutee_id = tutee.getTuteeId();
            TuteeController.ID = tutee.getTuteeId();

            showSubjects();
        }

        public void setTutor(){//add an account to the database
            System.out.println(schedTutee_id);
        if(schedTutee_id == 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);//alerting the user
            alert.setTitle("Error");
            alert.setHeaderText("Select a Tutee First");
            alert.showAndWait();            
        }else{
            TuteeController.ID = schedTutee_id;
            switchToSelect();
        }
        }

        @FXML
        private void searchTutees(ActionEvent event) {
            String searchValue = tfSearch.getText().trim();
            String selectedType = cbTypeSort.getValue();
            String selectedSubject = cbSubSort.getValue();
        
            ObservableList<Tutees> filteredList = FXCollections.observableArrayList();
            ObservableList<Tutees> originalList = getTuteesList();
        
            for (Tutees tutee : originalList) {
                boolean matchesSubject = (selectedSubject == null || selectedSubject.isEmpty() || tutee.getSubjectTaking().contains(selectedSubject));
                boolean matchesSearch = (searchValue.isEmpty() || (selectedType.equals("Name") && tutee.getName().toLowerCase().contains(searchValue.toLowerCase())) || (selectedType.equals("ID") && String.valueOf(tutee.getTuteeId()).contains(searchValue)));
                
                if (matchesSubject && matchesSearch) {
                    filteredList.add(tutee);
                }
            }
        
            // Set the filtered list to the TableView
            tvTutees.setItems(filteredList);
        }

        @FXML
        private void filterBySubject() {
            String selectedSubject = cbSubSort.getValue();
            ObservableList<Tutees> filteredList = FXCollections.observableArrayList();
        
            if (selectedSubject != null && !selectedSubject.isEmpty()) {
                ObservableList<Tutees> originalList = getTuteesList();
        
                for (Tutees tutee : originalList) {
                    System.out.println(tutee.getSubjectTaking()+", "+ selectedSubject);
                    if (tutee.getSubjectTaking().contains(selectedSubject)) {
                        filteredList.add(tutee);
                    }
                }
        
                // Set the filtered list to the TableView
                tvTutees.setItems(filteredList);
            } else {
                // If no subject is selected, show all tutees
                showTutees();
            }
        }
    
    public void switchToSelect() {
    try {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("select-subject.fxml"));
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
    
    }


