package ThinkersUserManagement; // Add this line with the correct package name

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AssignController{

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnClose;

    @FXML
    private Button btnList;

    @FXML
    private Button btnView;

    @FXML
    private Button btnRefresh;


    
    @FXML
    private TableView<Tutees> tv10to11;
    @FXML
    private TableColumn<Tutees, String> tutees1;

    @FXML
    private TableView<Tutees> tv1to2;
    @FXML
    private TableColumn<Tutees, String> tutees2;

    @FXML
    private TableView<Tutees> tv2to3;
    @FXML
    private TableColumn<Tutees, String> tutees3;

    @FXML
    private TableView<Tutees> tv3to4;
    @FXML
    private TableColumn<Tutees, String> tutees4;

    @FXML
    private TableView<Tutees> tv4to5;
    @FXML
    private TableColumn<Tutees, String> tutees5;

    static int ID;
    static String name;
    static String nickname;
    static String gender;
    static int age;
    static String address;
    static String contact;
    static String subject;
    static String timeSlot;

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

    @FXML
    private void handleMouseAction(MouseEvent event) {
        if (event.getClickCount() == 1) {
            TableView<Tutees> selectedTableView = (TableView<Tutees>) event.getSource();
            Tutees selectedTutees = selectedTableView.getSelectionModel().getSelectedItem();

    
            if (selectedTutees != null) {
                // Use the existing constructor to create an instance of Tutees
                Tutees tuteesData = new Tutees(
                        selectedTutees.getTuteeId(),
                        selectedTutees.getName(),
                        selectedTutees.getNickname(),
                        selectedTutees.getGender(),
                        selectedTutees.getAge(),
                        selectedTutees.getAddress(),
                        selectedTutees.getContactInformation(),
                        selectedTutees.getSubjectTaking(),
                        selectedTutees.getTimeSlot()
                );
    
                // Now, you can use the tuteesData object as needed.
                // You may want to pass it to another method, display it, or perform other actions.
                System.out.println("Clicked on row. Data: " + tuteesData);

                ID = selectedTutees.getTuteeId();
                name = selectedTutees.getName();
                nickname = selectedTutees.getNickname();
                gender = selectedTutees.getGender();
                age = selectedTutees.getAge();
                address = selectedTutees.getAddress();
                contact = selectedTutees.getContactInformation();
                subject = selectedTutees.getSubjectTaking();
                timeSlot = selectedTutees.getTimeSlot();

                
            }
        }
    }

    @FXML
    private void initialize() {
    // Initialize your TableView and TableColumn for 10am-11am
    tutees1.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
    populateTable("10am_11am", tv10to11);

    // Initialize your TableView and TableColumn for 1pm-2pm
    tutees2.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
    populateTable("1pm_2pm", tv1to2);

    // Initialize your TableView and TableColumn for 2pm-3pm
    tutees3.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
    populateTable("2pm_3pm", tv2to3);

    // Initialize your TableView and TableColumn for 3pm-4pm
    tutees4.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
    populateTable("3pm_4pm", tv3to4);

    // Initialize your TableView and TableColumn for 4pm-5pm
    tutees5.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
    populateTable("4pm_5pm", tv4to5);

    // Disable TableViews with only one row in accepted_students
    disableTableIfSingleRow("10am-11am", tv10to11);
    disableTableIfSingleRow("1pm-2pm", tv1to2);
    disableTableIfSingleRow("2pm-3pm", tv2to3);
    disableTableIfSingleRow("3pm-4pm", tv3to4);
    disableTableIfSingleRow("4pm-5pm", tv4to5);
}

    private void populateTable(String tableName, TableView<Tutees> tableView) {
        ObservableList<Tutees> tuteesList = FXCollections.observableArrayList();

        // Retrieve data from the database and add it to the list
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + tableName);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                // Assuming your database table has a column named 'name'
                int tuteeId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String nickname = resultSet.getString("nickname");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                String address = resultSet.getString("address");
                String contactInformation = resultSet.getString("contact_information");
                String subjectTaking = resultSet.getString("subject_taking");
                String timeSlot = resultSet.getString("time_slot");
                tuteesList.add(new Tutees(tuteeId, name, nickname, gender, age, address, contactInformation, subjectTaking, timeSlot));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableView.setItems(tuteesList);
    }

    @FXML
    public void switchToprofile(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tutee-profile.fxml"));
            Parent root = loader.load();
    
            // Access the controller of the tutee-profile scene
            TuteeProfileController tuteeProfileController = loader.getController();
    
            // Disable the "btnAddStudent" button in the tutee-profile scene
            tuteeProfileController.disableAddStudentButton(true);
    
            // Create a new stage
            Stage tuteeStage = new Stage();
            tuteeStage.initModality(Modality.APPLICATION_MODAL);
            tuteeStage.setTitle("Assignment Manager");
            tuteeStage.setScene(new Scene(root));
    
            // Make the stage not resizable
            tuteeStage.setResizable(false);
    
            // Show the stage
            tuteeStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    


@FXML
public void switchToAddProfile(ActionEvent event) {
try {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("tutee-profile.fxml"));
    Parent root = loader.load();

    // Create a new stage
    Stage tuteeStage = new Stage();
    tuteeStage.initModality(Modality.APPLICATION_MODAL);
    tuteeStage.setTitle("Assignment Manager");
    tuteeStage.setScene(new Scene(root));

    // Make the stage not resizable
    tuteeStage.setResizable(false);

    // Show the stage
    tuteeStage.show();
    
    // Close the current stage
    Stage currentStage = (Stage) btnList.getScene().getWindow();
    currentStage.close();

} catch (IOException e) {
    e.printStackTrace();
}
}

@FXML
private void refreshTableData(ActionEvent event) {
    // Refresh data for 10am-11am TableView
    populateTable("10am_11am", tv10to11);

    // Refresh data for 1pm-2pm TableView
    populateTable("1pm_2pm", tv1to2);

    // Refresh data for 2pm-3pm TableView
    populateTable("2pm_3pm", tv2to3);

    // Refresh data for 3pm-4pm TableView
    populateTable("3pm_4pm", tv3to4);

    // Refresh data for 4pm-5pm TableView
    populateTable("4pm_5pm", tv4to5);

    // Disable TableViews with only one row in accepted_students
    disableTableIfSingleRow("10am-11am", tv10to11);
    disableTableIfSingleRow("1pm-2pm", tv1to2);
    disableTableIfSingleRow("2pm-3pm", tv2to3);
    disableTableIfSingleRow("3pm-4pm", tv3to4);
    disableTableIfSingleRow("4pm-5pm", tv4to5);
}

private void disableTableIfSingleRow(String timeSlot, TableView<Tutees> tableView) {
    if (getRowCount("accepted_students", timeSlot) == 1) {
        tableView.setDisable(true);
    }
}

private int getRowCount(String tableName, String timeSlot) {
    int rowCount = 0;

    try (Connection connection = getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM " + tableName + " WHERE time_slot = ?");
    ) {
        preparedStatement.setString(1, timeSlot);

        System.out.println("Executing SQL query: " + preparedStatement.toString());


        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                rowCount = resultSet.getInt(1);

        System.out.println("Row count for " + timeSlot + ": " + rowCount);

            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return rowCount;
}

    @FXML
    public void close(ActionEvent event) {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }
    

}