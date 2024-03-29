package controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.SessionManager;
import models.user;
import services.userservice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.List;

public class back {
    private user currentUser;
    userservice us = new userservice();

    @FXML
    private Text labelprenom;

    @FXML
    private Text nomlabel;
    @FXML
    private Label nbdocteur;

    @FXML
    private Label nbpatient;
    @FXML
    private Button pro;
    private EventObject event;

    @FXML
    private ImageView profileimage;


    public void pro(javafx.event.ActionEvent actionEvent) {

        try {
            // Charger le fichier FXML d'adduser
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec le contenu de adduser
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle à partir de l'événement (actionEvent)
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Définir la nouvelle scène
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        // Initialise les données de l'utilisateur à partir de SessionManager
        currentUser = SessionManager.getCurrentUser();

        if (currentUser != null) {

            displayUserInfo(currentUser);
            displayUserImage(currentUser); // Affichez l'image de l'utilisateur
            updateNumberOfDoctors();
            updateNumberOfPatients();
        }
    }
    private void displayUserInfo(user user) {
        nomlabel.setText(user.getNom());
        labelprenom.setText(user.getPrenom());


    }

    private void displayUserImage(user user) {
        String imagePath = user.getProfileImage(); // Obtenez le chemin de l'image de l'utilisateur
        if (imagePath != null && !imagePath.isEmpty()) {
            Image image = new Image(imagePath);
            profileimage.setImage(image);
        } else {
            // Affichez une image par défaut si le chemin de l'image est vide ou nul
            Image defaultImage = new Image(getClass().getResourceAsStream("default_profile_image.png"));
            profileimage.setImage(defaultImage);
        }
    }
    public void updateNumberOfDoctors() {
        try {
            List<user> users = us.read(); // Assuming us is your UserService instance
            int numberOfDoctors = 0;
            for (user user : users) {
                if (user.getRoles().contains("ROLE_DOCTEUR")) {

                    numberOfDoctors++;
                }
            }
            nbdocteur.setText(String.valueOf(numberOfDoctors));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to calculate and display the number of patients
    public void updateNumberOfPatients() {
        try {
            List<user> users = us.read(); // Assuming us is your UserService instance
            int numberOfPatients = 0;
            for (user user : users) {

                if (user.getRoles().contains("ROLE_PATIENT")) {

                    numberOfPatients++;
                }
            }
            nbpatient.setText(String.valueOf(numberOfPatients));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }/*
    @FXML
    void recl(ActionEvent event, EventObject mouseEvent) {


    }*/

    public void recl(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/homeb.fxml"));
        nomlabel.getScene().setRoot(root);
    }
}

