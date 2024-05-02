package controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import models.SessionManager;
import models.user;

import java.io.IOException;
import java.util.EventObject;
import java.util.Objects;

public class homeController {
    @FXML
    private Text nomlabel;
    private user currentUser;

    @FXML
    private Button pro;

    @FXML
    private ImageView profileimage;
    private EventObject event;
    @FXML
    private Text labelprenom;

    @FXML
    void goToDon(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/listDon.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }
    public void update(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/profile.fxml"));
        nomlabel.getScene().setRoot(root);
    }
    @FXML
    void goToDon2(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/listDon.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }
    public void initialize() {
        currentUser = SessionManager.getCurrentUser();

        if (currentUser != null) {

            displayUserInfo(currentUser);
            displayUserImage(currentUser);

        }
    }
    private void displayUserInfo(user user) {
        nomlabel.setText(user.getNom());
        labelprenom.setText(user.getPrenom());


    }

    private void displayUserImage(user user) {
        String imagePath = user.getProfileImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            Image image = new Image(imagePath);
            profileimage.setImage(image);
        } else {
            Image defaultImage = new Image(getClass().getResourceAsStream("default_profile_image.png"));
            profileimage.setImage(defaultImage);
        }
    }
}
