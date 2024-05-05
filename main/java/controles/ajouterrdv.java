package controles;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import models.local;
import models.rendezvouz;
import services.EmailService;
import services.localService;
import services.rendezvouzService;

import javax.mail.MessagingException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

public class ajouterrdv {
    private final rendezvouzService rdvs = new rendezvouzService();

    @FXML
    private Button ajouter;

    @FXML
    private Button annuler;

    @FXML
    private TextField daterdv;

    @FXML
    private TextField email;

    @FXML
    private ComboBox<String> localComboBox;

    @FXML
    private Label emaillabel;

    @FXML
    private Label daterdvlabel;

    @FXML
    void initialize() {

        try {
            localService ls = new localService();
            List<local> lsss = ls.read();
            List<String> names = lsss.stream()
                    .map(local::getNom)
                    .toList();
            localComboBox.setItems(FXCollections.observableArrayList(names));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void onajouter(ActionEvent event) {
        String dateString = daterdv.getText().trim();
        String emailString = email.getText().trim();
        String selectedLocal = localComboBox.getValue();

        if (dateString.isEmpty() || emailString.isEmpty() || selectedLocal == null) {
            showAlert("Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        if (!isValidDateTimeFormat(dateString, "yyyy-MM-dd HH:mm")) {
            showAlert("Format de date invalide", "Utilisez le format yyyy-MM-dd HH:mm pour la date.");
            return;
        }
        LocalDateTime dateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        if (dateTime.isBefore(LocalDateTime.now())) {
            showAlert("Date invalide", "La date doit être ultérieure à aujourd'hui.");
            return;
        }

        if (!isValidEmail(emailString)) {
            showAlert("Format d'email invalide", "Veuillez saisir une adresse email valide.");
            return;
        }

        try {
            Timestamp parsedTimestamp = Timestamp.valueOf(dateTime);

            rendezvouz rdv = new rendezvouz();
            rdv.setEmail(emailString);
            rdv.setDaterdv(parsedTimestamp);

            localService ls = new localService();
            rdv.setLocal_id(ls.getnomm(selectedLocal));

            rdvs.create(rdv);

            String htmlContent = "<html><body>Rendez-vous: " + rdv.getDaterdv() + "</body></html>";
            EmailService.sendEmailWithAttachmentAndHTML(rdv.getEmail(), "Confirmation de rendez-vous", htmlContent);
            onannuler(event);
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du rendez-vous : " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de Calendar.fxml : " + e.getMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    void onannuler(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/liste.fxml"));
            annuler.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private boolean isValidDateTimeFormat(String dateTimeString, String format) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            LocalDateTime.parse(dateTimeString, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
