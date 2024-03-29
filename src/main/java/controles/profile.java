package controles;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.SessionManager;
import models.user;
import org.controlsfx.control.Notifications;
import services.userservice;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;

public class profile {
    private final userservice userservice;
    @FXML
    private ImageView userImageView;

    @FXML
    private TextField emailField;

    @FXML
    private TextField genderField;

    @FXML
    private Label idLabel;

    @FXML
    private TextField nomField;

    @FXML
    private TextField numtelField;

    @FXML
    private TextField prenomField;

    private user currentUser;

    public profile() {
        this.userservice = new userservice(); // Ou injectez UserService si nécessaire
    }

    public profile(userservice userservice) {
        this.userservice = userservice;
    }

    public void initialize() {
        // Initialise les données de l'utilisateur à partir de SessionManager
        currentUser = SessionManager.getCurrentUser();
        System.out.println("IDdddddddddd: ");

        if (currentUser != null) {
            System.out.println("ccccccccccccc: ");

            displayUserInfo(currentUser);
            displayUserImage(currentUser); // Affichez l'image de l'utilisateur

            System.out.println("ID: " + currentUser.getId());
            System.out.println("Email: " + currentUser.getEmail());
            // Ajoutez des instructions similaires pour les autres attributs de l'utilisateur
        } else {
            System.out.println("Utilisateur actuel non défini.");
        }
    }

    private void displayUserInfo(user user) {
        idLabel.setText(String.valueOf(user.getId()));
        emailField.setText(user.getEmail());
        nomField.setText(user.getNom());
        prenomField.setText(user.getPrenom());
        numtelField.setText(String.valueOf(user.getNumtel()));
        genderField.setText(user.getGender());
    }




    @FXML
    void saveProfile(ActionEvent event) {
        try {
            // Mettre à jour les données de l'utilisateur avec les valeurs des champs
            currentUser.setEmail(emailField.getText());
            currentUser.setNom(nomField.getText());
            currentUser.setPrenom(prenomField.getText());
            currentUser.setNumtel(Integer.parseInt(numtelField.getText()));
            currentUser.setGender(genderField.getText());

            // Appel de la méthode de mise à jour dans le service utilisateur
            userservice.update(currentUser);
        } catch (SQLException e) {
            // Gérer l'exception de manière appropriée (affichage d'une boîte de dialogue d'erreur, par exemple)
            e.printStackTrace();
        }
    }



    private void clearFields() {
        emailField.clear();
        nomField.clear();
        prenomField.clear();
        numtelField.clear();
        genderField.clear();
        idLabel.setText("");
    }

    public void saveProfile(javafx.event.ActionEvent actionEvent) {
        try {
            // Mettre à jour les données de l'utilisateur avec les valeurs des champs
            currentUser.setEmail(emailField.getText());
            currentUser.setNom(nomField.getText());
            currentUser.setPrenom(prenomField.getText());
            currentUser.setNumtel(Integer.parseInt(numtelField.getText()));
            currentUser.setGender(genderField.getText());

            // Appel de la méthode de mise à jour dans le service utilisateur
            userservice.update(currentUser);
            showNotification();
        } catch (SQLException e) {
            // Gérer l'exception de manière appropriée (affichage d'une boîte de dialogue d'erreur, par exemple)
            e.printStackTrace();
        }

    }
    private void displayUserImage(user user) {
        String imagePath = user.getProfileImage(); // Obtenez le chemin de l'image de l'utilisateur
        if (imagePath != null && !imagePath.isEmpty()) {
            Image image = new Image(imagePath);
            userImageView.setImage(image);
        } else {
            // Affichez une image par défaut si le chemin de l'image est vide ou nul
            Image defaultImage = new Image(getClass().getResourceAsStream("default_profile_image.png"));
            userImageView.setImage(defaultImage);
        }
    }
    @FXML
    void annuler(javafx.event.ActionEvent event) {
        try {
            // Charger le fichier FXML d'adduser
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/front.fxml"));
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
    private void showNotification() {
        try {
               Image image = new Image("/logo.png");

            Notifications notifications = Notifications.create();
             notifications.graphic(new ImageView(image));
            notifications.text("Profile updated successfully");
            notifications.title("Success Message");
            notifications.hideAfter(Duration.seconds(4));
            notifications.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
