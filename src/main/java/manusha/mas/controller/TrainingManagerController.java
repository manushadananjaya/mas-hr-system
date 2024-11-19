package manusha.mas.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class TrainingManagerController {

    @FXML
    private Button SearchBtn;

    @FXML
    private TableColumn<?, ?> countColumn;

    @FXML
    private TextField designationField;

    @FXML
    private TextField genderField;

    @FXML
    private Button logOutBtn;

    @FXML
    private TableColumn<?, ?> moduleNoColumn;

    @FXML
    private TableColumn<?, ?> nameColumn;

    @FXML
    private TextField nameField;

    @FXML
    private ProgressBar operation1Bar;

    @FXML
    private Label operation1Label;

    @FXML
    private ProgressBar operation2Bar;

    @FXML
    private Label operation2Label;

    @FXML
    private ProgressBar operation3Bar;

    @FXML
    private Label operation3Label;

    @FXML
    private ProgressBar operation4Bar;

    @FXML
    private Label operation4Label;

    @FXML
    private ProgressBar operation5Bar;

    @FXML
    private Label operation5Label;

    @FXML
    private ProgressBar operation6Bar;

    @FXML
    private Label operation6Label;

    @FXML
    private TableColumn<?, ?> operationColumn;

    @FXML
    private BarChart<?, ?> performanceChart;

    @FXML
    private BarChart<?, ?> performanceDailyChart;

    @FXML
    private BarChart<?, ?> qualityChart;

    @FXML
    private TableColumn<?, ?> reqDateColumn;

    @FXML
    private Button requestedDetailsBtn;

    @FXML
    private AnchorPane requestedDetailsPane;

    @FXML
    private TableView<?> requestedDetailsTable;

    @FXML
    private Button requirementFormBtn;

    @FXML
    private AnchorPane requirementFormPane;

    @FXML
    private TextField searchField;

    @FXML
    private TableColumn<?, ?> sectionColumn;

    @FXML
    private TextField trainingField;

    @FXML
    private Button trainingPerformanceBtn;

    @FXML
    private AnchorPane trainingPerformancePane;

    @FXML
    void handleSearchEPF(ActionEvent event) {

    }


    public void initialize() {

        showPane(trainingPerformancePane);
        highlightButton(trainingPerformanceBtn);

    }

    @FXML
    private void trainingPerformanceBtn(ActionEvent event) {
        showPane(trainingPerformancePane);
        highlightButton(trainingPerformanceBtn);
    }

    @FXML
    private void requestedDetailsBtn(ActionEvent event) {
        showPane(requestedDetailsPane);
        highlightButton(requestedDetailsBtn);
    }

    @FXML
    private void requirementFormBtn(ActionEvent event) {
        showPane(requirementFormPane);
        highlightButton(requirementFormBtn);
    }

    private void showPane(AnchorPane pane) {
        trainingPerformancePane.setVisible(false);
        requestedDetailsPane.setVisible(false);
        requirementFormPane.setVisible(false);
        pane.setVisible(true);
    }

    private void highlightButton(Button activeButton) {
        // Reset styles for all buttons
        trainingPerformanceBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        requirementFormBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        requestedDetailsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");

        // Apply the active style to the selected button
        activeButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;");
    }


}
