package controles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.AnchorPane;
import services.rendezvouzService;
import models.rendezvouz;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class Liste {
    @FXML
    private TextField search;
    @FXML
    private TextField daterdv;
    @FXML
    private TextField email;
    @FXML
    private GridPane gridpane;
    @FXML
    private HBox hbox;
    @FXML
    private TableView<rendezvouz> table;
    private TableColumn<rendezvouz, Integer> idColumn;
    private TableColumn<rendezvouz, Timestamp> daterdvColumn;
    private TableColumn<rendezvouz, String> emailColumn;
    private TableColumn<rendezvouz, Integer> localColumn;
    private rendezvouzService rdvs = new rendezvouzService();

    @FXML
    void initialize() {
        try {
            ObservableList<rendezvouz> list = FXCollections.observableList(rdvs.read());

            idColumn = new TableColumn<>("ID");
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

            daterdvColumn = new TableColumn<>("Date rdv");
            daterdvColumn.setCellValueFactory(new PropertyValueFactory<>("daterdv"));

            emailColumn = new TableColumn<>("Email");
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

            localColumn = new TableColumn<>("Local ID");
            localColumn.setCellValueFactory(new PropertyValueFactory<>("local_id"));

            //table.getColumns().addAll(idColumn, daterdvColumn, emailColumn, localColumn);

            FilteredList<rendezvouz> filteredData = new FilteredList<>(list, b -> true);
            search.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(rendezvouz -> {
                    if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
                        return true;
                    }
                    String searchKeyword = newValue.toLowerCase();
                    return rendezvouz.getEmail().toLowerCase().contains(searchKeyword)
                            || rendezvouz.getDaterdv().toString().toLowerCase().contains(searchKeyword);
                });
            });

            int column = 0;
            int row = 1;
            for (rendezvouz r : rdvs.read()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/cardRdv.fxml"));
                AnchorPane cardBox = fxmlLoader.load();

                cardRdv cardRdv = fxmlLoader.getController(); // Correction ici
                cardRdv.setData(r);
                if (column == 3) {
                    column = 0;
                    row++;
                }
                gridpane.add(cardBox, column++, row);
                GridPane.setMargin(cardBox,new javafx.geometry.Insets(10, 10, 10, 10));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onmodifier(ActionEvent event) {
        rendezvouz selection = table.getSelectionModel().getSelectedItem();
        if (selection == null) {
            return;
        }

        String nouvelleDateStr = daterdv.getText();
        String nouvelEmail = email.getText();

        if (nouvelleDateStr.isEmpty() || nouvelEmail.isEmpty()) {
            System.out.println("Veuillez remplir tous les champs.");
            return;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date parsedDate = dateFormat.parse(nouvelleDateStr);
            Timestamp nouvelleDate = new Timestamp(parsedDate.getTime());

            selection.setDaterdv(nouvelleDate);
            selection.setEmail(nouvelEmail);

            rdvs.update(selection);

            table.refresh();

            System.out.println("Rendez-vous mis à jour avec succès !");
        } catch (ParseException e) {
            System.out.println("Erreur lors de la conversion de la date : " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du rendez-vous : " + e.getMessage());
        }
    }


    @FXML
    void onsupprimer(ActionEvent event) {
        rendezvouz selection = table.getSelectionModel().getSelectedItem();
        if (selection == null) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Confirm Deletion");
        alert.setContentText("Are you sure you want to delete this item?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    rdvs.delete(selection.getId());
                    table.setItems(FXCollections.observableList(rdvs.read()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    void rowclick(MouseEvent event) {
        rendezvouz selection = table.getSelectionModel().getSelectedItem();
        if (selection != null) {
            email.setText(selection.getEmail());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String formattedDate = formatter.format(selection.getDaterdv().toLocalDateTime());
            daterdv.setText(formattedDate);
        }
    }


    @FXML
    void onajouter(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/test.fxml"));
        hbox.getScene().setRoot(root);
    }

    @FXML
    void gotordv(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/liste.fxml"));
        hbox.getScene().setRoot(root);
    }

    @FXML
    void onmodifer(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/editrdv.fxml"));
        hbox.getScene().setRoot(root);
    }

    @FXML
    void chn(KeyEvent event) throws SQLException, IOException {
        List<rendezvouz> rendezvouzs = rdvs.ffxd(search.getText());
        gridpane.getChildren().clear();
        int column = 0;
        int row = 1;
        for (rendezvouz r : rendezvouzs) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/cardRdv.fxml"));
            AnchorPane cardBox = fxmlLoader.load();

            cardRdv cardRdv = fxmlLoader.getController(); // Correction ici
            cardRdv.setData(r);
            if (column == 3) {
                column = 0;
                row++;
            }
            gridpane.add(cardBox, column++, row);
            GridPane.setMargin(cardBox,new javafx.geometry.Insets(10, 10, 10, 10));
        }
    }
}
