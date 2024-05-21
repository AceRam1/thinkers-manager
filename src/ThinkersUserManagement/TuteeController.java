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
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.Statement;
import java.time.LocalDate;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class TuteeController implements Initializable {
    
    
    //initialization
    private Stage stage;
    
    private Scene scene;
    
    private Parent root;

    @FXML
    private Button addButton;
    @FXML
    private Button backButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button resetbutton;
    @FXML
    private Button updateButton;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnSched;
    @FXML
    private Button btnRefresh;

    @FXML
    private ChoiceBox<String> cbGender;
    
    private String[] arrgender = {"male", "female"};

    @FXML
    private ChoiceBox<String> cbSearchBy;
    private String[] arrSearch = {"ID", "Name", "Subject"};

//    @FXML
//    private ChoiceBox<?> cbSubject;

    @FXML
    private TableColumn<Tutees, String> colAddress;
    @FXML
    private TableColumn<Tutees, Integer> colAge;
    @FXML
    private TableColumn<Tutees, String> colContactInformation;
    @FXML
    private TableColumn<Tutees, String> colGender;
    @FXML
    private TableColumn<Tutees, Integer> colID;
    @FXML
    private TableColumn<Tutees, String> colName;
    @FXML
    private TableColumn<Tutees, String> colNickname;
    @FXML
    private TableColumn<Tutees, String> colSubjectTaking;


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

    @FXML
    private TextField tfAddress;
    @FXML
    private TextField tfAge;
    @FXML
    private TextField tfContactInformation;
    @FXML
    private TextField tfID;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfNickname;
    @FXML
    private TextField tfSearchBox;

    @FXML
    private TableView<Tutees> tvTutees;

    @FXML
    private SelectTutorController selectTutorController; 

    static String subject;
    static String timeSlot; 
    static int ID;
    static String name;
    static String nickname;
    static String gender;
    static int age;
    static String address;
    static String contact;

    // Declare a new variable to store the selected subject
    static String selectedSubject = "Reading and Math";

    static String status = null;

    
    @Override
    public void initialize(URL url, ResourceBundle rb){

        updateEnrollmentStatusForAllTutees();
        showTutees();
        
        cbGender.getItems().addAll(arrgender);

        setupButtonHoverEffect(backButton);
        setupButtonHoverEffect(updateButton);
        setupButtonHoverEffect(resetbutton);
        setupButtonHoverEffect(btnAdd);
        setupButtonHoverEffect2(btnSched);


        cbSearchBy.getItems().addAll(arrSearch);
        cbSearchBy.setValue(arrSearch[0]); // Set default value

    };
    
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

        //button hover effects
        private void setupButtonHoverEffect2(Button button) {
        
            button.setStyle("-fx-background-color: #fa4e2f; -fx-text-fill: #ffffff;");//standard color
            button.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
                button.setStyle("-fx-background-color: #ffffff; -fx-text-fill: #2b2b2b;");//color on mouse hover
            });
            button.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
                button.setStyle("-fx-background-color: #fa4e2f; -fx-text-fill: #ffffff;");// color on mouse 
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

    @FXML
    private void handleRadioButtonClick(MouseEvent event) {
        RadioButton selectedRadioButton = (RadioButton) event.getSource();
        selectedSubject = selectedRadioButton.getText();
    }

    @FXML
    private void addUser(ActionEvent event) {
        if (isDuplicateID(tfID.getText())) {
            System.out.println("reacehd1");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Duplicate ID");
            alert.setHeaderText("Duplicate ID, ID should be Unique");
            alert.showAndWait();
            return;
        }
        if (
            tfName.getText().isBlank() || 
            tfNickname.getText().isBlank() || 
            tfAge.getText().isBlank() || 
            tfAddress.getText().isBlank() || 
            tfContactInformation.getText().isBlank() || 
            cbGender.getValue() == null 
        ) { // authentication for blank fields
            System.out.println("reached2");
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION); // alerting the user
            alert2.setTitle("Error");
            alert2.setHeaderText("Fill up the Form or Select a Student in the tableview First");
            alert2.showAndWait();            
            return;
        } else {
            Tutees tutee = tvTutees.getSelectionModel().getSelectedItem(); // Get the selected Tutees object from the table
            System.out.println("reached3");
    
            // Retrieve values from the text fields and set them to the variables
            ID = Integer.parseInt(tfID.getText());
            name = tfName.getText();
            nickname = tfNickname.getText();
            gender = cbGender.getValue(); // Get the selected gender from the choice box
            age = Integer.parseInt(tfAge.getText());
            address = tfAddress.getText();
            contact = tfContactInformation.getText();
    
            status = "new";
            System.out.println("reached5");

                    // Remove the trailing comma and space if the subject is not empty
                    String query2 = "INSERT INTO tutees (tutee_id, name, nickname, gender, age, address, contact_information) VALUES ('"
                    + ID + "','"
                    + name + "','"
                    + nickname + "','"
                    + gender + "',"
                    + age + ",'"
                    + address + "','"
                    + contact + "')"; 
        
                executeQuery(query2);
                showTutees();
    
        }
    }
    
    //showuser method to show the values of the user class within the observablelist
    public void showTutees(){
        ObservableList<Tutees> list = getTuteesList();
         
        colID.setCellValueFactory(new PropertyValueFactory<Tutees, Integer>("tuteeId"));
        colName.setCellValueFactory(new PropertyValueFactory<Tutees, String>("name"));
        colNickname.setCellValueFactory(new PropertyValueFactory<Tutees, String>("nickname"));
        colSubjectTaking.setCellValueFactory(new PropertyValueFactory<Tutees, String>("subjectTaking"));
        colGender.setCellValueFactory(new PropertyValueFactory<Tutees, String>("gender"));
        colAge.setCellValueFactory(new PropertyValueFactory<Tutees, Integer>("age"));
        colAddress.setCellValueFactory(new PropertyValueFactory<Tutees, String>("address"));
        colContactInformation.setCellValueFactory(new PropertyValueFactory<Tutees, String>("contactInformation"));

        tvTutees.setItems(list);
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

public void updateEnrollmentStatusForAllTutees() {
    String query = "SELECT DISTINCT tutee_id FROM tutees";

    try (Connection conn = getConnection();
         Statement statement = conn.createStatement();
         ResultSet resultSet = statement.executeQuery(query)) {

        while (resultSet.next()) {
            int tuteeId = resultSet.getInt("tutee_id");
            updateEnrollmentStatus(tuteeId);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}

public void updateEnrollmentStatus(int tuteeId) {
    LocalDate today = LocalDate.now();

    // SQL query to retrieve future schedules for the given tutee
    String query = "SELECT DISTINCT subject FROM schedules " +
                   "WHERE tutee_id = ? " +
                   "AND ((year > ?) " +
                   "OR (year = ? AND month > ?) " +
                   "OR (year = ? AND month = ? AND day >= ?))";
                   
    try (Connection conn = getConnection();
         PreparedStatement ps = conn.prepareStatement(query)) {
        
        ps.setInt(1, tuteeId);
        ps.setInt(2, today.getYear());
        ps.setInt(3, today.getYear());
        ps.setString(4, String.valueOf(today.getMonthValue()));
        ps.setInt(5, today.getYear());
        ps.setString(6, String.valueOf(today.getMonthValue()));
        ps.setInt(7, today.getDayOfMonth());
        
        ResultSet rs = ps.executeQuery();

        int readingAndMath = 0;
        int scienceClass = 0;
        int academicAssistance = 0;
        int englishConversationClass = 0;

        System.out.println("subject zero");

        while (rs.next()) {
            String subject = rs.getString("subject");
            System.out.println("Distinct Subject: " + subject);
            if (subject != null) {
                if (subject.equals("Reading and Math")) {
                    readingAndMath = 1;
                    System.out.println("exec1");
                } else if (subject.equals("Science Class")) {
                    scienceClass = 1;
                    System.out.println("exec2");
                } else if (subject.equals("Academic Assistance")) {
                    academicAssistance = 1;
                    System.out.println("exec3");
                } else if (subject.equals("English Conversation Class")) {
                    englishConversationClass = 1;
                    System.out.println("exec4");
                }
            } else {
                // Handle NULL values if necessary
                System.out.println("Subject is NULL for this row.");
            }
        }

        // Update the enrollment status in the tutees table
        String updateQuery = "UPDATE tutees SET Reading_And_Math = ?, Science_Class = ?, " +
                             "Academic_Assistance = ?, English_Conversation_Class = ? " +
                             "WHERE tutee_id = ?";
        
        try (PreparedStatement updatePS = conn.prepareStatement(updateQuery)) {
            updatePS.setInt(1, readingAndMath);
            updatePS.setInt(2, scienceClass);
            updatePS.setInt(3, academicAssistance);
            updatePS.setInt(4, englishConversationClass);
            updatePS.setInt(5, tuteeId);
            
            int rowsAffected = updatePS.executeUpdate();
            System.out.println("Rows updated: " + rowsAffected);
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    
    @FXML
    private Label warning;//initialized the warning label here since it somewhat doesnt get read if it's on top with the others
    
    private ObservableList<Tutors> tutorList;//setting up an observable list to be used in add account
    
    public void setTutor(){//add an account to the database

        
        if(     tfName.getText().isBlank() || 
                tfNickname.getText().isBlank() || 
                tfAge.getText().isBlank() || 
                tfAddress.getText().isBlank() || 
                tfContactInformation.getText().isBlank() || 
                cbGender.getValue() == null 
        ){//authentication for blank fields
            Alert alert = new Alert(Alert.AlertType.INFORMATION);//alerting the user
            alert.setTitle("Error");
            alert.setHeaderText("Fill up the Form or Select a Student in the tableview First");
            alert.showAndWait();            
        
        }else{
            
            Tutees tutee = tvTutees.getSelectionModel().getSelectedItem();//setting up the user getting its values
            tfID.setText("" + tutee.getTuteeId());//setting its value to the textfield
            tfName.setText("" + tutee.getName());//setting its value to the textfield
            tfNickname.setText("" + tutee.getNickname());//setting its value to the textfield
            tfAge.setText("" + tutee.getAge());//setting its value to the textfield
            tfAddress.setText("" + tutee.getAddress());//setting its value to the textfield
            tfContactInformation.setText("" + tutee.getContactInformation());//setting its value to the textfield
            cbGender.setValue("" + tutee.getGender());//setting its value to the textfield
    
            subject = tutee.getSubjectTaking();
            ID = tutee.getTuteeId();
            name = tutee.getName();
            nickname = tutee.getNickname();
            gender = tutee.getGender();
            age = tutee.getAge();
            address = tutee.getAddress();
            contact = tutee.getContactInformation();

            switchToSelect();

        }}
     
    private void resetFields(){//method for reseting fields
        tfName.clear();
        tfNickname.clear();
        tfAge.clear();
        tfAddress.clear();
        cbGender.getSelectionModel().clearSelection();
        tfContactInformation.clear();
        subjReadingAndMath.setSelected(false);
        subjScienceClass.setSelected(false);
        subjAcademicAssistance.setSelected(false);
        subjEnglishConversationClass.setSelected(false);

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
    
    public void updateRecord() {
        String gender = cbGender.getValue();

        if (    tfName.getText().isBlank() ||
                tfNickname.getText().isBlank() ||
                tfAge.getText().isBlank() ||
                tfAddress.getText().isBlank() ||
                tfContactInformation.getText().isBlank() ||
                cbGender.getValue() == null) {//authentication for blank fields

            warning.setText("Please fill in every data!");
        } else {
            String query = "UPDATE tutees SET "
            + "name = '" + tfName.getText() + "', "
            + "nickname = '" + tfNickname.getText() + "', "
            + "gender = '" + gender + "', "
            + "age = " + tfAge.getText() + ", "
            + "address = '" + tfAddress.getText() + "', "
            + "contact_information = '" + tfContactInformation.getText() + "' "
            + "WHERE tutee_id = " + tfID.getText();

            executeQuery(query);
            showTutees();
            resetFields();
            warning.setText("");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Account Update");
            alert.setHeaderText("Account Updated Successfully!");
            alert.showAndWait();
        }
    }

    public void deleteUser() {
        // Check if any required fields are blank
        if (tfName.getText().isBlank() ||
            tfNickname.getText().isBlank() ||
            tfAge.getText().isBlank() ||
            tfAddress.getText().isBlank() ||
            tfContactInformation.getText().isBlank() ||
            cbGender.getValue() == null) {
            
            warning.setText("Please fill in every data!");
            return; // Exit the method early if any required fields are blank
        }
        
        // Check if the tutee has schedules
        String queryToCheckSchedules = "SELECT * FROM schedules WHERE tutee_id = ?";
        boolean tuteeHasSchedules = false;
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(queryToCheckSchedules)) {
            
            ps.setInt(1, Integer.parseInt(tfID.getText()));
            try (ResultSet rs = ps.executeQuery()) {
                tuteeHasSchedules = rs.next(); // If the result set has at least one row, the tutee has schedules
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQL exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Database Error");
            alert.setContentText("An error occurred while checking tutee schedules.");
            alert.showAndWait();
            return; // Exit the method due to the database error
        }
        
        // If the tutee has schedules, display an error message and return
        if (tuteeHasSchedules) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot Delete Tutee");
            alert.setContentText("This tutee has existing schedules. Delete the schedules first before deleting the tutee.");
            alert.showAndWait();
            return; // Exit the method
        }
        
        // The tutee does not have schedules, proceed with deletion
        String queryToDelete = "DELETE FROM tutees WHERE tutee_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(queryToDelete)) {
            
            ps.setInt(1, Integer.parseInt(tfID.getText()));
            int rowsAffected = ps.executeUpdate(); // Attempt to delete the tutee
            
            if (rowsAffected > 0) {
                // The tutee was deleted successfully
                showTutees();
                resetFields();
                warning.setText("");
    
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Account Deletion");
                successAlert.setHeaderText("Tutee Deleted Successfully.");
                successAlert.showAndWait();
            } else {
                // The tutee was not deleted
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Failed to Delete Tutee");
                errorAlert.setContentText("The specified tutee ID does not exist.");
                errorAlert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQL exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Database Error");
            alert.setContentText("An error occurred while deleting the tutee.");
            alert.showAndWait();
        }
    }

    private boolean executeQueryReturnsResult(String queryToCheck) {
        boolean tuteeExists = false;
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/thinkersdb", "admin", "admin");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(queryToCheck)) {
    
            // Check if the result set contains any rows
            tuteeExists = resultSet.next();
        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace(); // or log the exception
        }
        return tuteeExists = true; 
    }

    @FXML
    private void searchTutees() {
        String searchBy = cbSearchBy.getValue();
        String searchText = tfSearchBox.getText();
        String category = null;
    
        if ("ID".equals(searchBy)) {
            category = "tutee_id";
        } else if ("Name".equals(searchBy)) {
            category = "name"; // Assuming you have a lastName column in your tutees table
        } else if ("First Name".equals(searchBy)) {
            category = "firstName"; // Assuming you have a firstName column in your tutees table
        }
    
        ObservableList<Tutees> filteredList = FXCollections.observableArrayList();
    
        if (category != null && !searchText.isEmpty()) {
            Connection conn = getConnection();
            String query = "SELECT * FROM tutees WHERE " + category + " LIKE ?";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, "%" + searchText + "%");
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        // Concatenate subjects with a value of 1
                        StringBuilder subjects = new StringBuilder();
                        if (rs.getInt("Reading_And_Math") == 1) {
                            subjects.append("Reading and Math\n");
                        }
                        if (rs.getInt("Science_Class") == 1) {
                            subjects.append("Science Class\n");
                        }
                        if (rs.getInt("Academic_Assistance") == 1) {
                            subjects.append("Academic Assistance\n");
                        }
                        if (rs.getInt("English_Conversation_Class") == 1) {
                            subjects.append("English Conversation Class\n");
                        }
    
                        Tutees tutee = new Tutees(
                                rs.getInt("tutee_id"),
                                rs.getString("name"),
                                rs.getString("nickname"),
                                rs.getString("gender"),
                                rs.getInt("age"),
                                rs.getString("address"),
                                rs.getString("contact_information"),
                                subjects.toString(),
                                rs.getString("time_slot"));
                        filteredList.add(tutee);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
    
        } else {
            // If no search category or text is specified, retrieve all tutees
            Connection conn = getConnection();
            String query = "SELECT * FROM tutees";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        // Concatenate subjects with a value of 1
                        StringBuilder subjects = new StringBuilder();
                        if (rs.getInt("Reading_And_Math") == 1) {
                            subjects.append("Reading and Math\n");
                        }
                        if (rs.getInt("Science_Class") == 1) {
                            subjects.append("Science Class\n");
                        }
                        if (rs.getInt("Academic_Assistance") == 1) {
                            subjects.append("Academic Assistance\n");
                        }
                        if (rs.getInt("English_Conversation_Class") == 1) {
                            subjects.append("English Conversation Class\n");
                        }
    
                        Tutees tutee = new Tutees(
                                rs.getInt("tutee_id"),
                                rs.getString("name"),
                                rs.getString("nickname"),
                                rs.getString("gender"),
                                rs.getInt("age"),
                                rs.getString("address"),
                                rs.getString("contact_information"),
                                subjects.toString(),
                                rs.getString("time_slot"));
                        filteredList.add(tutee);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // Set the filtered list to the TableView
        tvTutees.setItems(filteredList);
    }
    

    
        @FXML
        private void handleMouseAction(MouseEvent event) {
            Tutees tutee = tvTutees.getSelectionModel().getSelectedItem();//setting up the user getting its values
            tfID.setText("" + tutee.getTuteeId());//setting its value to the textfield
            tfName.setText("" + tutee.getName());//setting its value to the textfield
            tfNickname.setText("" + tutee.getNickname());//setting its value to the textfield
            tfAge.setText("" + tutee.getAge());//setting its value to the textfield
            tfAddress.setText("" + tutee.getAddress());//setting its value to the textfield
            tfContactInformation.setText("" + tutee.getContactInformation());//setting its value to the textfield
            cbGender.setValue("" + tutee.getGender());//setting its value to the textfield
        
            // Set subject based on the tutee's subjectTaking
        // Set subject based on the tutee's subjectTaking
        String subjectTaking = tutee.getSubjectTaking();
        switch (subjectTaking) {
            case "Academic Assistance":
                radiosubject.selectToggle(subjAcademicAssistance);
                subject = "Academic Assistance"; // Update the subject variable
                break;
            case "English Conversation Class":
                radiosubject.selectToggle(subjEnglishConversationClass);
                subject = "English Conversation Class"; // Update the subject variable
                break;
            case "Reading and Math":
                radiosubject.selectToggle(subjReadingAndMath);
                subject = "Reading and Math"; // Update the subject variable
                break;
            case "Science Class":
                radiosubject.selectToggle(subjScienceClass);
                subject = "Science Class"; // Update the subject variable
                break;
            default:
                // Handle default case or leave radio buttons unselected
                break;
        }
        
            ID = tutee.getTuteeId();
            name = tutee.getName();
            nickname = tutee.getNickname();
            gender = tutee.getGender();
            age = tutee.getAge();
            address = tutee.getAddress();
            contact = tutee.getContactInformation();
        }

        // Method to set the selected subject based on the subject chosen in the radio buttons
    private void setSelectedSubject() {
    RadioButton selectedRadioButton = (RadioButton) radiosubject.getSelectedToggle();
    if (selectedRadioButton != null) {
        selectedSubject = selectedRadioButton.getText(); // Store the text of the selected radio button
    }
    }

        private boolean isDuplicateID(String id) {
        String query = "SELECT COUNT(*) FROM tutees WHERE tutee_id = '" + id + "'";
        Connection conn = getConnection();
        Statement st;
        ResultSet rs;
    
        try {
            st = conn.createStatement();
            rs = st.executeQuery(query);
            rs.next();
            int count = rs.getInt(1);
    
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    public void refresh(ActionEvent event){
        updateEnrollmentStatusForAllTutees();
        showTutees();
    }

    //Switch Scenes

    @FXML
    public void switchToStudentList(ActionEvent event) {
    try {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("student-list.fxml"));
    Parent root = loader.load();

    // Create a new stage
    Stage tuteeStage = new Stage();
    tuteeStage.initModality(Modality.APPLICATION_MODAL);
    tuteeStage.setTitle("Assignment Manager");
    tuteeStage.setScene(new Scene(root));
    // Show the stage
    tuteeStage.show();

} catch (IOException e) {
    e.printStackTrace();
}}

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

@FXML
public void switchToSched(ActionEvent event) {
    if (ID != 0) { // Check if ID is not null or 0
        if (isTuteeExists(ID)) { // Check if the tutee with the given ID exists in the database
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("sched-view.fxml"));
                Parent root = loader.load();

                // Create a new stage
                Stage scheduleStage = new Stage();
                scheduleStage.initModality(Modality.APPLICATION_MODAL);
                scheduleStage.setTitle("Schedule View");
                scheduleStage.setScene(new Scene(root));

                // Get the controller instance
                ScheduleController controller = loader.getController();
                controller.initialize(null, null); // Initialize the controller

                // Show the stage
                scheduleStage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Tutee with ID " + ID + " does not exist in the database.");
        }
    } else {
        System.out.println("ID is null or does not exist.");
    }
}

// Method to check if the tutee with the given ID exists in the database
private boolean isTuteeExists(int tuteeID) {
    String query = "SELECT COUNT(*) FROM tutees WHERE tutee_id = ?";
    Connection conn = getConnection();
    PreparedStatement ps = null;
    ResultSet rs = null;
    boolean exists = false;

    try {
        ps = conn.prepareStatement(query);
        ps.setInt(1, tuteeID);
        rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);

        exists = count > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    return exists;
}
    
    public void reset(){        
        tfID.clear();
        tfName.clear();
        tfNickname.clear();
        tfAge.clear();
        tfAddress.clear();
        cbGender.getSelectionModel().clearSelection();
        tfContactInformation.clear();
        subjReadingAndMath.setSelected(false);
        subjScienceClass.setSelected(false);
        subjAcademicAssistance.setSelected(false);
        subjEnglishConversationClass.setSelected(false);
    }
}//end of class
