package controles;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.SessionManager;
import models.user;

import java.io.IOException;
import java.util.EventObject;

public class front {

    @FXML
    private Text nomlabel;
    private user currentUser;

    @FXML
    private Button pro;

    @FXML
    private ImageView profileimage;
    private EventObject event;
    @FXML
    private Text labelprenom;

/*
    @FXML
    void pro(ActionEvent event) {

        try {
            // Charger le fichier FXML d'adduser
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateprofile.fxml"));
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



    }*/

    public void pro(javafx.event.ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateprofile.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        currentUser = SessionManager.getCurrentUser();

        if (currentUser != null) {

            displayUserInfo(currentUser);
            displayUserImage(currentUser);

        }
    }
    private void displayUserInfo(user user) {
        nomlabel.setText(user.getNom());
        labelprenom.setText(user.getPrenom());


    }

    private void displayUserImage(user user) {
        String imagePath = user.getProfileImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            Image image = new Image(imagePath);
            profileimage.setImage(image);
        } else {
            Image defaultImage = new Image(getClass().getResourceAsStream("default_profile_image.png"));
            profileimage.setImage(defaultImage);
        }
    }

}
