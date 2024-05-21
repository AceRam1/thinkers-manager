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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.fxml.Initializable;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.DayOfWeek;
import java.time.LocalDate;


public class SetDateController implements Initializable{

    @FXML
    private Button btnSet;

    @FXML
    private ChoiceBox<String> cbDay;

    @FXML
    private ChoiceBox<String> cbMonth;

    @FXML
    private ChoiceBox<String> cbYear;

    @FXML
    private ChoiceBox<String> cbTime;

    private String selectedMonth;
    private String selectedYear;

    private Stage stage;

    static String StartDay;
    static String month;
    static String year;
    static String time;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        populateMonthChoiceBox();
        populateYearChoiceBox();
        populateTimeChoiceBox();
        
        cbMonth.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && cbYear.getValue() != null) {
                updateDayChoiceBox();
            }
        });
        
        cbYear.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && cbMonth.getValue() != null) {
                updateDayChoiceBox();
            }
        });

        System.out.println(TuteeController.selectedSubject);
    }

        // Method to set the stage reference
        public void setStage(Stage stage) {
            this.stage = stage;
        }

    private void populateMonthChoiceBox() {
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        cbMonth.getItems().addAll(months);
    }

    private void populateYearChoiceBox() {
        int currentYear = LocalDate.now().getYear(); // Get the current year
        for (int year = currentYear; year <= 2030; year++) { // Start from the current year
            cbYear.getItems().add(String.valueOf(year));
        }
    }

    private void populateTimeChoiceBox() {
        for (int hour = 9; hour <= 17; hour++) { // Loop from 9 AM to 5 PM
            if (hour != 12) { // Skip 12 PM
                for (int minute = 0; minute < 60; minute += 60) {
                    String startTime = String.format("%d:00 %s", hour % 12 == 0 ? 12 : hour % 12, hour < 12 ? "AM" : "PM");
                    String endTime = String.format("%d:00 %s", (hour + 1) % 12 == 0 ? 12 : (hour + 1) % 12, (hour + 1) < 12 ? "AM" : "PM");
                    String timeSlot;
                    if (hour == 11 && hour < 12) {
                        timeSlot = startTime + " - " + "12:00 NN";
                    } else {
                        timeSlot = startTime + " - " + endTime;
                    }
                    cbTime.getItems().add(timeSlot);
                }
            }
        }
    }

    private void updateDayChoiceBox() {
        int selectedMonthIndex = cbMonth.getSelectionModel().getSelectedIndex();
        selectedMonth = cbMonth.getSelectionModel().getSelectedItem();
        selectedYear = cbYear.getValue();
        int daysInMonth = getDaysInMonth(selectedMonthIndex, Integer.parseInt(selectedYear));
    
        LocalDate currentDate = LocalDate.now(); // Get the current date
    
        cbDay.getItems().clear();
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = LocalDate.of(Integer.parseInt(selectedYear), selectedMonthIndex + 1, day);
            DayOfWeek dayOfWeek = date.getDayOfWeek();
    
            // Only add days that are after or equal to the current date
            if (date.isEqual(currentDate) || date.isAfter(currentDate)) {
                String dayString = String.valueOf(day);
                if (isDayBookedForTutee(TuteeController.ID, date) || isDayBookedForTutor(SelectTutorController.tutor_id, date)) {
                    dayString += " (unavailable)";
                }
                // Mark weekends as "(weekend)"
                if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                    dayString += " (weekend)";
                }
                cbDay.getItems().add(dayString);
            }
        }
    }
    
    private int getDaysInMonth(int month, int year) {
        if (month == 1) {
            if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                return 29;
            } else {
                return 28;
            }
        } else if (month == 3 || month == 5 || month == 8 || month == 10) {
            return 30;
        } else {
            return 31;
        }
    }

    private boolean isDayBookedForTutee(int tuteeId, LocalDate date) {
        String query = "SELECT COUNT(*) FROM schedules WHERE tutee_id = ? AND day = ? AND month = ? AND year = ?";
        Connection conn = getConnection();
        PreparedStatement ps;
        ResultSet rs;
    
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, tuteeId);
            ps.setInt(2, date.getDayOfMonth());
            ps.setString(3, date.getMonth().name());
            ps.setInt(4, date.getYear());
            rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isDayBookedForTutor(int tutorId, LocalDate date) {
        String query = "SELECT COUNT(*) FROM schedules WHERE tutor_id = ? AND day = ? AND month = ? AND year = ?";
        Connection conn = getConnection();
        PreparedStatement ps;
        ResultSet rs;
    
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, tutorId);
            ps.setInt(2, date.getDayOfMonth());
            ps.setString(3, date.getMonth().name());
            ps.setInt(4, date.getYear());
            rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            return count > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isScheduleConflict(int tuteeId, String day, String month, String year, String time) {
        String query = "SELECT COUNT(*) FROM schedules WHERE tutee_id = ? AND day = ? AND month = ? AND year = ? AND time = ?";
        Connection conn = getConnection();
        PreparedStatement ps;
        ResultSet rs;
    
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, tuteeId);
            ps.setString(2, day);
            ps.setString(3, month);
            ps.setString(4, year);
            ps.setString(5, time);
            rs = ps.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            return count > 0; // Returns true if there is a schedule conflict
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @FXML
    private void handleSetButtonAction() {
        time = cbTime.getValue();
        month = cbMonth.getValue();
        StartDay = cbDay.getValue();
        year = cbYear.getValue();

        String endDateString = "";
        String startDateString = "";

        

        // Check if the selected day is unavailable or a weekend
        if (StartDay.contains("(unavailable)") || StartDay.contains("(weekend)")) {
            // Display an alert informing the user that the selected day is unavailable
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Unavailable Day");
            alert.setHeaderText(null);
            alert.setContentText("The selected day is unavailable or a weekend. Please choose another day.");
            alert.showAndWait();
            return; // Exit the method without further processing
        }

        // Check if the tutor already has a schedule at the selected date and time
        if (isScheduleConflict(TuteeController.ID, StartDay, month, year, time)) {
            // Display an alert informing the user about the schedule conflict
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Schedule Conflict");
            alert.setHeaderText(null);
            alert.setContentText("The tutee already has a schedule at the selected date and time.");
            alert.showAndWait();
            return; // Exit the method without inserting the schedule
        }

        if(TuteeController.status == "new"){
        // Remove the trailing comma and space if the subject is not empty
            String query2 = "INSERT INTO tutees (tutee_id, name, nickname, gender, age, address, contact_information, subject_taking, time_slot) VALUES ('"
            + TuteeController.ID + "','"
            + TuteeController.name + "','"
            + TuteeController.nickname + "','"
            + TuteeController.gender + "',"
            + TuteeController.age + ",'"
            + TuteeController.address + "','"
            + TuteeController.contact + "','"
            + TuteeController.selectedSubject + "','"
            + time + "')"; 

        executeQuery(query2);
        TuteeController.status = null;
        }
        
        int sescount = 1;
        int currentDay = Integer.parseInt(StartDay);
        int currentMonthIndex = cbMonth.getItems().indexOf(month);
        int currentYear = Integer.parseInt(year);

        // Determine the values for all subjects
        int readingAndMathValue = TuteeController.selectedSubject.equals("Reading and Math") ? 1 : 0;
        int scienceClassValue = TuteeController.selectedSubject.equals("Science Class") ? 1 : 0;
        int academicAssistanceValue = TuteeController.selectedSubject.equals("Academic Assistance") ? 1 : 0;
        int englishConversationClassValue = TuteeController.selectedSubject.equals("English Conversation Class") ? 1 : 0;

// Determine the current values for all subjects
String queryGetSubjects = "SELECT Reading_And_Math, Science_Class, Academic_Assistance, English_Conversation_Class FROM tutees WHERE tutee_id = ?";
try (Connection conn = getConnection();
     PreparedStatement ps = conn.prepareStatement(queryGetSubjects)) {
    ps.setInt(1, TuteeController.ID);
    try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
            readingAndMathValue = rs.getInt("Reading_And_Math");
            scienceClassValue = rs.getInt("Science_Class");
            academicAssistanceValue = rs.getInt("Academic_Assistance");
            englishConversationClassValue = rs.getInt("English_Conversation_Class");

            // Update the values based on the selected subject
            switch (TuteeController.selectedSubject) {
                case "Reading and Math":
                    if (readingAndMathValue == 0) {
                        readingAndMathValue = 1;
                    }
                    break;
                case "Science Class":
                    if (scienceClassValue == 0) {
                        scienceClassValue = 1;
                    }
                    break;
                case "Academic Assistance":
                    if (academicAssistanceValue == 0) {
                        academicAssistanceValue = 1;
                    }
                    break;
                case "English Conversation Class":
                    if (englishConversationClassValue == 0) {
                        englishConversationClassValue = 1;
                    }
                    break;
                default:
                    // Handle default case
                    break;
            }

            // Update the database with the modified subject values
            String queryUpdateSubjects = "UPDATE tutees SET Reading_And_Math = ?, Science_Class = ?, Academic_Assistance = ?, English_Conversation_Class = ? WHERE tutee_id = ?";
            try (PreparedStatement updateStatement = conn.prepareStatement(queryUpdateSubjects)) {
                updateStatement.setInt(1, readingAndMathValue);
                updateStatement.setInt(2, scienceClassValue);
                updateStatement.setInt(3, academicAssistanceValue);
                updateStatement.setInt(4, englishConversationClassValue);
                updateStatement.setInt(5, TuteeController.ID);
                updateStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
} catch (SQLException e) {
    e.printStackTrace();
}
    
        // Check if the subject_taking column in the tutees table has the value of TuteeController.selectedSubject
        if (!isSubjectTakingEqualToSelectedSubject(TuteeController.ID, TuteeController.selectedSubject)) {
            // Update the subject_taking column in the tutees table by concatenating the selected subject
            updateSubjectTaking(TuteeController.ID, TuteeController.selectedSubject);
        }
    
// Determine the first day of the month
LocalDate firstDayOfMonth = LocalDate.of(currentYear, currentMonthIndex + 1, currentDay);
DayOfWeek firstDayOfWeek = firstDayOfMonth.getDayOfWeek();
int startDayIndex = firstDayOfWeek.getValue(); // 1 for Monday, 2 for Tuesday, ..., 7 for Sunday

// Initialize start date string with default value
startDateString = year + "-" + String.format("%02d", currentMonthIndex + 1) + "-" + String.format("%02d", currentDay);

// Insert into the database for each day of the month until March 19th with a maximum of 22 sessions
while (sescount <= 22) { // March is month index 2
    // Skip Saturdays (6) and Sundays (7)
    if (startDayIndex != 6 && startDayIndex != 7) {
        String query = "INSERT INTO schedules (session_count, tutor_id, tutee_id, subject, time, day, month, year) VALUES ('"
                + sescount + "','"
                + SelectTutorController.tutor_id + "','"
                + TuteeController.ID + "','"
                + TuteeController.selectedSubject + "', '"
                + time + "', '"
                + currentDay + "', '"
                + cbMonth.getItems().get(currentMonthIndex) + "', '"
                + year + "')";

        executeQuery(query);
        sescount++;
    }

    currentDay++;
    startDayIndex++;
    if (startDayIndex > 7) {
        startDayIndex = 1; // Reset to Monday if it exceeds Sunday
    }

    // If the current day exceeds the days in the current month, move to the next month
    if (currentDay > getDaysInMonth(currentMonthIndex, currentYear)) {
        currentMonthIndex++; // Move to the next month
        currentDay = 1; // Reset the day to the first day of the next month
    }

    // Update end date string when sescount is 22
    if (sescount == 22) {
        endDateString = year + "-" + String.format("%02d", currentMonthIndex + 1) + "-" + String.format("%02d", currentDay);
    }
}

// Construct the status string with start and end dates
String status = startDateString + " to " + endDateString;
    


    // Insert into the payment table with start and end dates in the status column
    String query = "INSERT INTO payment (tutee_id, subject, start_date, end_date, balance, status) VALUES ('"
            + TuteeController.ID + "','"
            + TuteeController.selectedSubject + "', '"
            + startDateString + "', '"
            + endDateString + "', '"
            + 2000 + "', '"
            + "Unpaid" + "')";

    executeQuery(query);

    closeWindow();

    switchToSched();
    System.out.println("reached");

    }

    private boolean isSubjectTakingEqualToSelectedSubject(int iD, String selectedSubject) {
        String query = "SELECT COUNT(*) FROM tutees WHERE tutee_id = '" + iD + "' AND subject_taking LIKE '%" + selectedSubject + "%'";
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

// Method to update the subject_taking column in the tutees table by concatenating the selected subject
private void updateSubjectTaking(int iD, String selectedSubject) {
    String query = "UPDATE tutees SET subject_taking = CONCAT(subject_taking, ', ', '" + selectedSubject + "') WHERE tutee_id = '" + iD + "'";
    Connection conn = getConnection();
    Statement st;

    try {
        st = conn.createStatement();
        st.executeUpdate(query);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    // Method to close the window
    public void closeWindow() {
        // Get the reference to the stage
        Stage stage = (Stage) cbTime.getScene().getWindow();
        stage.close(); // Close the stage
    }
    
    public void switchToSched() {
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
}