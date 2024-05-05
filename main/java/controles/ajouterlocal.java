package controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.local;
import services.localService;
import javafx.scene.image.Image;


import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class ajouterlocal {
    private final localService lSrv = new localService();
    public Button browseButton;
    public ImageView imageView;

    @FXML
    private Button ajouter;

    @FXML
    private Button annuler;

    @FXML
    private TextField nom;

    @FXML
    private TextField adresse;
    @FXML
    private ImageView imageRdv;


    @FXML
    private TextField imageR;
    @FXML
    void onajouter(ActionEvent event) throws SQLException {
        String nomLocal = nom.getText().trim();
        String adresseLocal = adresse.getText().trim();
        String imagePath = imageR.getText();
        if (nomLocal.isEmpty() || adresseLocal.isEmpty()) {
            showAlert("Attention", "Les champs nom et adresse ne peuvent pas être vides !");
            return;
        }

        if (!isAlpha(nomLocal)) {
            showAlert("Attention", "Le nom doit contenir uniquement des lettres alphabétiques !");
            return;
        }

        try {
            local nouveauLocal = new local();
            nouveauLocal.setNom(nomLocal);
            nouveauLocal.setAdresse(adresseLocal);
            nouveauLocal.setImage(imagePath);
            lSrv.create(nouveauLocal);
            nom.setText("");
            adresse.setText("");
            Parent root = FXMLLoader.load(getClass().getResource("/Listeloc.fxml"));
            ajouter.getScene().setRoot(root);
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du local : " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onannuler(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/listeloc.fxml"));
            nom.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isAlpha(String str) {
        return str.matches("[a-zA-Z]+");
    }
    @FXML
    void browseimage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {

            Image image = new Image(file.toURI().toString());
            imageRdv.setImage(image);
            imageR.setText(file.toURI().toString());
    }
    }
}


