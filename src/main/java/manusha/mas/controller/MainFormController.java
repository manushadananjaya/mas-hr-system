package manusha.mas.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import manusha.mas.util.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;

public class MainFormController {

    @FXML
    private Button btnsignin;

    @FXML
    private TextField txtuname;

    @FXML
    private PasswordField txtpword;

    @FXML
    private ComboBox<String> roleSelect;

    private double x = 0;
    private double y = 0;

    public static class getData {
        public static String username;
    }

    @FXML
    private void initialize() {
        btnsignin.setOnAction(this::handleSignIn);
    }

    @FXML
    void handleSignIn(ActionEvent event) {
        String username = txtuname.getText().trim();
        String password = txtpword.getText().trim();
        String selectedRole = roleSelect.getValue();

        // Check if fields are empty
        if (username.isEmpty() || password.isEmpty() || selectedRole == null) {
            showAlert(AlertType.WARNING, "Validation Error", "Please fill in all fields and select a role.");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ? AND role = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, selectedRole);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                getData.username = resultSet.getString("username");

                showAlert(AlertType.INFORMATION, "Login Successful", "Welcome, " + username + "!");

                btnsignin.getScene().getWindow().hide();

                // Load the appropriate dashboard based on the selected role
                String fxmlFile = switch (selectedRole) {
                    case "Admin" -> "/view/AdminDashboard.fxml";
                    case "IE" -> "/view/IEDashboard.fxml";
                    case "Training Manager" -> "/view/TrainingManagerDashboard.fxml";
                    case "Training Assistant" -> "/view/TrainingAssistantDashboard.fxml";
                    default -> throw new IllegalStateException("Unexpected role: " + selectedRole);
                };

                Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                // Setting up dragging functionality (optional)
                root.setOnMousePressed(e -> {
                    x = e.getSceneX();
                    y = e.getSceneY();
                });

                root.setOnMouseDragged(e -> {
                    stage.setX(e.getScreenX() - x);
                    stage.setY(e.getScreenY() - y);
                });


                stage.setScene(scene);
                stage.setTitle(selectedRole + " Dashboard");
                stage.show();

            } else {
                showAlert(AlertType.ERROR, "Login Failed", "Invalid username, password, or role.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "An error occurred while connecting to the database.");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Load Error", "An error occurred while loading the dashboard.");
        }
    }

    // Method to show alerts
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // No header text
        alert.setContentText(message);
        alert.showAndWait();
    }
}