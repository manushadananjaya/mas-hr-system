package manusha.busticket;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppIntializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainForm.fxml"));
        Object load = loader.load();

        Scene scene = new Scene((javafx.scene.Parent) load);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Bus Ticket Reservation System");
        primaryStage.show();

    }
}
