package manusha.mas.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class IEDashboardController {

    @FXML
    private AnchorPane mainContentPane;

    @FXML
    private AnchorPane requestedDetailsPane;

    @FXML
    private AnchorPane trainingPerformancePane;

    @FXML
    private Button requestingFormBtn;

    @FXML
    private Button requestedDetailsBtn;

    @FXML
    private Button trainingPerfoBtn;

    @FXML
    private void initialize() {
        // Initially show the main content pane and hide others
        showPane(mainContentPane);

        // Highlight the requesting form button by default
        highlightButton(requestingFormBtn);
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

    /**
     * Shows the specified pane and hides all others.
     *
     * @param paneToShow The pane to make visible.
     */
    private void showPane(AnchorPane paneToShow) {
        // Set all panes to invisible
        mainContentPane.setVisible(false);
        requestedDetailsPane.setVisible(false);
        trainingPerformancePane.setVisible(false);

        // Set the specified pane to visible
        paneToShow.setVisible(true);
    }

    /**
     * Highlights the specified button and resets styles for all others.
     *
     * @param activeButton The button to highlight.
     */
    private void highlightButton(Button activeButton) {
        // Reset styles for all buttons
        requestingFormBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        requestedDetailsBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        trainingPerfoBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");

        // Highlight the active button
        activeButton.setStyle("-fx-background-color: #4caf50; -fx-text-fill: white;");
    }
}
