package controles;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import models.SessionManager;
import models.user;
import toolkit.QRCodeGenerator;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class profile {

    @FXML
    private Label email;

    @FXML
    private Label nom;

    @FXML
    private Label prenom;


    private user currentUser;
    @FXML
    private ImageView userImageView;


    @FXML
    private ImageView qrCodeImageView;


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
//idLabel.setText(String.valueOf(user.getId()));
        email.setText(user.getEmail());
        nom.setText(user.getNom());
        prenom.setText(user.getPrenom());
        //numtelField.setText(String.valueOf(user.getNumtel()));
       // genderField.setText(user.getGender());



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


    public void update(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/updateprofile.fxml"));
        nom.getScene().setRoot(root);
    }
    public void back(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/home2.fxml"));
        prenom.getScene().setRoot(root);
    }
}
