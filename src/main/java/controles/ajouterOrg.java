package controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Organisation;
import services.OrganisationService;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;




public class ajouterOrg {
    private final OrganisationService os=new OrganisationService();

    @FXML
    private Label emailErrorLabel;
    @FXML
    private Label adresseLabel;

    @FXML
    private Button ajouter;

    @FXML
    private TextField email;

    @FXML
    private Label emailLabel;

    @FXML
    private TextField nom;

    @FXML
    private Label nomLabel;

    @FXML
    private TextField num_tel;

    @FXML
    private Label num_telLabel;

    @FXML
    private TextField orgImage;

    @FXML
    private ImageView orgImageView;

    @FXML
    private ImageView ima;

    @FXML
    private TextField adresse;


    @FXML
    private Button retour;


    @FXML
    void BrowseImage(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            Image image = new Image(file.toURI().toString());

            orgImageView.setImage(image);

            orgImage.setText(file.toURI().toString());

        }
    }

    @FXML
    void gotoDon(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/listDon.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    @FXML
    void gotoOrg(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/listOrg.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    @FXML
    void ajouter(ActionEvent event) {


        if (nom.getText().isEmpty() || email.getText().isEmpty() || adresse.getText().isEmpty() || num_tel.getText().isEmpty()) {

            Alert emptyFieldsAlert = new Alert(Alert.AlertType.WARNING);
            emptyFieldsAlert.setTitle("Warning");
            emptyFieldsAlert.setHeaderText(null);
            emptyFieldsAlert.setContentText("Please fill in all fields.");
            emptyFieldsAlert.showAndWait();
            return;
        }

        String nomText = nom.getText();

        String emailText = email.getText();

        String numTelText = num_tel.getText();



        try {

            Organisation o=new Organisation();
            o.setNom(nomText);
            o.setEmail(emailText);
            o.setAdresse(adresse.getText());
            o.setNum_tel(numTelText);
            String imagePath = orgImage.getText();
            o.setImage(imagePath);
            os.create(o);

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Organisation added successfully!");
            Optional<ButtonType> result = successAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Parent root = FXMLLoader.load(getClass().getResource("/listOrg.fxml"));
                nom.getScene().setRoot(root);
            }
        } catch (SQLException e) {

            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("There was an error while adding the Organisation. Please try again later.");
            errorAlert.showAndWait();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }



    @FXML
    void retour(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/listOrg.fxml"));
            nom.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


}



