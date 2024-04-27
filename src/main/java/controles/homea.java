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
import models.reponse;
import org.controlsfx.control.Notifications;
import org.w3c.dom.Text;
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
import javafx.scene.layout.GridPane;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
public class homea {

    @FXML
    private Button delete;

    @FXML
    private Button edit;

    @FXML
    private TextField search;

    @FXML
    private Button deleter;

    @FXML
    private Button editr;

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
    @FXML
    private Button repondre;

    @FXML
    private GridPane gridpane;

    private ReclamationService rs = new ReclamationService();
    private ReponseService rss = new ReponseService();
    @FXML
    private Button front;
    reclamation data =reclamation.getInstance();
    @FXML
    void initialize() throws SQLException, IOException {
        ObservableList<reclamation> list = FXCollections.observableList(rs.read());
        xd.setItems(list);
        //idColumn = new TableColumn<>("id");
        sujetColumn = new TableColumn<>("sujet");
        contenuColumn = new TableColumn<>("contenu");
        useridColumn= new TableColumn<>("date");
        //idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        sujetColumn.setCellValueFactory(new PropertyValueFactory<>("sujet"));
        contenuColumn.setCellValueFactory(new PropertyValueFactory<>("contenu"));
        useridColumn.setCellValueFactory(new PropertyValueFactory<>("date"));


        //xd.getColumns().addAll(idColumn, sujetColumn, contenuColumn,useridColumn);
        xd.getColumns().addAll(sujetColumn, contenuColumn,useridColumn);
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

        List<reclamation> reclamations=rs.read();
        int column = 0;
        int row = 1;
        for (reclamation r : reclamations) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/carda.fxml"));
            HBox cardBox = fxmlLoader.load();

            carda cardController = fxmlLoader.getController();
            cardController.setData(r);
            if (column == 2) {
                column = 0;
                row++;
            }
            gridpane.add(cardBox, column++, row);
            GridPane.setMargin(cardBox,new javafx.geometry.Insets(10, 10, 10, 10));
        }
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
        reclamation selection = xd.getSelectionModel().getSelectedItem();
        selection.setSujet(sujet.getText());
        selection.setContenu(contenu.getText());
        rs.update(selection);
        ObservableList<reclamation> list = FXCollections.observableList(rs.read());
        xd.setItems(list);
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


    @FXML
    void deleterep(ActionEvent event) throws SQLException {
        reclamation selection = xd.getSelectionModel().getSelectedItem();
        if (selection != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation Dialog");
            confirmationAlert.setHeaderText("Delete Item");
            confirmationAlert.setContentText("Are you sure you want to delete this item?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                rss.delete((rss.affichagerep(xd.getSelectionModel().getSelectedItem().getId())).getId());
                repp.setText("");
                textdate.setText("");
                Notifications notifications=Notifications.create();
                notifications.text("reponse supprimee");
                notifications. title("Tache succes");
                notifications.show();
            } else {
                Notifications notifications=Notifications.create();
                notifications.text("suppression annulee");
                notifications. title("Tache succes");
                notifications.show();
            }
        }
    }

    @FXML
    void editrep(ActionEvent event) throws SQLException {
        reclamation selection = xd.getSelectionModel().getSelectedItem();
        reponse rep=rss.affichagerep(xd.getSelectionModel().getSelectedItem().getId());
        rep.setResponse(repp.getText());
        rss.update(rep);
        Notifications notifications=Notifications.create();
        notifications.text("reponse modifiee");
        notifications. title("Tache succes");
        notifications.show();
    }

    @FXML
    void repondre(ActionEvent event) throws IOException {
        reclamation selection = xd.getSelectionModel().getSelectedItem();
        data.setId(selection.getId());
        Parent root = FXMLLoader.load(getClass().getResource("/repondre.fxml"));
        xd.getScene().setRoot(root);

    }

    @FXML
    void gotofront(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/home2.fxml"));
        xd.getScene().setRoot(root);
    }

    void refreshh(FilteredList<reclamation> filteredData) throws SQLException, IOException {
        gridpane.getChildren().clear();
        int column = 0;
        int row = 1;
        for (reclamation r : filteredData) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/carda.fxml"));
            HBox cardBox = fxmlLoader.load();

            carda cardController = fxmlLoader.getController();
            cardController.setData(r);
            if (column == 2) {
                column = 0;
                row++;
            }
            gridpane.add(cardBox, column++, row);
            GridPane.setMargin(cardBox,new javafx.geometry.Insets(10, 10, 10, 10));
        }
    }
    @FXML
    void ertyu(InputMethodEvent event) {
        System.out.println("change");
    }
    @FXML
    void chn(KeyEvent event) throws SQLException, IOException {
        List<reclamation> reclamations = rs.ffxd(search.getText());
        gridpane.getChildren().clear();
        int column = 0;
        int row = 1;
        for (reclamation r : reclamations) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/carda.fxml"));
            HBox cardBox = fxmlLoader.load();

            carda cardController = fxmlLoader.getController();
            cardController.setData(r);
            if (column == 2) {
                column = 0;
                row++;
            }
            gridpane.add(cardBox, column++, row);
            GridPane.setMargin(cardBox,new javafx.geometry.Insets(10, 10, 10, 10));
        }

    }
}