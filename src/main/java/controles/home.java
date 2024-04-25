package controles;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import models.PDFExporter;
import models.reclamation;
import org.w3c.dom.Text;
import services.DataValidation;
import services.ReclamationService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import services.ReponseService;
import javafx.scene.control.Label;
public class home {

    @FXML
    private Button delete;

    @FXML
    private Button edit;

    @FXML
    private TextField search;

    @FXML
    private Label sujetLabel;

    @FXML
    private Label contenuLabel;

    @FXML
    private TableView<reclamation> xd;
    private TableColumn<reclamation, Timestamp> useridColumn;
    private TableColumn<reclamation, Integer> idColumn;
    private TableColumn<reclamation, String> sujetColumn ;
    private TableColumn<reclamation, String> contenuColumn;
    private TableColumn<reclamation, Void> actions;
    @FXML
    private TextField contenu;
    @FXML
    private TextField sujet;
    @FXML
    private Button recher;
    @FXML
    private TextField repp;
    @FXML
    private TextField textdate;
    @FXML
    private Button stats;

    private ReclamationService rs = new ReclamationService();
    private ReponseService rss = new ReponseService();
    @FXML
    void initialize() throws SQLException {
        ObservableList<reclamation> list = FXCollections.observableList(rs.read());
        xd.setItems(list);
        idColumn = new TableColumn<>("id");
        sujetColumn = new TableColumn<>("sujet");
        contenuColumn = new TableColumn<>("contenu");
        useridColumn= new TableColumn<>("date");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        sujetColumn.setCellValueFactory(new PropertyValueFactory<>("sujet"));
        contenuColumn.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        useridColumn.setCellValueFactory(new PropertyValueFactory<>("date"));


        xd.getColumns().addAll(idColumn, sujetColumn, contenuColumn,useridColumn);
        xd.setItems(list);

        FilteredList<reclamation> filteredData = new FilteredList<>(list,b->true);
        search.textProperty().addListener((observable,oldValue,newValue ) -> {
            filteredData.setPredicate(reclamation -> {
                if (newValue.isEmpty() ||newValue.isBlank() || newValue == null){
                    return true;
                }
                String searchKeyword =newValue.toLowerCase();
                if( reclamation.getSujet().toLowerCase().indexOf(searchKeyword)>-1){
                    return true;
                }
                else if(reclamation.getContenu().toLowerCase().indexOf(searchKeyword)>-1){
                    return true;
                }
                else
                    return false;
            });

        });

        SortedList<reclamation> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(xd.comparatorProperty());
        xd.setItems(sortedData);
        ObservableList<reclamation> data = xd.getItems();
        PDFExporter.exportToPDF(data);
    }




    @FXML
    void testbtn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/add.fxml"));
        xd.getScene().setRoot(root);
    }

    @FXML
    public void rowClick(javafx.scene.input.MouseEvent mouseEvent) throws SQLException {
        if (mouseEvent.getClickCount() == 1) {
            reclamation selection = xd.getSelectionModel().getSelectedItem();

            if (selection != null) {
                sujet.setText(selection.getSujet());
                contenu.setText(selection.getContenu());
                repp.setText((rss.affichagerep(xd.getSelectionModel().getSelectedItem().getId())).getResponse());
                textdate.setText((rss.affichagerep(xd.getSelectionModel().getSelectedItem().getId())).getDateresponse().toString());
            }
        }
    }
    @FXML
    public void maj(javafx.event.ActionEvent actionEvent) throws SQLException {

        boolean sujetAlphabetic = DataValidation.textAlphabet(sujet, sujetLabel, "Please only enter letters from a - z");
        boolean sujetEmpty = DataValidation.textFieldIsNull(sujet, sujetLabel, "Should not be empty");
        boolean contenuEmpty = DataValidation.textFieldIsNull(contenu, contenuLabel, "Should not be empty");

        boolean sujetValid = !sujetEmpty && sujetAlphabetic;
        boolean contenuValid = !contenuEmpty;

        if (sujetValid && contenuValid) {
            reclamation selection = xd.getSelectionModel().getSelectedItem();
            selection.setSujet(sujet.getText());
            selection.setContenu(contenu.getText());
            rs.update(selection);
            ObservableList<reclamation> list = FXCollections.observableList(rs.read());
            xd.setItems(list);

            // Clear the error messages from the labels
            sujetLabel.setText("");
            contenuLabel.setText("");
        }
    }
    @FXML
    public void del(javafx.event.ActionEvent actionEvent) throws SQLException {
        reclamation selection = xd.getSelectionModel().getSelectedItem();
        if(repp.getText().equals("pas de reponse :/") || (repp.getText() == null))
        {
            rs.delete(selection.getId());
            ObservableList<reclamation> list = FXCollections.observableList(rs.read());
            xd.setItems(list);
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de suppression");
            alert.setHeaderText(null);
            alert.setContentText("Supression Invalide. Vérifier que vous avez Séléctionné une réclamation sans réponse.");
            alert.showAndWait();
        }
    }
    @FXML
    public void recherche(javafx.event.ActionEvent actionEvent) throws SQLException {
        rs.rech(search.getText());
        ObservableList<reclamation> list = FXCollections.observableList(rs.rech(search.getText()));
        xd.setItems(list);
    }

    @FXML
    void navstats(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/stats.fxml"));
        xd.getScene().setRoot(root);
    }



}