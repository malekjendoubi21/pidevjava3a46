package controles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import models.local;
import services.localService;

import java.io.IOException;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Listeloc {

    @FXML
    private TextField adresse;

    @FXML
    private Button ajouter;

    @FXML
    private Button modifier;

    @FXML
    private TextField nom;

    @FXML
    private TextField search;

    @FXML
    private Button supprimer;

    @FXML
    private GridPane gridpane;
    @FXML
    private TableView<local> table;
    private TableColumn<local, Integer> idColumn;

    private TableColumn<local, String> nomColumn;
    private TableColumn<local, String> adresseColumn;

    private localService lSrv = new localService();

    @FXML
    void initialize() throws SQLException, IOException {
        ObservableList<local> list = FXCollections.observableList(lSrv.read());
        //table.setItems(list);

        idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        nomColumn = new TableColumn<>("Nom");
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));

        adresseColumn = new TableColumn<>("Adresse");
        adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));



        FilteredList<local> filteredData = new FilteredList<>(list, b -> true);

        int column = 0;
        int row = 1;

        for (local l : lSrv.read()) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/cardlocal.fxml"));
            HBox cardBox = fxmlLoader.load();

            cardlocal cardController = fxmlLoader.getController();
            cardController.setData(l);
            if (column == 2) {
                column = 0;
                row++;
            }
            gridpane.add(cardBox, column++, row);
            GridPane.setMargin(cardBox,new javafx.geometry.Insets(10, 10, 10, 10));

        }

    }

    @FXML
    void onajouter(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/ajouterlocal.fxml"));
        ajouter.getScene().setRoot(root);
    }

    @FXML
    void onmodifier(ActionEvent event) {
        local selection = table.getSelectionModel().getSelectedItem();
        if (selection != null) {
            String nouveauNom = nom.getText();
            String nouvelleAdresse = adresse.getText();

            if (nouveauNom.isEmpty() || nouvelleAdresse.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Attention");
                alert.setHeaderText(null);
                alert.setContentText("Les champs nom et adresse ne peuvent pas être vides !");
                alert.showAndWait();
                return;
            }

            selection.setNom(nouveauNom);
            selection.setAdresse(nouvelleAdresse);

            try {
                lSrv.update(selection);
                table.refresh(); // Actualiser la table après la modification
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    void onsupprimer(ActionEvent event) {
        local selection = table.getSelectionModel().getSelectedItem();
        if (selection != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Confirm Deletion");
            alert.setContentText("Are you sure you want to delete this item?");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        lSrv.delete(selection.getId());
                        ObservableList<local> list = FXCollections.observableList(lSrv.read());
                        table.setItems(list);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @FXML
    void rowclick(MouseEvent event) {
        local selection = table.getSelectionModel().getSelectedItem();
        if (selection != null) {
            nom.setText(selection.getNom());
            adresse.setText(selection.getAdresse());
        }
    }
}
