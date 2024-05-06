package tests;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;


public class MainFX extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // ff
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home2.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);

    }

}
