package controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.SessionManager;
import models.user;
import services.userservice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.List;

public class back {
    private user currentUser;
    userservice us = new userservice();

    @FXML
    private Text labelprenom;

    @FXML
    private Text nomlabel;
    @FXML
    private Label nbdocteur;

    @FXML
    private Label nbpatient;
    @FXML
    private Button pro;
    private EventObject event;

    @FXML
    private ImageView profileimage;


    public void pro(javafx.event.ActionEvent actionEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profile.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        currentUser = SessionManager.getCurrentUser();

        if (currentUser != null) {

            displayUserInfo(currentUser);
            displayUserImage(currentUser);
            updateNumberOfDoctors();
            updateNumberOfPatients();
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
    public void updateNumberOfDoctors() {
        try {
            List<user> users = us.read();
            int numberOfDoctors = 0;
            for (user user : users) {
                if (user.getRoles().contains("ROLE_DOCTEUR")) {

                    numberOfDoctors++;
                }
            }
            nbdocteur.setText(String.valueOf(numberOfDoctors));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateNumberOfPatients() {
        try {
            List<user> users = us.read(); // Assuming us is your UserService instance
            int numberOfPatients = 0;
            for (user user : users) {

                if (user.getRoles().contains("ROLE_PATIENT")) {

                    numberOfPatients++;
                }
            }
            nbpatient.setText(String.valueOf(numberOfPatients));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void affuser(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/listuser.fxml"));
        labelprenom.getScene().setRoot(root);
    }

    public void aduser(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/adduser.fxml"));
        labelprenom.getScene().setRoot(root);
    }
}
