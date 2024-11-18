package manusha.mas.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import manusha.mas.util.DatabaseConnection;
import javafx.scene.input.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TrainingAssistantController {

    public ListView nameSuggestionsListView;
    @FXML
    private Button attendanceBtn;

    @FXML
    private RadioButton attendanceNo;

    @FXML
    private AnchorPane attendancePane;

    @FXML
    private TableColumn<?, ?> attendancePercentColumn;

    @FXML
    private TableView<?> attendanceTable;

    @FXML
    private RadioButton attendanceYes;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField designationField;

    @FXML
    private TableColumn<?, ?> epfColumn;

    @FXML
    private TextField epfField;

    @FXML
    private BarChart<?, ?> fttChart;

    @FXML
    private TextField genderField;

    @FXML
    private Button individualPerformanceBtn;

    @FXML
    private Button logOutBtn;

    @FXML
    private ComboBox<?> monthComboBox;

    @FXML
    private TableColumn<?, ?> nameColumn;

    @FXML
    private TextField nameField;

    @FXML
    private TextField nameFieldIndividualPerfomance;

    @FXML
    private Slider operation1Slider;

    @FXML
    private Label operation1ValueLabel;

    @FXML
    private Slider operation2Slider;

    @FXML
    private Label operation2ValueLabel;

    @FXML
    private Slider operation3Slider;

    @FXML
    private Label operation3ValueLabel;

    @FXML
    private Slider operation4Slider;

    @FXML
    private Label operation4ValueLabel;

    @FXML
    private Slider operation5Slider;

    @FXML
    private Label operation5ValueLabel;

    @FXML
    private Slider operation6Slider;

    @FXML
    private Label operation6ValueLabel;

    @FXML
    private BarChart<?, ?> performanceChart;

    @FXML
    private BarChart<?, ?> performanceDailyChart;

    @FXML
    private Button performanceFormBtn;

    @FXML
    private Slider performanceSlider;

    @FXML
    private Label performanceValueLabel;

    @FXML
    private Slider qualitySlider;

    @FXML
    private Label qualityValueLabel;

    @FXML
    private TextField searchField;

    @FXML
    private AnchorPane trainingAssistantPane;

    @FXML
    private TextField trainingField;

    @FXML
    private AnchorPane trainingPerformancePane;

    @FXML
    private Button saveButton;


    private ObservableList<String> suggestionsList = FXCollections.observableArrayList();



    @FXML
    public void initialize() {
        nameSuggestionsListView.setItems(suggestionsList);
        nameSuggestionsListView.setVisible(false); // Hide by default

        nameFieldIndividualPerfomance.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 1) { // Only query when input length > 1
                fetchNameSuggestions(newValue);
            } else {
                nameSuggestionsListView.setVisible(false);
            }
        });

        nameSuggestionsListView.setOnMouseClicked((MouseEvent event) -> {
            String selectedName = (String) nameSuggestionsListView.getSelectionModel().getSelectedItem();
            if (selectedName != null) {
                nameFieldIndividualPerfomance.setText(selectedName);
                populateEpfField(selectedName);
                nameSuggestionsListView.setVisible(false);
            }
        });

        bindSliderToLabel(performanceSlider, performanceValueLabel);
        bindSliderToLabel(qualitySlider, qualityValueLabel);
        bindSliderToLabel(operation1Slider, operation1ValueLabel);
        bindSliderToLabel(operation2Slider, operation2ValueLabel);
        bindSliderToLabel(operation3Slider, operation3ValueLabel);
        bindSliderToLabel(operation4Slider, operation4ValueLabel);
        bindSliderToLabel(operation5Slider, operation5ValueLabel);
        bindSliderToLabel(operation6Slider, operation6ValueLabel);

        showPane(trainingAssistantPane);
        highlightButton(individualPerformanceBtn);
    }



    @FXML
    private void individualPerformanceBtn() {
        showPane(trainingAssistantPane);
        highlightButton(individualPerformanceBtn);
    }

    @FXML
    private void performanceFormBtn() {
        showPane(trainingPerformancePane);
        highlightButton(performanceFormBtn);
    }

    @FXML
    private void attendanceBtn() {
        showPane(attendancePane);
        highlightButton(attendanceBtn);
    }


    private void showPane(AnchorPane paneToShow) {
        trainingAssistantPane.setVisible(false);
        attendancePane.setVisible(false);
        trainingPerformancePane.setVisible(false);
        paneToShow.setVisible(true);
    }


    private void bindSliderToLabel(Slider slider, Label label) {
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            label.setText(String.format("%.0f%%", newValue.doubleValue()));
        });
    }

    private void fetchNameSuggestions(String input) {
        suggestionsList.clear();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT full_name FROM employees WHERE full_name LIKE ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, input + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                suggestionsList.add(resultSet.getString("full_name"));
            }

            nameSuggestionsListView.setVisible(!suggestionsList.isEmpty());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateEpfField(String name) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT epf_number FROM employees WHERE full_name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                epfField.setText(resultSet.getString("epf_number"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onSaveButtonClick() {
        if (nameFieldIndividualPerfomance.getText().trim().isEmpty() ||
                epfField.getText().trim().isEmpty() ||
                datePicker.getValue() == null ||
                (!attendanceYes.isSelected() && !attendanceNo.isSelected())) {

            // Show warning alert for missing fields
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill in all required fields and ensure attendance is selected.");
            alert.show();
            return; // Stop execution if validation fails
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String insertQuery = "INSERT INTO employee_performance (name, epf, date, attendance, performance, quality, operation1, operation2, operation3, operation4, operation5, operation6) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insertQuery);

            // Set name and EPF from text fields
            stmt.setString(1, nameFieldIndividualPerfomance.getText().trim());
            stmt.setString(2, epfField.getText().trim());

            // Set date from date picker
            stmt.setDate(3, java.sql.Date.valueOf(datePicker.getValue()));

            // Set attendance as boolean (converted to "Yes" or "No")
            stmt.setInt(4, attendanceYes.isSelected() ? 1 : 0);

            // Set performance, quality, and operation values from sliders
            stmt.setDouble(5, performanceSlider.getValue()); // Set as double to preserve decimal precision
            stmt.setDouble(6, qualitySlider.getValue());
            stmt.setDouble(7, operation1Slider.getValue());
            stmt.setDouble(8, operation2Slider.getValue());
            stmt.setDouble(9, operation3Slider.getValue());
            stmt.setDouble(10, operation4Slider.getValue());
            stmt.setDouble(11, operation5Slider.getValue());
            stmt.setDouble(12, operation6Slider.getValue());

            // Execute the update
            stmt.executeUpdate();

            // Display success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Data saved successfully!");
            alert.show();
        } catch (Exception e) {
            // Print stack trace and show error alert
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save data: " + e.getMessage());
            alert.show();
        }
    }

    private void highlightButton(Button activeButton) {
        // Reset styles for all buttons
        individualPerformanceBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        performanceFormBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        attendanceBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");

        // Apply the active style to the selected button
        activeButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;");
    }




}
