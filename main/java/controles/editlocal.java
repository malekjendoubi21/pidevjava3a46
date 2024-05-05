package controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.local;
import services.localService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class editlocal {

    private localService os = new localService();

    @FXML
    private TextField nom;

    @FXML
    private TextField adresse;

    @FXML
    private Button retour;

    @FXML
    private Button modifier;

    local data =local.getInstance();

    @FXML
    void initialize() throws SQLException {

        adresse.setText(data.getAdresse());
        nom.setText(data.getNom());

    }

    @FXML
    void retour(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Listeloc.fxml"));
        nom.getScene().setRoot(root);
        adresse.getScene().setRoot(root);

    }

    @FXML
    void update(ActionEvent event) throws SQLException, IOException {
        String nomLocal = nom.getText().trim();
        String adresseLocal = adresse.getText().trim();

        if (nomLocal.isEmpty() || adresseLocal.isEmpty()) {
            showAlert("Attention", "Les champs nom et adresse ne peuvent pas être vides !");
            return; // Arrêter l'exécution de la méthode si les champs sont vides
        }

        if (!isAlpha(nomLocal)) {
            showAlert("Attention", "Le nom doit contenir uniquement des lettres alphabétiques !");
            return;
        }

        try {
            data.setNom(nomLocal);
            data.setAdresse(adresseLocal);
            os.update(data);
            Parent root = FXMLLoader.load(getClass().getResource("/Listeloc.fxml"));
            modifier.getScene().setRoot(root);
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du local : " + e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isAlpha(String str) {
        return str.matches("[a-zA-Z]+");
    }

    public void initData(local l) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        nom.setText(l.getNom());
        adresse.setText(l.getAdresse());
        Parent root = fxmlLoader.load(getClass().getResource("/editlocal.fxml"));
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.show();
    }

    @FXML
    void goToDon(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/listDon.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    @FXML
    void goToDon2(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/listDon.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    @FXML
    void goToHome(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Home2.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    public void setData(local data) {
    }
}
