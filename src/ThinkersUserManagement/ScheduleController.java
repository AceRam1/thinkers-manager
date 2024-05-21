package ThinkersUserManagement;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ScheduleController implements Initializable {

    @FXML
    private Button btnClose;

    @FXML
    private ChoiceBox<String> cbSubject;
    private String[] arrsubject = {"Reading and Math", "Science Class", "Academic Assistance", "English Conversation Class"};


    @FXML
    private TableColumn<schedules, String> colDay;

    @FXML
    private TableColumn<schedules, String> colMonth;

    @FXML
    private TableColumn<schedules, Integer> colSesCount;

    @FXML
    private TableColumn<schedules, String> colTime;

    @FXML
    private TableColumn<schedules, String> colTutorName;

    @FXML
    private TableColumn<schedules, String> colSubjects;

    @FXML
    private TableColumn<schedules, String> colYear;

    @FXML
    private TableColumn<schedules, String> colPStat;


    @FXML
    private TextField tfEndDate;

    @FXML
    private TextField tfStartDate;

    @FXML
    private TableView<schedules> tvSchedules;

    @FXML
    private Label lbName;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbSubject.getItems().clear(); // Clear existing items

        cbSubject.getItems().addAll(arrsubject);
        cbSubject.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            filterBySubject(); // Apply filtering when a subject is selected
        });
    
        setTuteeNameLabel();
        showschedules();
    }

    public void showschedules() {
        ObservableList<schedules> list = getTuteesList();
    
        colSesCount.setCellValueFactory(new PropertyValueFactory<>("scheduleId"));
        colTutorName.setCellValueFactory(new PropertyValueFactory<>("tutorName"));
        colSubjects.setCellValueFactory(new PropertyValueFactory<>("subjects"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colMonth.setCellValueFactory(new PropertyValueFactory<>("month"));
        colDay.setCellValueFactory(new PropertyValueFactory<>("day"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colPStat.setCellValueFactory(new PropertyValueFactory<>("paymentStatus")); // Add this line
    
        // Set the items to the TableView
        tvSchedules.setItems(list);
    
        // Set the first and last dates to the text fields
        if (!list.isEmpty()) {
            // Assuming that the schedules are ordered by date, you can get the first and last items
            schedules firstSchedule = list.get(0);
            schedules lastSchedule = list.get(list.size() - 1);
    
            // Set the start date to the TextField
            tfStartDate.setText(firstSchedule.getDay() + "/" + firstSchedule.getMonth() + "/" + firstSchedule.getYear());
    
            // Set the end date to the TextField
            tfEndDate.setText(lastSchedule.getDay() + "/" + lastSchedule.getMonth() + "/" + lastSchedule.getYear());
        }
    }

    public ObservableList<schedules> getTuteesList() {
        ObservableList<schedules> tuteeList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT * FROM schedules WHERE tutee_id = ?";
    
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, TuteeController.ID);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int tutorId = rs.getInt("tutor_id");
                    String tutorName = getTutorName(tutorId); // Fetch tutor's name
                    
                    schedules schedule;
                    if (tutorName != null && !tutorName.isEmpty()) {
                        schedule = new schedules(
                            rs.getInt("session_count"),
                            tutorName,
                            rs.getString("subject"),
                            rs.getString("time"),
                            rs.getString("day"),
                            rs.getString("month"),
                            rs.getString("year"),
                            rs.getString("payment_status")

                        );
                    } else {
                        schedule = new schedules(
                            rs.getInt("session_count"),
                            rs.getInt("tutor_id"), // Use getInt for tutor_id
                            rs.getString("subject"),
                            rs.getString("time"),
                            rs.getString("day"),
                            rs.getString("month"),
                            rs.getString("year"),
                            rs.getString("payment_status")

                        );
                    }
                    
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
    // Method to fetch tutor's name based on tutor_id
    private String getTutorName(int tutorId) {
        String tutorName = "";
        Connection conn = getConnection();
        String query = "SELECT firstName, lastName FROM tutors WHERE tutor_id = ?";
    
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, tutorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String firstName = rs.getString("firstName");
                    String lastName = rs.getString("lastName");
                    tutorName = firstName + " " + lastName;
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
        return tutorName;
    }

    // Method to set the tutee's name to the label
private void setTuteeNameLabel() {
    Connection conn = getConnection();
    String query = "SELECT name FROM tutees WHERE tutee_id = ?";
    try (PreparedStatement ps = conn.prepareStatement(query)) {
        ps.setInt(1, TuteeController.ID);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String tuteeName = rs.getString("name");
                lbName.setText(tuteeName + "'s Schedule");
            } else {
                lbName.setText("Tutee Name: Not Found");
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
}

@FXML
private void filterBySubject() {
    String selectedSubject = cbSubject.getValue();
    ObservableList<schedules> filteredList = FXCollections.observableArrayList();
    
    if (selectedSubject != null && !selectedSubject.isEmpty()) {
        ObservableList<schedules> originalList = getTuteesList();
        
        for (schedules schedule : originalList) {
            if (schedule.getSubjects().equals(selectedSubject)) {
                filteredList.add(schedule);
            }
        }
        
        // Set the filtered list to the TableView
        tvSchedules.setItems(filteredList);
    } else {
        // If no subject is selected, show all schedules
        showschedules();
    }
}

@FXML
private void handleClose(ActionEvent event) {
    // Get the reference to the button's stage
    Stage stage = (Stage) btnClose.getScene().getWindow();
    stage.close(); // Close the stage
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

    // Define other methods as needed
}