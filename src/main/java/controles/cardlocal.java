package controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.local;
import services.localService;
import java.io.IOException;
import java.sql.SQLException;


public class cardlocal {
    local data = local.getInstance();



    @FXML
    private HBox hbox;

    @FXML
    private Label adresse;



    @FXML
    private Label id;

    @FXML
    private Label nom;

    @FXML
    private Button supprimer;
    private localService ls = new localService();

    @FXML
    void del(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete ?");
        alert.setContentText("Are you sure you want to delete this ?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    ls.delete(Integer.parseInt(id.getText()));
                    Parent root = FXMLLoader.load(getClass().getResource("/listeloc.fxml"));
                    supprimer.getScene().setRoot(root);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @FXML
    void modifier(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/editlocal.fxml"));
        VBox cardBox = fxmlLoader.load();
        data.setId(Integer.parseInt(id.getText()));
        data.setNom(nom.getText());
        data.setAdresse(adresse.getText());
        Parent root = fxmlLoader.load(getClass().getResource("/editlocal.fxml"));
        hbox.getScene().setRoot(root);


    }



    public void setData(local l){
        nom.setText(l.getNom());
        adresse.setText(l.getAdresse());
        id.setText(String.valueOf(l.getId()));
    }

}
