package controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.publication;
import services.publicationservice;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;


public class cardpublication {
    publication data = publication.getInstance();

    @FXML
    private ImageView imagepub;

    @FXML
    private HBox hbox;

    @FXML
    private Label contenu;


    @FXML
    private TextField imagep;
    @FXML
    private Label id;

    @FXML
    private Button modifier;

    @FXML
    private Label titre;

    @FXML
    private Button supprimer;
    private publicationservice ps = new publicationservice();

    @FXML
    void del(ActionEvent event) {



                try {
                    ps.delete(Integer.parseInt(id.getText()));
                    Parent root = FXMLLoader.load(getClass().getResource("/add.fxml"));
                    supprimer.getScene().setRoot(root);
                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
            }



    @FXML
    void modifier(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/editpublication.fxml"));
        VBox cardBox = fxmlLoader.load();
        data.setId(Integer.parseInt(id.getText()));
        data.setContenu(contenu.getText());
        data.setTitre(titre.getText());
        Parent root = fxmlLoader.load(getClass().getResource("/editpublication.fxml"));
        hbox.getScene().setRoot(root);


    }



    public void setData(publication l){
        titre.setText(l.getTitre());
        contenu.setText(l.getContenu());

        id.setText(String.valueOf(l.getId()));
       displayUserImage(l);
    }
    private void displayUserImage(publication publication) {
        String imagePath = publication.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            Image image1 = new Image(imagePath);
            imagepub.setImage(image1);
        } else {
            // Chargez une image par défaut à partir des ressources de votre application
            try {
                InputStream inputStream = getClass().getResourceAsStream("/default_profile_image.png");
                Image defaultImage = new Image(inputStream);
                imagepub.setImage(defaultImage);
            } catch (NullPointerException e) {
                // Affichez un message d'erreur si l'image par défaut n'est pas trouvée
                System.err.println("Image par défaut introuvable : " + e.getMessage());
            }
        }
    }


}