package controles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.commentaire;
import models.publication;
import services.commentaireservice;
import services.commentaireservice;
import services.publicationservice;
import utils.MyDatabase;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;


public class addcomentaire {

    commentaireservice ps = new commentaireservice();
    publicationservice pss=new publicationservice();

    @FXML
    private TableView<commentaire> af;
    private TableColumn<commentaire, String> conetnucommentaire;
    private TableColumn<commentaire, String> signalementscommentaire;

    @FXML
    private TextField sign;

    @FXML
    private Button btn_addcomment;

    @FXML
    private Button btn_deletecomment;

    @FXML
    private Button btn_signal;

    @FXML
    private Button btn_updatecomment;

    @FXML
    private TextField contentcomment;

    @FXML
    private ComboBox<String> publication_id_choicebox;
    publication data = publication.getInstance();
    public static final String TWILIO_PHONE_NUMBER = "+13253265603";
    // Clé API de votre compte Twilio
    public static final String ACCOUNT_SID = "AC74d9a1a9a629af8a3d0580a2b6b5186a";
    // Clé secrète de votre compte Twilio
    public static final String AUTH_TOKEN = "8aaf6241e6e86f06a3c4898e4860e2d3";
    @FXML
    void initialize() throws SQLException {
       // ObservableList<commentaire> list = FXCollections.observableList(ps.read());
         ObservableList<commentaire> list = FXCollections.observableList(ps.readByPublicationId(data.getId()));
        af.setItems(list);
        conetnucommentaire = new TableColumn<>("contenu");
        signalementscommentaire = new TableColumn<>("signalements");
        conetnucommentaire.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        signalementscommentaire.setCellValueFactory(new PropertyValueFactory<>("signalements"));
        af.getColumns().addAll(conetnucommentaire ,signalementscommentaire);
        af.setItems(list);
        List<String> names = pss.read().stream()
                .map(publication::getTitre)
                .toList();
        publication_id_choicebox.setItems(FXCollections.observableArrayList(names));

    }
    @FXML
    void addComment(ActionEvent event) throws SQLException, IOException {

        /*String selectedPublication = publication_id_choicebox.getValue();


        if (selectedPublication == null || contentcomment.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs et sélectionner une publication.");
            alert.showAndWait();
        } else {
            commentaire c = new commentaire();
            c.setContenu(contentcomment.getText());
            c.setPublication_id(pss.searchh(selectedPublication).getId());

            commentaireservice cs = new commentaireservice();
            cs.create(c);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Commentaire ajouté avec succès.");
            alert.showAndWait();
        }*/
        commentaire c = new commentaire();
        c.setContenu(contentcomment.getText());
        c.setPublication_id(data.getId());
        ps.create(c);
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("/addcomentaire.fxml"));
        btn_addcomment.getScene().setRoot(root);
    }





    @FXML
    public void rowClick(javafx.scene.input.MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            commentaire selection = af.getSelectionModel().getSelectedItem();

            if (selection != null) {

                contentcomment.setText(selection.getContenu());
                sign.setText(String.valueOf(selection.getSignalements()));

            }
        }
    }


    public void deletecomment(ActionEvent actionEvent) throws SQLException {
        commentaire selection = af.getSelectionModel().getSelectedItem();
        ps.delete(selection.getId());
        ObservableList<commentaire> list = FXCollections.observableList(ps.read());
        af.setItems(list);

    }

    public void update(ActionEvent actionEvent) throws SQLException {
        commentaire selection = af.getSelectionModel().getSelectedItem();
        selection.setContenu(contentcomment.getText());

        ps.update(selection);
        ObservableList<commentaire> list = FXCollections.observableList(ps.read());
        af.setItems(list);


    }
    @FXML
    void signaler(ActionEvent event) throws SQLException, IOException {

        commentaire selection = af.getSelectionModel().getSelectedItem();
        if (selection != null) {

            selection.setSignalements(selection.getSignalements() + 1);

            ps.update(selection);

            if (selection.getSignalements() > 9) {

            //    sendTwilioMessage();
                ps.delete(selection.getId());
            }
        }
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("/addcomentaire.fxml"));
        btn_signal.getScene().setRoot(root);
    }
    /*private void sendTwilioMessage() {
        try {
            // Initialiser Twilio
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

            String toPhoneNumber = "+21653560823"; // Numéro du destinataire de la notification
            String msgContent = "WARNING: Your comment have been deleted."; // Texte du message

            // Envoyer le SMS
            Message message = Message.creator(
                            new PhoneNumber(toPhoneNumber),
                            new PhoneNumber(TWILIO_PHONE_NUMBER),
                            msgContent)
                    .create();

            System.out.println("Sent message w/ SID: " + message.getSid());

        } catch (ApiException e) {
            System.err.println("Error sending SMS: " + e.getMessage());
            // Afficher une boîte de dialogue d'erreur dans votre application JavaFX, logger l'erreur, etc.
        }
    }*/

    @FXML
    void annuler(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/add.fxml"));
            contentcomment.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }    }
    }














    

