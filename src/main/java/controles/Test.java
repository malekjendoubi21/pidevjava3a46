package controles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import models.reclamation;
import services.ReclamationService;
import services.ReponseService;

import javax.swing.text.TableView;
import java.awt.*;
import java.sql.SQLException;

public class Test {
    @FXML
    private Label contenuLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private Label sujetLabel;
    @FXML
    private VBox vbox;
    private ReclamationService rs = new ReclamationService();
    private ReponseService rss = new ReponseService();
    @FXML
    void initialize() throws SQLException {
        ObservableList<reclamation> list = FXCollections.observableList(rs.read());

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        //vbox.setPadding(new Insets(10));

        for (reclamation item : list) {
            // Create a custom UI representation for each reclamation item
            Node reclamationNode = createReclamationNode(item);
            vbox.getChildren().add(reclamationNode);
        }


    }
    private Node createReclamationNode(reclamation item) {
        // Create a custom UI representation for each reclamation item
        Label idLabel = new Label("ID: " + item.getId());
        Label sujetLabel = new Label("Sujet: " + item.getSujet());
        Label contenuLabel = new Label("Contenu: " + item.getContenu());
        Label dateLabel = new Label("Date: " + item.getDate());

        VBox reclamationBox = new VBox();
        reclamationBox.setStyle("-fx-border-color: black; -fx-border-width: 1px; -fx-padding: 5px;");
        return reclamationBox;
    }




}
