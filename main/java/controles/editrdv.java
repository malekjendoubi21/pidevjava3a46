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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.rendezvouz;
import services.rendezvouzService;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class editrdv {

    private rendezvouzService rs = new rendezvouzService();
    private rendezvouz data;

    @FXML
    private Button annuler;

    @FXML
    private TextField daterdv;

    @FXML
    private Label daterdvlabel;

    @FXML
    private TextField email;

    @FXML
    private Label emaillabel;

    @FXML
    private ComboBox<String> localComboBox;

    @FXML
    private Button modifier;

    @FXML
    void initialize() {
        if (data != null) {
            Timestamp timestamp = data.getDaterdv();
            if (timestamp != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:");
                String formattedDate = formatter.format(timestamp);
                daterdv.setText(formattedDate);
            }
            email.setText(data.getEmail());
        }
    }

    @FXML
    void update(ActionEvent event) throws SQLException, IOException, ParseException {
        String dateString = daterdv.getText().trim();
        String emailString = email.getText().trim();

        if (dateString.isEmpty() || emailString.isEmpty()) {
            showAlert("Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date parsedDate;
        try {
            parsedDate = formatter.parse(dateString);
        } catch (ParseException e) {
            showAlert("Format de date invalide", "Utilisez le format yyyy-MM-dd HH:mm pour la date.");
            return;
        }
        Timestamp timestamp = new Timestamp(parsedDate.getTime());

        data.setEmail(emailString);
        data.setDaterdv(timestamp);
        rs.update(data);

        navback(event);
    }

    @FXML
    void goRdv(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/liste.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    @FXML
    void navback(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/liste.fxml"));
            daterdv.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void initData(rendezvouz d) {
        this.data = d;
        email.setText(d.getEmail());
        daterdv.setText(d.getDaterdv().toString());
    }
}
