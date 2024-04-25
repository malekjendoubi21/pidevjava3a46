package controles;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.reclamation;
import javafx.scene.control.Button;
import org.w3c.dom.Node;
import services.ReclamationService;
import services.ReponseService;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class carda {
    reclamation data =reclamation.getInstance();
    @FXML
    private Button delete;

    @FXML
    private Button edit;

    @FXML
    private HBox hbox;
    @FXML
    private Label text;

    @FXML
    private Label textt;

    @FXML
    private Label texttt;

    @FXML
    private Label rep;
    @FXML
    private Label textdate;
    @FXML
    private Label idd;
    @FXML
    private Button repondre;
    @FXML
    private Button supp;
    @FXML
    private Label userid;
    @FXML
    private Button updatee;
    private ReponseService rss = new ReponseService();
    private ReclamationService rs = new ReclamationService();
    @FXML
    private Label repid;
    public void setData(reclamation r) throws SQLException {
        text.setText(r.getSujet());
        textt.setText(r.getContenu());
        texttt.setText(r.getDate().toString());
        rep.setText((rss.affichagerep(r.getId())).getResponse());
        textdate.setText((rss.affichagerep(r.getId())).getDateresponse().toString());
        idd.setText(String.valueOf(r.getId()));
        userid.setText(String.valueOf(r.getUser_id()));
        if(!rep.getText().equals("pas de reponse :/") && (rep.getText() != null)){
            repondre.setVisible(false);
        }
        else
        {
            updatee.setVisible(false);
            supp.setVisible(false);
        }
        repid.setText(String.valueOf((rss.affichagerep(r.getId())).getId()));
        repid.setVisible(false);

    }
    @FXML
    void repondre(ActionEvent event) throws IOException {
        data.setId(Integer.parseInt(idd.getText()));
        Parent root = FXMLLoader.load(getClass().getResource("/repondre.fxml"));
        hbox.getScene().setRoot(root);

    }

    @FXML
    void gotoupd(ActionEvent event) throws IOException {
        data.setId(Integer.parseInt(idd.getText()));
        Parent root = FXMLLoader.load(getClass().getResource("/editrep.fxml"));
        hbox.getScene().setRoot(root);

    }

    @FXML
    void suppp(ActionEvent event) throws SQLException, IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer reponse");
        alert.setContentText("Are you sure you want to delete this item?");


        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    rss.delete(Integer.parseInt(repid.getText()));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("/homea.fxml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                hbox.getScene().setRoot(root);
            }
        });

    }

}
