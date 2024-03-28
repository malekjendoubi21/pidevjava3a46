package controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
public class home {

    @FXML
    private Button prof;
    @FXML
    void testbtn(ActionEvent event) {

        try {
            // Charger le fichier FXML d'adduser
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/adduser.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec le contenu de adduser
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle à partir de l'événement
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Définir la nouvelle scène
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void doc(ActionEvent event) {
        try {
            // Charger le fichier FXML d'adduser
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/adddocteur.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec le contenu de adduser
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle à partir de l'événement
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Définir la nouvelle scène
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    void pat(ActionEvent event) {
        try {
            // Charger le fichier FXML d'adduser
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/addpatient.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec le contenu de adduser
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle à partir de l'événement
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Définir la nouvelle scène
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void prof(ActionEvent event) {
        try {
            // Charger le fichier FXML d'adduser
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec le contenu de adduser
            Scene scene = new Scene(root);

            // Obtenir la fenêtre principale à partir du nœud racine
            Stage stage = (Stage) ((Node) event.getTarget()).getScene().getWindow();

            // Définir la nouvelle scène
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void login(ActionEvent event) {
        try {
            // Charger le fichier FXML d'adduser
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec le contenu de adduser
            Scene scene = new Scene(root);

            // Obtenir la fenêtre principale à partir du nœud racine
            Stage stage = (Stage) ((Node) event.getTarget()).getScene().getWindow();

            // Définir la nouvelle scène
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }





    }

}
