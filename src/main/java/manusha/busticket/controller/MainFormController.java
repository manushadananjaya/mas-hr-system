package manusha.busticket.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import manusha.busticket.util.DatabaseConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EventObject;

import javafx.event.ActionEvent;



public class MainFormController {

    @FXML
    private Button btnsignin;

    @FXML
    private TextField txtpword;

    @FXML
    private TextField txtuname;


    @FXML
    private AnchorPane form_reg;

    @FXML
    private AnchorPane form_signin;


    @FXML
    private TextField register_confrimPassword;


    @FXML
    private Button register_signinBtn;


    @FXML
    private TextField register_password;

    @FXML
    private Button register_submitBtn;

    @FXML
    private TextField register_username;

    @FXML
    private Button btn_mainform_register;

    private double x= 0;
    private double y=0;

    public static class getData{
        public static String username;
    }

    @FXML
    private void initialize() {
        btnsignin.setOnAction(this::handleSignIn);
    }

    @FXML
    void handleSignIn(ActionEvent event) {
        handleSignIn();
    }

    private void handleSignIn() {
        String username = txtuname.getText().trim();
        String password = txtpword.getText().trim();

        // Check if username or password fields are empty
        if (username.isEmpty() || password.isEmpty()) {
            // Show warning alert if fields are empty
            showAlert(AlertType.WARNING, "Validation Error", "Please fill in all fields.");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                getData.username = resultSet.getString("username");

                // User found, handle successful login
                showAlert(AlertType.INFORMATION, "Login Successful", "Welcome, " + username + "!");
                btnsignin.getScene().getWindow().hide();

                // Clear the fields
                txtuname.clear();
                txtpword.clear();

                Parent root = FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml"));

                Stage stage = new Stage();

                Scene scene = new Scene(root);

                root.setOnMousePressed(e -> {
                    x = e.getSceneX();
                    y = e.getSceneY();
                });

                root.setOnMouseDragged(e -> {
                    stage.setX(e.getScreenX() - x);
                    stage.setY(e.getScreenY() - y);
                });

                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(scene);
                stage.setTitle("Dashboard");
                stage.show();



            } else {
                // User not found, handle login failure
                showAlert(AlertType.ERROR, "Login Failed", "Invalid username or password.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "An error occurred while connecting to the database.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to handle registration
    public void handleRegistration(){
        String username = register_username.getText().trim();
        String password = register_password.getText().trim();
        String confirmPassword = register_confrimPassword.getText().trim();

        // Check if any field is empty
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            // Show warning alert if fields are empty
            showAlert(AlertType.WARNING, "Validation Error", "Please fill in all fields.");
            return;
        }

        // Check if password and confirm password match
        if (!password.equals(confirmPassword)) {
            // Show warning alert if passwords do not match
            showAlert(AlertType.WARNING, "Validation Error", "Passwords do not match.");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                // Registration successful
                showAlert(AlertType.INFORMATION, "Registration Successful", "You have successfully registered.");
                signInForm((ActionEvent) new EventObject(new Object()));
            } else {
                // Registration failed
                showAlert(AlertType.ERROR, "Registration Failed", "An error occurred while registering.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "An error occurred while connecting to the database.");
        }
    }

    //go to register form when clicked on register button
    public void registerForm(ActionEvent event) {
        form_signin.setVisible(false);
        form_reg.setVisible(true);
    }

    //go to sign in form when clicked on sign in button
    public void signInForm(ActionEvent event) {
        form_signin.setVisible(true);
        form_reg.setVisible(false);
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