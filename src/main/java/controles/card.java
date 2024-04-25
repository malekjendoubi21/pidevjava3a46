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

public class card {
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
    private Label userid;
    private ReponseService rss = new ReponseService();
    private ReclamationService rs = new ReclamationService();
    public void setData(reclamation r) throws SQLException {
        text.setText(r.getSujet());
        textt.setText(r.getContenu());
        texttt.setText(r.getDate().toString());
        rep.setText((rss.affichagerep(r.getId())).getResponse());
        textdate.setText((rss.affichagerep(r.getId())).getDateresponse().toString());
        idd.setText(String.valueOf(r.getId()));
        userid.setText(String.valueOf(r.getUser_id()));
        if(!rep.getText().equals("pas de reponse :/") && (rep.getText() != null)){
            edit.setVisible(false);
            delete.setVisible(false);
        }
    }
    @FXML
    void modifier(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/edittt.fxml"));
        VBox cardBox = fxmlLoader.load();
        data.setId(Integer.parseInt(idd.getText()));
        data.setSujet(text.getText());
        data.setContenu(textt.getText());
        data.setUser_id(Integer.parseInt(userid.getText()));
        data.setDate(null);
        Parent root = fxmlLoader.load(getClass().getResource("/edittt.fxml"));
        hbox.getScene().setRoot(root);

    }

    @FXML
    void supprimer(ActionEvent event) throws SQLException, IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Supprimer reclamation");
        alert.setContentText("Are you sure you want to delete this item?");


        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {

                    if(rep.getText().equals("pas de reponse :/") || (rep.getText() == null))
                    {
                        rs.delete(Integer.parseInt(idd.getText()));
                        Parent root = FXMLLoader.load(getClass().getResource("/testtt.fxml"));
                        hbox.getScene().setRoot(root);
                    }


                } catch (SQLException e) {
                    e.printStackTrace();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

}
