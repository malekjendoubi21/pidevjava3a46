package controles;

import javafx.embed.swing.SwingFXUtils;
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
import toolkit.QRCodeGenerator;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;


public class cardpublication {
    private publication publication;
    publication data = publication.getInstance();

    @FXML
    private Button bt_comment;

    @FXML
    private ImageView imagepub;

    @FXML
    private HBox hbox;

    @FXML
    private Label contenu;


    @FXML
    private Label imagep;
    @FXML
    private Label id;

    @FXML
    private Button modifier;

    @FXML
    private Label titre;

    @FXML
    private Button supprimer;
    @FXML
    private Button displayQRButton;
    public ImageView qrcodeimage;
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

            try {
                InputStream inputStream = getClass().getResourceAsStream("/default_profile_image.png");
                Image defaultImage = new Image(inputStream);
                imagepub.setImage(defaultImage);
            } catch (NullPointerException e) {

                System.err.println("Image par défaut introuvable : " + e.getMessage());
            }
        }
    }
    @FXML
    void commenter(ActionEvent event) {
        try {
            data.setId(Integer.parseInt(id.getText()));
            Parent root = FXMLLoader.load(getClass().getResource("/addcomentaire.fxml"));
            contenu.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }    }

    }


