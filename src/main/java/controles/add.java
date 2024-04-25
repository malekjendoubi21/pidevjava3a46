package controles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import models.publication;
import services.publicationservice;
import utils.MyDatabase;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;


public class add {

    publicationservice ps=new publicationservice();

    @FXML
    private GridPane gridpane;
    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnUpdate;

    @FXML
    private TextField contenu;

    @FXML
    private TextField imagep;
    @FXML
    private TextField search;
    @FXML
    private TextField titre;
    @FXML
    private TableView<publication> af;
    private TableColumn<publication, String> publicationcontentColumn;
    private TableColumn<publication, String> publicationtitleColumn;
    private TableColumn<publication, String> publicationimageColumn;



    @FXML
    void Add(ActionEvent event) throws SQLException {

        // Vérification si les champs ne sont pas vides
        if(titre.getText().isEmpty() || contenu.getText().isEmpty() || imagep.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
        } else {
            // Ajout de la publication
            publication p = new publication(titre.getText(), contenu.getText(), imagep.getText());

             ps.create(p);

            // Affichage d'une confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Publication ajoutée avec succès.");
            alert.showAndWait();
        }
        ObservableList<publication> list = FXCollections.observableList(ps.read());
        af.setItems(list);
    }




    @FXML
    void initialize() throws SQLException, IOException {

        int column = 0;
        int row = 1;

        for (publication p : ps.read()) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/cardpublication.fxml"));
            HBox cardBox = fxmlLoader.load();

            cardpublication cardController = fxmlLoader.getController();
            cardController.setData(p);
            if (column == 2) {
                column = 0;
                row++;
            }
            gridpane.add(cardBox, column++, row);
            GridPane.setMargin(cardBox,new javafx.geometry.Insets(10, 10, 10, 10));

        }
    }

    @FXML
    void ajoutpublication(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ajouterpublication.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    @FXML
    public void rowClick(javafx.scene.input.MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) { // Check if it's a single click
            publication selection = af.getSelectionModel().getSelectedItem();

            if (selection != null) {
                // Set the values to the text fields and date picker
                titre.setText(selection.getTitre());
                contenu.setText(selection.getContenu());
                imagep.setText(selection.getImage());


            }
        }
    }



/*

    @FXML
    void Delete(ActionEvent event) throws SQLException {
        publication selection = af.getSelectionModel().getSelectedItem();
        ps.delete(selection.getId());
        ObservableList<publication> list = FXCollections.observableList(ps.read());
        af.setItems(list);
    }

    @FXML
    void Update(ActionEvent event) throws SQLException {

        publication selection = af.getSelectionModel().getSelectedItem();
        selection.setTitre(titre.getText());
        selection.setContenu(contenu.getText());
        selection.setImage(image.getText());

        ps.update(selection);
        ObservableList<publication> list = FXCollections.observableList(ps.read());
        af.setItems(list);


    }*/

}