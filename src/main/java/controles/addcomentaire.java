package controles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.commentaire;
import models.publication;
import services.commentaireservice;
import services.commentaireservice;
import services.publicationservice;
import utils.MyDatabase;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;


public class addcomentaire {

    commentaireservice ps = new commentaireservice();
    publicationservice pss=new publicationservice();
    @FXML
    private TableView<commentaire> af;
    private TableColumn<commentaire, String> conetnucommentaire;
    private TableColumn<commentaire, String> signalementscommentaire;

    @FXML
    private TextField sign;

    @FXML
    private Button btn_addcomment;

    @FXML
    private Button btn_deletecomment;

    @FXML
    private Button btn_updatecomment;

    @FXML
    private TextField contentcomment;

    @FXML
    private ComboBox<String> publication_id_choicebox;
    @FXML
    void initialize() throws SQLException {
        ObservableList<commentaire> list = FXCollections.observableList(ps.read());
        af.setItems(list);
        conetnucommentaire = new TableColumn<>("contenu");
        signalementscommentaire = new TableColumn<>("signalements");
        conetnucommentaire.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        signalementscommentaire.setCellValueFactory(new PropertyValueFactory<>("signalements"));
        af.getColumns().addAll(conetnucommentaire ,signalementscommentaire);
        af.setItems(list);

        List<String> names = pss.read().stream()
                .map(publication::getTitre)
                .toList();
        publication_id_choicebox.setItems(FXCollections.observableArrayList(names));



    }
    @FXML
    void addComment(ActionEvent event) throws SQLException {
        // Obtenez la publication sélectionnée à partir du ChoiceBox
        String selectedPublication = publication_id_choicebox.getValue();

        // Vérification si les champs ne sont pas vides
        if (selectedPublication == null || contentcomment.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs et sélectionner une publication.");
            alert.showAndWait();
        } else {
            commentaire c = new commentaire();
            c.setContenu(contentcomment.getText());
            c.setPublication_id(pss.searchh(selectedPublication).getId());


            // Ajout du commentaire
            commentaireservice cs = new commentaireservice();
            cs.create(c);

            // Affichage d'une confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Commentaire ajouté avec succès.");
            alert.showAndWait();
        }

    }





    @FXML
    public void rowClick(javafx.scene.input.MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) { // Check if it's a single click
            commentaire selection = af.getSelectionModel().getSelectedItem();

            if (selection != null) {
                // Set the values to the text fields and date picker
                contentcomment.setText(selection.getContenu());
                sign.setText(String.valueOf(selection.getSignalements()));




            }
        }
    }


    public void deletecomment(ActionEvent actionEvent) throws SQLException {
        commentaire selection = af.getSelectionModel().getSelectedItem();
        ps.delete(selection.getId());
        ObservableList<commentaire> list = FXCollections.observableList(ps.read());
        af.setItems(list);
    }

    public void update(ActionEvent actionEvent) throws SQLException {
        commentaire selection = af.getSelectionModel().getSelectedItem();
        selection.setContenu(contentcomment.getText());
  //      selection.setSignalements(Integer.parseInt(sign.getText()));

        ps.update(selection);
        ObservableList<commentaire> list = FXCollections.observableList(ps.read());
        af.setItems(list);


    }

}











    

