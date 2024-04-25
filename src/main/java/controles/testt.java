package controles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.control.ListView;
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
public class testt implements Initializable {

    @FXML
    private HBox hbox;
    private ReclamationService rs = new ReclamationService();
    private ReponseService rss = new ReponseService();

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
}