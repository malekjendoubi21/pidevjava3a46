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
    private TextField imageO;

    @FXML
    private ImageView imageOrg;

    @FXML
    private ImageView ima;

    @FXML
    private TextField adresse;


    @FXML
    private Button retour;


    @FXML
    void browseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {

            Image image = new Image(file.toURI().toString());
            imageOrg.setImage(image);
            imageO.setText(file.toURI().toString());

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
            showAlert("Attention", "Veuillez remplir tous les champs.");
            return;
        }

        if (!nom.getText().matches("[a-zA-Z ]+")) {
            showAlert("Attention", "Le nom doit contenir uniquement des lettres et des espaces.");
            return;
        }

        if (!isValidEmail(email.getText())) {
            showAlert("Attention", "Veuillez saisir une adresse e-mail valide.");
            return;
        }

        if (!num_tel.getText().matches("\\d{8}")) {
            showAlert("Attention", "Le numéro de téléphone doit contenir exactement 8 chiffres.");
            return;
        }

        String nomText = nom.getText();
        String emailText = email.getText();
        String numTelText = num_tel.getText();

        try {
            Organisation o = new Organisation();
            o.setNom(nomText);
            o.setEmail(emailText);
            o.setAdresse(adresse.getText());
            o.setNum_tel(numTelText);
            o.setImage(imageO.getText());
            os.create(o);

            if (showConfirmation("Confirmation", "Voulez-vous ajouter cette organisation ?")) {
                showSuccessAlert("Organisation ajoutée avec succès !");
                retour(event);
            }
        } catch (SQLException e) {
            showErrorAlert("Erreur", "Une erreur s'est produite lors de l'ajout de l'organisation. Veuillez réessayer plus tard.");
        }
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        return email.matches(emailPattern);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour afficher une alerte d'erreur
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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


    @FXML
    void orgF(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/listOrgF.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    @FXML
    void donB(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/listDonB.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    @FXML
    void front(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Home2.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }


}



