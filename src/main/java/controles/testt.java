package controles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ListView;
import models.notif;
import models.reclamation;
import services.ReclamationService;
import javafx.scene.layout.HBox;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import services.ReponseService;
import javafx.scene.layout.GridPane;
import services.notifservice;

public class testt implements Initializable {

    @FXML
    private HBox hbox;
    private ReclamationService rs = new ReclamationService();
    private ReponseService rss = new ReponseService();
    private notifservice ns = new notifservice();
    @FXML
    private Button add;
    @FXML
    private GridPane gridpane;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<reclamation> reclamations = null;
        try {
            reclamations = rs.read();

        for (reclamation r : reclamations) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/card.fxml"));
            HBox cardBox = fxmlLoader.load();

            card cardController = fxmlLoader.getController();
            cardController.setData(r);
            hbox.getChildren().add(cardBox);
        }
            reclamations=rs.read();
            int column = 0;
            int row = 1;
            for (reclamation r : reclamations) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/card.fxml"));
                HBox cardBox = fxmlLoader.load();

                card cardController = fxmlLoader.getController();
                cardController.setData(r);
                if (column == 3) {
                    column = 0;
                    row++;
                }
                gridpane.add(cardBox, column++, row);
                GridPane.setMargin(cardBox,new javafx.geometry.Insets(10, 10, 10, 10));
            }
            List<notif> notifs=ns.read();
            if (cond(notifs,reclamations)!=-1){
                Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                informationAlert.setTitle("Nouvelle reponse");
                informationAlert.setHeaderText("Vous avez recu une nouvelle reponse");
                informationAlert.setContentText("Un administrateur a repondu a une de vos reclamations.");
                informationAlert.showAndWait();
                ns.delete(cond(notifs,reclamations));
            }




        }
        catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void gotoadd(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/add.fxml"));
        hbox.getScene().setRoot(root);
    }

    public int cond(List<notif> n, List<reclamation> reclamations) {
        for (notif notif : n) {
            for (reclamation reclamation : reclamations) {
                if (notif.getRecid()==reclamation.getId()) {
                    return notif.getId();
                }
            }
        }
        return -1;
    }
}