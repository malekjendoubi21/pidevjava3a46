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
import models.local;
import services.localService;

import java.io.IOException;
import java.io.InputStream;

public class cardlocal {
    local data = local.getInstance();

    @FXML
    private HBox hbox;

    @FXML
    private Label adresse;

    @FXML
    private ImageView imageRdv;

    @FXML
    private Label imageR;

    @FXML
    private Label id;

    @FXML
    private Label nom;

    @FXML
    private Button supprimer;

    private localService ls = new localService();

    @FXML
    void del(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete ?");
        alert.setContentText("Are you sure you want to delete this ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                ls.delete(Integer.parseInt(id.getText()));
                refreshView();
            }
        });
    }

    @FXML
    void modifier(ActionEvent event) throws IOException {
        // Update data object
        data.setId(Integer.parseInt(id.getText()));
        data.setNom(nom.getText());
        data.setAdresse(adresse.getText());

        // Load editlocal.fxml
        Parent root = FXMLLoader.load(getClass().getResource("/editlocal.fxml"));
        hbox.getScene().setRoot(root);
    }

    public void setData(local l) {
        nom.setText(l.getNom());
        adresse.setText(l.getAdresse());
        id.setText(String.valueOf(l.getId()));
        displayUserImage(l);
    }

    private void displayUserImage(local local) {
        String imagePath = local.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            Image image1 = new Image(imagePath);
            imageRdv.setImage(image1);
        } else {
            try {
                InputStream inputStream = getClass().getResourceAsStream("/default_profile_image.png");
                Image defaultImage = new Image(inputStream);
                imageRdv.setImage(defaultImage);
            } catch (NullPointerException e) {
                System.err.println("Default image not found: " + e.getMessage());
            }
        }
    }

    private void refreshView() {
        try {
            // Load the same FXML file to refresh the view
            Parent root = FXMLLoader.load(getClass().getResource("/listeloc.fxml"));
            hbox.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
