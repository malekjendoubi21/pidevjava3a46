package controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import models.SessionManager;
import models.reclamation;
import models.user;
import org.controlsfx.control.Notifications;
import services.ReclamationService;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;

public class add {

    private final ReclamationService rs=new ReclamationService();

    @FXML
    private Button ajouter;

    @FXML
    private Button annuler;

    @FXML
    private TextField contenu;

    @FXML
    private TextField sujet;
    private user currentUser;

    @FXML
    private TextField userid;

    @FXML
    void addReclamation(ActionEvent event) throws SQLException {
        try {
            currentUser = SessionManager.getCurrentUser();

            rs.create(new reclamation(sujet.getText(),contenu.getText(),currentUser.getId()));
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Product added successfully!");
        alert.showAndWait();

        contenu.setText("");
        sujet.setText("");
        userid.setText("");
        /*Email email = Email.create()
                .from("redblazers007@gmail.com")
                .to("ahmed.bousnina99@hotmail.fr")
                .subject("Nouvelle Reclamation")
                .textMessage("Vous avez recu une nouvelle reclamation.");
            SmtpServer smtpServer = MailServer.create()
                    .host("http://mail.com")
                    .port(21)
                    .buildSmtpMailServer();
            SendMailSession session = smtpServer.createSession();
            session.open();
            session.sendMail(email);
            session.close();*/
            sendmail();
            Notifications notifications=Notifications.create();
            notifications.text("reclamation ajoutée ");
            notifications. title("Tache succes");
            notifications.show();




        } catch (NumberFormatException e) {
            showErrorAlert("Invalid user id! Please enter a valid integer.");
        }

    }
    @FXML
    void fromAddToList(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/homeb.fxml"));
            contenu.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void sendmail(){
        try {
            URI uri = new URI("http://127.0.0.1:8000/reclamation/jav");
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(uri);
                } else {
                    System.out.println("Desktop browsing is not supported on this platform.");
                }
            } else {
                System.out.println("Desktop is not supported on this platform.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


}

}
