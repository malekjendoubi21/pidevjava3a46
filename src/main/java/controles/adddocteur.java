package controles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.docteur;
import services.docteurservice;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
public class adddocteur {

    @FXML
    private Button ajouter;

    @FXML
    private DatePicker birth;

    @FXML
    private TextField email;

    @FXML
    private TextField gender;

    @FXML
    private TextField nom;

    @FXML
    private TextField numtel;

    @FXML
    private TextField password;

    @FXML
    private TextField prenom;

    @FXML
    private TextField profileImage;
    @FXML
    private ImageView userImageView;
    @FXML
    private TextField roles;

    @FXML
    private TextField specialite;
    private final docteurservice ds =new docteurservice();

    @FXML
    void adddocteur(ActionEvent event) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Création de l'utilisateur avec les données des champs
            docteur newUser = new docteur();
            newUser.setEmail(email.getText());
            try {
                String rolesJson = objectMapper.writeValueAsString(new String[]{"ROLE_DOCTEUR"});
                newUser.setRoles(rolesJson);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            newUser.setPassword(password.getText());
            newUser.setNom(nom.getText());
            newUser.setPrenom(prenom.getText());
            newUser.setNumtel(Integer.parseInt(numtel.getText())); // Conversion en int
            // Vous devrez peut-être implémenter la conversion pour LocalDateTime selon le format de votre champ
            LocalDate selectedDate = birth.getValue();
            if (selectedDate != null) {
                LocalDateTime birthDateTime = selectedDate.atStartOfDay();
                newUser.setBirth(LocalDate.from(birthDateTime));
            }
            String imagePath = profileImage.getText();
            newUser.setProfileImage(imagePath);
            newUser.setGender(gender.getText());
            newUser.setSpecialite(specialite.getText());

            // Ajout de l'utilisateur dans la base de données
            ds.create(newUser);

            // Affichage d'une alerte de succès
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("User added successfully!");
            alert.showAndWait();

            // Effacement des champs après ajout réussi
            clearFields();
        } catch (SQLException e) {
            // En cas d'erreur SQL, afficher une alerte d'erreur
            showErrorAlert("Error adding user: " + e.getMessage());
        } catch (NumberFormatException e) {
            showErrorAlert("Invalid phone number! Please enter a valid integer.");
        }
    }
    @FXML
    void browseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            userImageView.setImage(image);

            // Mettre à jour le champ de texte profileImage avec le chemin de l'image sélectionnée
            profileImage.setText(file.toURI().toString());
        }
    }
    private void clearFields() {
        nom.clear();
        prenom.clear();
        email.clear();
        numtel.clear();
        password.clear();
        gender.clear();
          birth.setValue(null);
        profileImage.clear();
        roles.clear();
        specialite.clear();
    }


    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    void annuler(ActionEvent event) {

        try {
            // Charger le fichier FXML d'adduser
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home.fxml"));
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
}