package controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.publication;
import services.publicationservice;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javafx.scene.control.Label;
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
    private Label txtlabell;
    @FXML
    private Label TitreCtrl;

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
        Alert alert;

        // Vérifiez d'abord si le titre est en majuscules
        if (!titre.getText().equals(titre.getText().toUpperCase())) {
            TitreCtrl.setText("Le titre doit être en majuscules");
            TitreCtrl.setVisible(true);
            return;
        }

        // Ensuite, vérifiez si tous les champs sont remplis
        if(!titre.getText().isEmpty() && !contenu.getText().isEmpty() && !imagep.getText().isEmpty()) {
            String imagePath = imagep.getText();

            publication p = new publication(titre.getText(), contenu.getText(), imagePath);

            ps.create(p);
            onannuler(event);

        } else {
            txtlabell.setText("Veuillez renseigner tous les champs");
            txtlabell.setVisible(true);
        }
    }

    @FXML
    void onannuler(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/addpublication.fxml"));
            contenu.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }    }




}
