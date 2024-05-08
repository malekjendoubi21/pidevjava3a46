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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Organisation;
import services.OrganisationService;
import java.io.IOException;
import java.io.InputStream;


public class CardOrg {
    Organisation data = Organisation.getInstance();

    @FXML
    private Label orgid;

    @FXML
    private HBox hbox;

    @FXML
    private Label adresse;

    @FXML
    private Button modifier;

    @FXML
    private Label id;

    @FXML
    private Label nom;

    @FXML
    private Label email;

    @FXML
    private Label num_tel;

    @FXML
    private Button supprimer;

    @FXML
    private ImageView imageOrg;

    @FXML
    private Label imageO;

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

                    os.delete(Integer.parseInt(id.getText()));
                    Parent root = FXMLLoader.load(getClass().getResource("/listOrg.fxml"));
                    supprimer.getScene().setRoot(root);


                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @FXML
    void modifier(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/editOrg.fxml"));
        VBox cardBox = fxmlLoader.load();
        data.setId(Integer.parseInt(id.getText()));
        data.setNom(nom.getText());
        data.setEmail(email.getText());
        data.setAdresse(adresse.getText());
        data.setNum_tel(num_tel.getText());
        Parent root = fxmlLoader.load(getClass().getResource("/editOrg.fxml"));
        hbox.getScene().setRoot(root);


    }



    public void setData(Organisation o){
        nom.setText(o.getNom());
        email.setText(o.getEmail());
        adresse.setText(o.getAdresse());
        num_tel.setText(o.getNum_tel());
        id.setText(String.valueOf(o.getId()));
        displayUserImage(o);
    }

    private void displayUserImage(Organisation Organisation) {
        String imagePath = Organisation.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            Image image1 = new Image(imagePath);
            imageOrg.setImage(image1);
        } else {

            try {
                InputStream inputStream = getClass().getResourceAsStream("/default_profile_image.png");
                Image defaultImage = new Image(inputStream);
                imageOrg.setImage(defaultImage);
            } catch (NullPointerException e) {

                System.err.println("Image par défaut introuvable : " + e.getMessage());
            }
        }
    }
}
