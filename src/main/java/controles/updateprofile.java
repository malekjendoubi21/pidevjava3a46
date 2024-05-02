package controles;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
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
    private boolean isEditable = false;

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
        this.userservice = new userservice();
    }

    public updateprofile(userservice userservice) {
        this.userservice = userservice;
    }

    public void initialize() {
        currentUser = SessionManager.getCurrentUser();
        System.out.println("IDdddddddddd: ");

        if (currentUser != null) {
            System.out.println("ccccccccccccc: ");

            displayUserInfo(currentUser);
            displayUserImage(currentUser);
             displayuserqrcode(currentUser);
            lockFields();

            System.out.println("ID: " + currentUser.getId());
            System.out.println("Email: " + currentUser.getEmail());

        } else {
            System.out.println("Utilisateur actuel non défini.");
        }
    }

    private void displayUserInfo(user user) {
       // idLabel.setText(String.valueOf(user.getId()));
        emailField.setText(user.getEmail());
        nomField.setText(user.getNom());
        prenomField.setText(user.getPrenom());
        numtelField.setText(String.valueOf(user.getNumtel()));
        genderField.setText(user.getGender());



    }




    @FXML
    void saveProfile(ActionEvent event) {
        try {
            currentUser.setEmail(emailField.getText());
            currentUser.setNom(nomField.getText());
            currentUser.setPrenom(prenomField.getText());
            currentUser.setNumtel(Integer.parseInt(numtelField.getText()));
            currentUser.setGender(genderField.getText());
            userservice.update(currentUser);
        } catch (SQLException e) {
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

                String emailText = emailField.getText();
                if (emailText.isEmpty() || !emailText.contains("@") || !emailText.endsWith(".com")) {
                    showErrorAlert("Invalid email! Please enter a valid email address.");
                    return;
                }



                String numtelText = numtelField.getText();
                if (numtelText.isEmpty() || !numtelText.matches("^\\d{8}$") || Integer.parseInt(numtelText) < 0) {
                    showErrorAlert("Invalid phone number! Please enter a valid 8-digit positive integer.");
                    return;
                }

                String nomText = nomField.getText();
                if (nomText.isEmpty() || !nomText.matches("[a-zA-Z]+")) {
                    showErrorAlert("Invalid name! Please enter a valid name containing letters only.");
                    return;
                }

                String prenomText = prenomField.getText();
                if (prenomText.isEmpty() || !prenomText.matches("[a-zA-Z]+")) {
                    showErrorAlert("Invalid last name! Please enter a valid last name containing letters only.");
                    return;
                }
            currentUser.setEmail(emailField.getText());
            currentUser.setNom(nomField.getText());
            currentUser.setPrenom(prenomField.getText());
            currentUser.setNumtel(Integer.parseInt(numtelField.getText()));
            currentUser.setGender(genderField.getText());

            userservice.update(currentUser);
            showNotification();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private void displayUserImage(user user) {
        String imagePath = user.getProfileImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            Image image = new Image(imagePath);
            userImageView.setImage(image);
        } else {
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

        if (qrCodeImage != null) {
            Image fxImage = SwingFXUtils.toFXImage(qrCodeImage, null);
            qrCodeImageView.setImage(fxImage);
        }




    }
    @FXML
    void annuler(javafx.event.ActionEvent event) throws IOException {

            Parent root = FXMLLoader.load(getClass().getResource("/profile.fxml"));
            emailField.getScene().setRoot(root);

    }
    private void showNotification() {
        try {
               Image image = new Image("/log.png");

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

        // fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PNG", "*.png"));

        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                javafx.scene.image.Image image = qrCodeImageView.getImage();
                java.awt.image.BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

                ImageIO.write(bufferedImage, "png", file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void handleDownloadButton(javafx.event.ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer l'image");

         fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers PNG", "*.png"));

        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                javafx.scene.image.Image image = qrCodeImageView.getImage();
                java.awt.image.BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

                ImageIO.write(bufferedImage, "png", file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("Aucun fichier sélectionné.");
        }

    }
        private void showErrorAlert(String message) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    private void lockFields() {
        isEditable = false;
        emailField.setDisable(true);


    }
}
