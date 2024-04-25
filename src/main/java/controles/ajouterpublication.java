package controles;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.publication;
import services.publicationservice;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class ajouterpublication {
    publicationservice ps=new publicationservice();

    @FXML
    private Button ajouter;

    @FXML
    private Button annuler;

    @FXML
    private TextField contenu;

    @FXML
    private TextField imagep;

    @FXML
    private ImageView imagepub;

    @FXML
    private TextField titre;

    @FXML
    void browseimage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            Image image1 = new Image(file.toURI().toString());
            imagepub.setImage(image1);

            imagep.setText(file.toURI().toString());
        }
    }

    @FXML
    void onajouter(ActionEvent event) throws SQLException, IOException {
        // Vérification si les champs ne sont pas vides
        if(titre.getText().isEmpty() || contenu.getText().isEmpty() || imagep.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
        } else {
            // Ajout de la publication
            String imagePath = imagep.getText();

            publication p = new publication(titre.getText(), contenu.getText(), imagePath);

            ps.create(p);
            onannuler(event);

            // Affichage d'une confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Publication ajoutée avec succès.");
            alert.showAndWait();
        }

    }

    @FXML
    void onannuler(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/add.fxml"));
            contenu.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }    }




}
