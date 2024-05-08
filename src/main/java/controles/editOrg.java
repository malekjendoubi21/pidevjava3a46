package controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.Don;
import models.Organisation;
import services.DonService;
import services.OrganisationService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class editOrg {

    private OrganisationService os = new OrganisationService();

    @FXML
    private TextField nom;

    @FXML
    private TextField email;

    @FXML
    private TextField num_tel;

    @FXML
    private TextField adresse;

    @FXML
    private TextField search;

    @FXML
    private Button retour;

    @FXML
    private Button modifier;

    Organisation data =Organisation.getInstance();

    @FXML
    void initialize() throws SQLException {
        nom.setText(data.getNom());
        adresse.setText(data.getAdresse());
        email.setText(data.getEmail());
        num_tel.setText(data.getNum_tel());
    }

    @FXML
    void retour(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/listOrg.fxml"));
        nom.getScene().setRoot(root);
        email.getScene().setRoot(root);
        adresse.getScene().setRoot(root);
        num_tel.getScene().setRoot(root);

    }

    /*
    @FXML
    void update(ActionEvent event) throws SQLException, IOException {

        data.setNom(nom.getText());
        data.setAdresse(adresse.getText());
        data.setEmail(email.getText());
        data.setNum_tel(num_tel.getText());
        System.out.println(data);
        os.update(data);
        Parent root = FXMLLoader.load(getClass().getResource("/listOrg.fxml"));
        modifier.getScene().setRoot(root);
    }
    */

    @FXML
    void update(ActionEvent event) throws SQLException, IOException {
        if (nom.getText().isEmpty() || adresse.getText().isEmpty() || email.getText().isEmpty() || num_tel.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        if (!nom.getText().matches("[a-zA-ZÀ-ÿ\\s']+")) {
            showAlert("Erreur", "Le nom doit contenir uniquement des lettres.");
            return;
        }


        if (!isValidEmail(email.getText())) {
            showAlert("Erreur", "Veuillez saisir une adresse e-mail valide.");
            return;
        }

        if (!num_tel.getText().matches("\\d{8}")) {
            showAlert("Erreur", "Le numéro de téléphone doit contenir exactement 8 chiffres.");
            return;
        }

        data.setNom(nom.getText());
        data.setAdresse(adresse.getText());
        data.setEmail(email.getText());
        data.setNum_tel(num_tel.getText());

        os.update(data);

        showSuccess("Succès", "Organisation mise à jour avec succès.");
        navback(event);

    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";

        return email.matches(emailPattern);
    }

    private boolean isAlpha(String str) {
        return str.matches("[a-zA-Z]+");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void initData(Organisation o) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        nom.setText(o.getNom());
        adresse.setText(o.getAdresse());
        email.setText(o.getEmail());
        num_tel.setText(o.getNum_tel());
        Parent root = fxmlLoader.load(getClass().getResource("/editOrg.fxml"));
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.show();
    }

    @FXML
    void goToDon(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/listDon.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    @FXML
    void goToDon2(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/listDon.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    @FXML
    void goToHome(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Home2.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
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

    @FXML
    void navback(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/listOrg.fxml"));
            nom.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

}
