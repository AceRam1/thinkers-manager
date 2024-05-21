package ThinkersUserManagement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // Import Statement from java.sql
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

public class paymentController implements Initializable {

    @FXML
    private Button btnSubmit;

    @FXML
    private TableColumn<payment, Integer> colBalance;

    @FXML
    private TableColumn<payment, Integer> colId;

    @FXML
    private TableColumn<payment, String> colStatus;

    @FXML
    private TableColumn<payment, String> colSubject;

    @FXML
    private TableColumn<payment, String> colEnd;

    @FXML
    private TableColumn<payment, String> colStart;

    @FXML
    private TextField tfAmount;

    @FXML
    private TableView<payment> tvPayment;

        private List<payment> receipt = new ArrayList<>();
        List<String> receiptList = new ArrayList<>();
        String filePath = "C:/Users/Peter/Desktop/thinkers manager/receipt.pdf";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int tuteeId = selectTuteeController.payment_tuteeid; // Get the tutee ID
        showPayments(tuteeId);
    }
    
    public void showPayments(int tuteeId) {
        ObservableList<payment> list = getPaymentsList(tuteeId);
    
        // Update the PropertyValueFactory to match the field name in the payment class
        colId.setCellValueFactory(new PropertyValueFactory<payment, Integer>("paymentId"));
        colSubject.setCellValueFactory(new PropertyValueFactory<payment, String>("subject"));
        colStart.setCellValueFactory(new PropertyValueFactory<payment, String>("start"));
        colEnd.setCellValueFactory(new PropertyValueFactory<payment, String>("end"));
        colBalance.setCellValueFactory(new PropertyValueFactory<payment, Integer>("balance"));
        colStatus.setCellValueFactory(new PropertyValueFactory<payment, String>("status"));
    
        // Check if the balance is 0 and update the status to "Paid"
        for (payment p : list) {
            if (p.getBalance() == 0) {
                p.setStatus("Paid");
                updateStatusInDatabase(p.getPaymentId(), "Paid");
            }
        }
    
        // Print statements for debugging
        System.out.println("Size of payment list: " + list.size());
        for (payment p : list) {
            System.out.println("Payment ID: " + p.getPaymentId() + ", Start: " + p.getStart() + ", End: " + p.getEnd());
        }
    
        tvPayment.setItems(list);
    }

    public ObservableList<payment> getPaymentsList(int tuteeId) {
        ObservableList<payment> paymentList = FXCollections.observableArrayList(); // Instantiate paymentList
        Connection conn = getConnection(); // Setting up connection
        String query = "SELECT * FROM payment WHERE tutee_id = ?"; // Query to fetch data from payment table for the specific tutee
        PreparedStatement st; // Setting up the prepared statement
        ResultSet rs; // Setting up the resultset
    
        try {
            st = conn.prepareStatement(query);
            st.setInt(1, tuteeId); // Set the tutee ID parameter
            rs = st.executeQuery();
            payment payment;
            while (rs.next()) { // While the result set has next value keep on reading
                // Print statements to verify data retrieved from the database
                System.out.println("payment_id: " + rs.getInt("payment_id") + ", subject: " + rs.getString("subject") + ", balance: " + rs.getInt("balance") + ", status: " + rs.getString("status"));
    
                payment = new payment(
                        rs.getInt("payment_id"),
                        rs.getInt("tutee_id"),
                        rs.getString("subject"),
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getInt("balance"),
                        rs.getString("status")
                );
                // Print statement to verify the instantiation of payment objects
                System.out.println("Created payment object: " + payment.getPaymentId());
                paymentList.add(payment); // Adding the values to the paymentList
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return paymentList; // Returns the list of payments
    }

    @FXML
    private void handleMouseAction(MouseEvent event) {
        payment selectedPayment = tvPayment.getSelectionModel().getSelectedItem();
        if (selectedPayment != null) {
            int paymentId = selectedPayment.getPaymentId();
            System.out.println("Selected Payment ID: " + paymentId);
            // You can use the paymentId as needed here
        } else {
            System.out.println("No payment selected");
        }
    }

    public void Pay() {
        // Get the selected payment
        payment selectedPayment = tvPayment.getSelectionModel().getSelectedItem();
        if (selectedPayment == null) {
            System.out.println("No payment selected.");
            return;
        }
    
        // Get the amount from the tfAmount TextField
        int amount = Integer.parseInt(tfAmount.getText());
        if (amount <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid amount");
            alert.setHeaderText(null);
            alert.setContentText("Invalid amount");
            alert.showAndWait();
            return;
        }
    
        // Deduct the amount from the balance
        int updatedBalance = selectedPayment.getBalance() - amount;
        if (updatedBalance < 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Insufficient balance");
            alert.setHeaderText(null);
            alert.setContentText("The payment is higher than the required.");
            alert.showAndWait();
            return;
        }
    
        // Update the balance in the payment object
        selectedPayment.setBalance(updatedBalance);
    
        // Update the balance in the database
        String updateBalanceQuery = "UPDATE payment SET balance = ? WHERE payment_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(updateBalanceQuery)) {
            statement.setInt(1, updatedBalance);
            statement.setInt(2, selectedPayment.getPaymentId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Balance updated successfully.");
            } else {
                System.out.println("Failed to update balance.");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
    
        // Refresh the TableView to reflect the changes
        showPayments(selectTuteeController.payment_tuteeid);
    
        // Initialize parameters for updating schedule payment status
        int tuteeId = selectedPayment.getTuteeId();
        String startDate = selectedPayment.getStart();
        String endDate = selectedPayment.getEnd();
    
        // Update schedule payment status
        updateSchedulePaymentStatus(tuteeId, startDate, endDate);
        System.out.println(tuteeId + ", " + startDate + ", " + endDate);
    
        // Generate receipt content
        List<String> contentList = new ArrayList<>();
        contentList.add("Tutee ID: " + selectedPayment.getTuteeId()+"                                                                                      "+ "Transaction Number: " + selectedPayment.getPaymentId());
        contentList.add("\n-------------------------------------------------------------------------------------------------------------------------");
        contentList.add(selectedPayment.getSubject()+"                                                              "+"Date: " + selectedPayment.getStart() + " - " + selectedPayment.getEnd());
        contentList.add("\n\n-------------------------------------------------------------------------------------------------------------------------");
        contentList.add("Status: " + selectedPayment.getStatus()+"                                                                                                        "+"Balance: " + selectedPayment.getBalance());

    
        // Generate PDF with the extracted content
        generatePDF(contentList, "C:/Users/Peter/Desktop/thinkers manager/receipt.pdf");
        openFile(filePath);

    }

    private void updateStatusInDatabase(int paymentId, String newStatus) {
        String updateStatusQuery = "UPDATE payment SET status = ? WHERE payment_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(updateStatusQuery)) {
            statement.setString(1, newStatus);
            statement.setInt(2, paymentId);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Status updated successfully.");
            } else {
                System.out.println("Failed to update status.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSchedulePaymentStatus(int tuteeId, String startDate, String endDate) {
        // First, check if the balance is zero
        if (getBalance(tuteeId) == 0) {
            // Define the SQL query to update the payment status
            String updateQuery = "UPDATE schedules SET payment_status = ? WHERE tutee_id = ? AND CONCAT(year, '-', "
                                 + "CASE "
                                 + "    WHEN month = 'Jan' OR month = 'January' THEN '01' "
                                 + "    WHEN month = 'Feb' OR month = 'February' THEN '02' "
                                 + "    WHEN month = 'Mar' OR month = 'March' THEN '03' "
                                 + "    WHEN month = 'Apr' OR month = 'April' THEN '04' "
                                 + "    WHEN month = 'May' THEN '05' "
                                 + "    WHEN month = 'Jun' OR month = 'June' THEN '06' "
                                 + "    WHEN month = 'Jul' OR month = 'July' THEN '07' "
                                 + "    WHEN month = 'Aug' OR month = 'August' THEN '08' "
                                 + "    WHEN month = 'Sep' OR month = 'September' THEN '09' "
                                 + "    WHEN month = 'Oct' OR month = 'October' THEN '10' "
                                 + "    WHEN month = 'Nov' OR month = 'November' THEN '11' "
                                 + "    WHEN month = 'Dec' OR month = 'December' THEN '12' "
                                 + "    ELSE NULL "
                                 + "END, '-', LPAD(day, 2, '0')) BETWEEN ? AND ?";
    
            try (Connection conn = getConnection();
                 PreparedStatement statement = conn.prepareStatement(updateQuery)) {
                // Set the new payment status
                statement.setString(1, "Paid");
                // Set the tutee ID
                statement.setInt(2, tuteeId);
                // Set the start date
                statement.setString(3, startDate);
                // Set the end date
                statement.setString(4, endDate);
    
                // Execute the update statement
                int rowsAffected = statement.executeUpdate();
                System.out.println(rowsAffected + " rows updated.");
    
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void generatePDF(List<String> contentList, String filePath) {
        // Create a new Document
        Document document = new Document();
    
        try {
            // Create a PdfWriter to write the Document to a file
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
    
            // Open the Document
            document.open();
    
            // Check if contentList is empty
            if (contentList.isEmpty()) {
                System.out.println("Content list is empty. No content to add to the PDF.");
            } else {
                // Add content to the Document
                for (String content : contentList) {
                    document.add(new Paragraph(content));
                }
            }
    
            // Close the Document
            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Method to get the balance for a specific tutee
    public int getBalance(int tuteeId) {
        int balance = -1;
        String query = "SELECT SUM(balance) AS total_balance FROM payment WHERE tutee_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, tuteeId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    balance = rs.getInt("total_balance");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }
        public static void openFile(String filePath) {
        // Create a File object
        File file = new File(filePath);

        try {
            // Check if the file exists
            if (file.exists()) {
                // Check if the Desktop class is supported
                if (Desktop.isDesktopSupported()) {
                    // Get the desktop instance
                    Desktop desktop = Desktop.getDesktop();

                    // Open the file with the default associated application
                    desktop.open(file);
                } else {
                    System.out.println("Desktop is not supported on this platform.");
                }
            } else {
                System.out.println("The file does not exist: " + filePath);
            }
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

    // Setting up the connection
    public Connection getConnection() {
        Connection conn;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/thinkersdb", "root", ""); // Getting the database connection
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
