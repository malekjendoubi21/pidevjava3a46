package controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import models.rendezvouz;
import services.localService;
import services.rendezvouzService;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class cardRdv {
    private rendezvouz data;
    private rendezvouzService rs = new rendezvouzService();
    private localService ls = new localService();

    @FXML
    private Label daterdv;

    @FXML
    private Label email;

    @FXML
    private HBox hbox;

    @FXML
    private Label id;

    @FXML
    private Label local_id;

    @FXML
    private Button update;

    @FXML
    private Button supprimer;

    @FXML
    void del(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete rendezvouz");
        alert.setContentText("Are you sure you want to delete this rendezvouz?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    rs.delete(Integer.parseInt(id.getText()));
                    Parent root = FXMLLoader.load(getClass().getResource("/liste.fxml"));
                    supprimer.getScene().setRoot(root);
                } catch (SQLException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    void update(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/editrdv.fxml"));
        Parent root = fxmlLoader.load();
        editrdv controller = fxmlLoader.getController();
        controller.initData(data);
        hbox.getScene().setRoot(root);
    }


    public void setData(rendezvouz d) throws SQLException {
        this.data = d;

        Timestamp timestamp = d.getDaterdv();
        if (timestamp != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String formattedDate = formatter.format(timestamp);
            daterdv.setText(formattedDate);

        }
        email.setText(d.getEmail());
        id.setText(String.valueOf(d.getId()));
        local_id.setText(ls.affichageOrg(d.getLocal_id()).getNom());

       // local_id.setText(String.valueOf(d.getLocal_id()));
    }
}

