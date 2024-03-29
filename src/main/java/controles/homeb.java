package controles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.PDFExporter;
import models.reclamation;
import services.ReclamationService;
import services.ReponseService;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
public class homeb {

    @FXML
    private Button delete;

    @FXML
    private Button edit;

    @FXML
    private TextField search;

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
    public void maj(ActionEvent actionEvent) throws SQLException {
        reclamation selection = xd.getSelectionModel().getSelectedItem();
        selection.setSujet(sujet.getText());
        selection.setContenu(contenu.getText());
        rs.update(selection);
        ObservableList<reclamation> list = FXCollections.observableList(rs.read());
        xd.setItems(list);
    }
    @FXML
    public void del(ActionEvent actionEvent) throws SQLException {
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
    public void recherche(ActionEvent actionEvent) throws SQLException {
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