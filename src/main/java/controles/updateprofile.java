package controles;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.SessionManager;
import models.user;
import org.controlsfx.control.Notifications;
import services.userservice;
import toolkit.QRCodeGenerator;

import javax.imageio.ImageIO;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class updateprofile {
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
    private ImageView qrCodeImageView;
    @FXML
    private TextField numtelField;

    @FXML
    private TextField prenomField;

    private user currentUser;

    public updateprofile() {
        this.userservice = new userservice(); // Ou injectez UserService si nécessaire
    }

    public updateprofile(userservice userservice) {
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
             displayuserqrcode(currentUser);
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





    private void displayuserqrcode(user user )
    {
        String userData =
                "nom et prenom : " +user.getNom() + " " + user.getPrenom() + "\n" +
                "Gender: " + user.getGender() + "\n" +
                "Email: " + user.getEmail()+ "\n"+
                "Roles"+ user.getRoles()
                ;
        BufferedImage qrCodeImage = QRCodeGenerator.generateQRCode(userData, 200, 200);

        // Afficher le QR code où vous le souhaitez, peut-être dans un ImageView
        if (qrCodeImage != null) {
            Image fxImage = SwingFXUtils.toFXImage(qrCodeImage, null);
            qrCodeImageView.setImage(fxImage);
        }




    }
    @FXML
    void annuler(javafx.event.ActionEvent event) {
        try {
            // Charger le fichier FXML d'adduser
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home2.fxml"));
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

    @FXML
    private void handleDownloadButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer l'image");

        // Ajoutez ici les filtres pour spécifier le type d'image à enregistrer, si nécessaire
        // fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PNG", "*.png"));

        // Afficher la boîte de dialogue "Enregistrer sous"
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                // Convertir l'ImageView en BufferedImage
                javafx.scene.image.Image image = qrCodeImageView.getImage();
                java.awt.image.BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

                // Enregistrer l'image dans le fichier spécifié
                ImageIO.write(bufferedImage, "png", file);
            } catch (IOException ex) {
                // Gérer les erreurs d'entrée/sortie ici
                ex.printStackTrace();
            }
        }
    }

    public void handleDownloadButton(javafx.event.ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer l'image");

        // Ajoutez ici les filtres pour spécifier le type d'image à enregistrer, si nécessaire
         fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PNG", "*.png"));

        // Afficher la boîte de dialogue "Enregistrer sous"
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                // Convertir l'ImageView en BufferedImage
                javafx.scene.image.Image image = qrCodeImageView.getImage();
                java.awt.image.BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

                // Enregistrer l'image dans le fichier spécifié
                ImageIO.write(bufferedImage, "png", file);
            } catch (IOException ex) {
                // Gérer les erreurs d'entrée/sortie ici
                ex.printStackTrace();
            }
        } else {
            System.out.println("Aucun fichier sélectionné.");
        }

    }
}
