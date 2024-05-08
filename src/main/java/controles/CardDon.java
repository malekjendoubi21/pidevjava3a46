package controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import models.Don;
import services.DonService;
import services.OrganisationService;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public class CardDon {
    Don data =Don.getInstance();

    @FXML
    private Label orgid;

    @FXML
    private HBox hbox;


    @FXML
    private Label image;

    @FXML
    private Label description;

    @FXML
    private Label type;

    @FXML
    private Button modifier;

    @FXML
    private Label id;

    @FXML
    private ImageView orgImage;

    @FXML
    private Label montant;

    @FXML
    private Label nom;

    @FXML
    private Label prenom;

    @FXML
    private Label email;
    @FXML
    private Label organisation;

    @FXML
    private ImageView imageDon;

    @FXML
    private Label imageD;


    @FXML
    private Button supprimer;
    private DonService ds = new DonService();

    private OrganisationService os = new OrganisationService();


    @FXML
    void del(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete Don");
        alert.setContentText("Are you sure you want to delete this Don?");


        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {

                    ds.delete(Integer.parseInt(id.getText()));
                    Parent root = FXMLLoader.load(getClass().getResource("/listDonB.fxml"));
                    supprimer.getScene().setRoot(root);


                } catch (SQLException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }




    @FXML
    void modifier(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/editDon.fxml"));
        AnchorPane cardBox = fxmlLoader.load();
        data.setId(Integer.parseInt(id.getText()));
        data.setNom(nom.getText());
        data.setPrenom(prenom.getText());
        data.setEmail(email.getText());
        data.setDescription(description.getText());
        data.setMontant(Integer.parseInt(montant.getText()));
        //data.setOrganisation_id(Integer.parseInt(orgid.getText()));
        data.setType(type.getText());
        Parent root = fxmlLoader.load(getClass().getResource("/editDon.fxml"));
        hbox.getScene().setRoot(root);
    }


    public void setData(Don d) throws SQLException {
        nom.setText(d.getNom());
        prenom.setText(String.valueOf(d.getPrenom()));
        email.setText(d.getEmail());
        description.setText(d.getDescription());
        type.setText(d.getType());
        montant.setText(String.valueOf(d.getMontant()));
        id.setText(String.valueOf(d.getId()));
        organisation.setText(os.affichageOrg(d.getOrganisation_id()).getNom());
        System.out.println(os.affichageOrg(d.getOrganisation_id()).getNom());
        displayUserImage(d);
    }

    private void displayUserImage(Don Don) {
        String imagePath = Don.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            Image image1 = new Image(imagePath);
            imageDon.setImage(image1);
        } else {

            try {
                InputStream inputStream = getClass().getResourceAsStream("/default_profile_image.png");
                Image defaultImage = new Image(inputStream);
                imageDon.setImage(defaultImage);
            } catch (NullPointerException e) {

                System.err.println("Image par défaut introuvable : " + e.getMessage());
            }
        }
    }

}
