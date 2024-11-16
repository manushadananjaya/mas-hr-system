package manusha.mas.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import manusha.mas.util.DatabaseConnection;
import manusha.mas.model.RequestDetails;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class IEDashboardController {

    public Button editBtn;
    public TableView shiftTable;
    public TableColumn nameColumn;
    public TableColumn sectionColumn;
    public TableColumn moduleColumn;
    public TableColumn operationColumn;
    public TableColumn dateColumn;
    public TableColumn countColumn;
    @FXML
    private AnchorPane mainContentPane, requestedDetailsPane, trainingPerformancePane;

    @FXML
    private Button requestingFormBtn, requestedDetailsBtn, trainingPerfoBtn;

    @FXML
    private TextField ieepfField, ieNameField, ieShiftField, ieSectionField;
    @FXML
    private TextField moduleNoField, customerField, requiredOperationField, requiredCountField;

    @FXML
    private DatePicker requiredDatePicker;

    @FXML
    private TableView<RequestDetails> ieTable;

    @FXML
    private TableColumn<RequestDetails, Integer> idColumn;
    @FXML
    private TableColumn<RequestDetails, String> ieepfColumn, ieNameColumn, ieShiftColumn, ieSectionColumn;
    @FXML
    private TableColumn<RequestDetails, String> moduleNoColumn, customerColumn, ReqOperationColumn, reqDateColumn;
    @FXML
    private TableColumn<RequestDetails, Integer> reqCountColumn;

    @FXML
    private Button saveBtn, deleteBtn;

    private ObservableList<RequestDetails> requestDetailsList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Initially show the main content pane and hide others
        showPane(mainContentPane);
        highlightButton(requestingFormBtn);

        // Initialize table columns
        initializeTableColumns();
        

        // Load current data into the table
        loadCurrentRequestedDetails();

        // Add button actions
        saveBtn.setOnAction(event -> saveOrUpdateRequestDetails());
        deleteBtn.setOnAction(event -> deleteSelectedDetails());
        editBtn.setOnAction(event -> loadSelectedDetailsForEdit());
    }

    @FXML
    private void handleRequestingFormBtn() {
        showPane(mainContentPane);
        highlightButton(requestingFormBtn);
    }

    @FXML
    private void handleRequestedDetailsBtn() {
        showPane(requestedDetailsPane);
        highlightButton(requestedDetailsBtn);
    }

    @FXML
    private void handleTrainingPerfoBtn() {
        showPane(trainingPerformancePane);
        highlightButton(trainingPerfoBtn);
    }

    private void showPane(AnchorPane paneToShow) {
        mainContentPane.setVisible(false);
        requestedDetailsPane.setVisible(false);
        trainingPerformancePane.setVisible(false);
        paneToShow.setVisible(true);
    }

    private void highlightButton(Button activeButton) {
        requestingFormBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        requestedDetailsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        trainingPerfoBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        activeButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;");
    }

    private void initializeTableColumns() {
        idColumn.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        ieepfColumn.setCellValueFactory(data -> data.getValue().ieepfProperty());
        ieNameColumn.setCellValueFactory(data -> data.getValue().ieNameProperty());
        ieShiftColumn.setCellValueFactory(data -> data.getValue().ieShiftProperty());
        ieSectionColumn.setCellValueFactory(data -> data.getValue().ieSectionProperty());
        moduleNoColumn.setCellValueFactory(data -> data.getValue().moduleNoProperty());
        ReqOperationColumn.setCellValueFactory(data -> data.getValue().operationProperty());
        reqDateColumn.setCellValueFactory(data -> data.getValue().reqDateProperty().asString());
        reqCountColumn.setCellValueFactory(data -> data.getValue().countProperty().asObject());
    }



    private void loadSelectedDetailsForEdit() {
        RequestDetails selected = ieTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showErrorDialog("Selection Error", "No record selected for editing.");
            return;
        }

        // Populate fields with the selected record's data
        ieepfField.setText(selected.getIeepf());
        ieNameField.setText(selected.getIeName());
        ieShiftField.setText(selected.getIeShift());
        ieSectionField.setText(selected.getIeSection());
        moduleNoField.setText(selected.getModuleNo());
        customerField.setText(selected.getCustomer());
        requiredOperationField.setText(selected.getOperation());
        requiredDatePicker.setValue(selected.getReqDate());
        requiredCountField.setText(String.valueOf(selected.getCount()));

        // Highlight the row being edited
        ieTable.getSelectionModel().clearSelection();
        ieTable.getSelectionModel().select(selected);
    }

    private void loadCurrentRequestedDetails() {
        requestDetailsList.clear();
        String query = "SELECT * FROM requested_details WHERE req_date = CURDATE()";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                requestDetailsList.add(new RequestDetails(
                        rs.getInt("id"),
                        rs.getString("ieepf"),
                        rs.getString("ie_name"),
                        rs.getString("ie_shift"),
                        rs.getString("ie_section"),
                        rs.getString("module_no"),
                        rs.getString("customer"),
                        rs.getString("operation"),
                        rs.getDate("req_date").toLocalDate(),
                        rs.getInt("req_count")
                ));
            }
        } catch (SQLException e) {
            showErrorDialog("Error Loading Data", "Unable to load data from the database.\n" + e.getMessage());
        }
        ieTable.setItems(requestDetailsList);
    }

    private void saveOrUpdateRequestDetails() {
        String ieepf = ieepfField.getText();
        String ieName = ieNameField.getText();
        String ieShift = ieShiftField.getText();
        String ieSection = ieSectionField.getText();
        String moduleNo = moduleNoField.getText();
        String customer = customerField.getText();
        String operation = requiredOperationField.getText();
        LocalDate reqDate = requiredDatePicker.getValue();
        String reqCountStr = requiredCountField.getText();

        if (ieepf.isEmpty() || ieName.isEmpty() || reqDate == null || reqCountStr.isEmpty()) {
            showErrorDialog("Validation Error", "Please fill in all required fields.");
            return;
        }

        try {
            int reqCount = Integer.parseInt(reqCountStr);
            RequestDetails selected = ieTable.getSelectionModel().getSelectedItem();

            if (selected != null) {
                // Update existing record
                String updateQuery = "UPDATE requested_details SET ieepf = ?, ie_name = ?, ie_shift = ?, " +
                        "ie_section = ?, module_no = ?, customer = ?, operation = ?, req_date = ?, req_count = ? WHERE id = ?";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                    stmt.setString(1, ieepf);
                    stmt.setString(2, ieName);
                    stmt.setString(3, ieShift);
                    stmt.setString(4, ieSection);
                    stmt.setString(5, moduleNo);
                    stmt.setString(6, customer);
                    stmt.setString(7, operation);
                    stmt.setDate(8, java.sql.Date.valueOf(reqDate));
                    stmt.setInt(9, reqCount);
                    stmt.setInt(10, selected.getId());
                    stmt.executeUpdate();
                }
            } else {
                // Insert new record
                String insertQuery = "INSERT INTO requested_details (ieepf, ie_name, ie_shift, ie_section, module_no, " +
                        "customer, operation, req_date, req_count) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                    stmt.setString(1, ieepf);
                    stmt.setString(2, ieName);
                    stmt.setString(3, ieShift);
                    stmt.setString(4, ieSection);
                    stmt.setString(5, moduleNo);
                    stmt.setString(6, customer);
                    stmt.setString(7, operation);
                    stmt.setDate(8, java.sql.Date.valueOf(reqDate));
                    stmt.setInt(9, reqCount);
                    stmt.executeUpdate();
                }
            }

            // Reload the table data
            loadCurrentRequestedDetails();
            clearFields();

        } catch (NumberFormatException e) {
            showErrorDialog("Validation Error", "Required Count must be a number.");
        } catch (SQLException e) {
            showErrorDialog("Error Saving Data", "Unable to save data to the database.\n" + e.getMessage());
        }
    }

    private void clearFields() {
        ieepfField.clear();
        ieNameField.clear();
        ieShiftField.clear();
        ieSectionField.clear();
        moduleNoField.clear();
        customerField.clear();
        requiredOperationField.clear();
        requiredDatePicker.setValue(null);
        requiredCountField.clear();
    }

    private void deleteSelectedDetails() {
        RequestDetails selected = ieTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showErrorDialog("Selection Error", "No record selected for deletion.");
            return;
        }

        String query = "DELETE FROM requested_details WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, selected.getId());
            stmt.executeUpdate();
            loadCurrentRequestedDetails();
        } catch (SQLException e) {
            showErrorDialog("Error Deleting Data", "Unable to delete the selected record.\n" + e.getMessage());
        }
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
