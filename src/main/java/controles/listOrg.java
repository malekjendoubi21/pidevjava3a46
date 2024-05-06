package controles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Don;
import models.Organisation;
import services.DataValidation;
import services.OrganisationService;
import javafx.scene.control.Label;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class listOrg {

    @FXML
    private TableView<Organisation> table;
    private TableColumn<Organisation, Integer> idColumn;
    private TableColumn<Organisation, String> nomColumn;
    private TableColumn<Organisation, String> emailColumn;
    private TableColumn<Organisation, String> adresseColumn;
    private TableColumn<Organisation, Integer> num_telColumn;
    private TableColumn<Organisation, String> userimageColumn;

    @FXML
    private TextField adresse;
    @FXML
    private TextField image;
    @FXML
    private Label imageLabel;
    @FXML
    private ImageView ima;

    @FXML
    private Button ajoutOrg;

    @FXML
    private Button delete;

    @FXML
    private TextField email;

    @FXML
    private TextField nom;

    @FXML
    private TextField num_tel;

    @FXML
    private Label emailLabel;
    @FXML
    private Label adresseLabel;
    @FXML
    private Label nomLabel;

    @FXML
    private Label num_telLabel;

    @FXML
    private TextField search;

    @FXML
    private Button update;

    @FXML
    private GridPane gridpane;
    private OrganisationService os = new OrganisationService();

    @FXML
    void initialize() throws SQLException, IOException {
        List<Organisation> organisationList = os.read();
        if (organisationList != null) {
            ObservableList<Organisation> list = FXCollections.observableList(organisationList);
            /*table.setItems(list);

            idColumn = new TableColumn<>("id");
            nomColumn = new TableColumn<>("nom");
            emailColumn = new TableColumn<>("email");
            adresseColumn = new TableColumn<>("adresse");
            num_telColumn = new TableColumn<>("num_tel");
            userimageColumn = new TableColumn<>("image");


            userimageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            adresseColumn.setCellValueFactory(new PropertyValueFactory<>("adresse"));
            num_telColumn.setCellValueFactory(new PropertyValueFactory<>("num_tel"));

            table.getColumns().addAll(idColumn, nomColumn, emailColumn, adresseColumn, num_telColumn,userimageColumn);*/


            FilteredList<Organisation> filteredData = new FilteredList<>(list, b -> true);


            search.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(organisation -> {
                    if (newValue == null || newValue.isEmpty() || newValue.isBlank()) {
                        return true;
                    }

                    String searchText = newValue.toLowerCase();

                    return organisation.getNom().toLowerCase().contains(searchText) ||
                            organisation.getEmail().toLowerCase().contains(searchText) ||
                            organisation.getAdresse().toLowerCase().contains(searchText) ||
                            String.valueOf(organisation.getNum_tel()).contains(searchText);
                });
            });

          /*  SortedList<Organisation> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(table.comparatorProperty());
            table.setItems(sortedData);
*/

            int column = 0;
            int row = 1;

            for (Organisation o : os.read()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/cardOrg.fxml"));
                HBox cardBox = fxmlLoader.load();

                CardOrg cardController = fxmlLoader.getController();
                cardController.setData(o);
                if (column == 2) {
                    column = 0;
                    row++;
                }
                gridpane.add(cardBox, column++, row);
                GridPane.setMargin(cardBox,new javafx.geometry.Insets(10, 10, 10, 10));

        }
    }}

    private void displayUserImage(Don don) {
        String imagePath = don.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            Image image = new Image(imagePath);
            ima.setImage(image);
        } else {
            Image defaultImage = new Image(getClass().getResourceAsStream("default_profile_image.png"));
            ima.setImage(defaultImage);
        }
    }


    @FXML
    void ajoutOrg(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ajouterOrg.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    @FXML
    void select(MouseEvent event) {
        Organisation selection = table.getSelectionModel().getSelectedItem();
        if (selection != null) {
            nom.setText(selection.getNom());
            email.setText(selection.getEmail());
            adresse.setText(selection.getAdresse());
            num_tel.setText(String.valueOf(selection.getNum_tel()));
            //displayUserImage(selection);
        }
    }

    private void displayUserImage(Organisation org) {
        String imagePath = org.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            Image image = new Image(imagePath);
            ima.setImage(image);
        } else {

         /*   Image defaultImage = new Image(getClass().getResourceAsStream("default_profile_image.png"));
            ima.setImage(defaultImage);*/
        }
    }
    public void selectImage(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();

        // Set extension filters
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.png, *.jpg, *.jpeg)", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show open file dialog
        Stage stage = (Stage) table.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {

            imageLabel.setText(file.getName());

        }
    }

    @FXML
    void update(ActionEvent event) throws SQLException {
      /* boolean adresseValidation = DataValidation.dataLength(adresse, adresseLabel, "Must be 15 characters", "15");
        boolean numericPhNumber = DataValidation.textNumericPhone(num_tel, num_telLabel, "Please only enter numbers from 0 - 9 and 8 digits exactly");
        boolean nomValidation = DataValidation.textAlphabet(nom, nomLabel, "Please only enter letters from a - z");
        boolean emailValidation = DataValidation.emailFormat(email, emailLabel, "Format must be name@emailaddress.com");

        if (adresseValidation && numericPhNumber && nomValidation && emailValidation) {*/
            Organisation selection = table.getSelectionModel().getSelectedItem();

            if (selection != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Confirm Update");
                alert.setContentText("Are you sure you want to update this Organisation?");


                ButtonType result = alert.showAndWait().orElse(ButtonType.CANCEL);


                if (result == ButtonType.OK) {
                    selection.setNom(nom.getText());
                    selection.setEmail(email.getText());
                    selection.setAdresse(adresse.getText());
                    selection.setNum_tel(num_tel.getText());

                    os.update(selection);
                    ObservableList<Organisation> list = FXCollections.observableList(os.read());
                    table.setItems(list);
                    nom.setText("");
                    adresse.setText("");
                    email.setText("");
                    num_tel.setText("");
             //  }
            }
        }
    }

    @FXML
    void toDelete(ActionEvent actionEvent) throws SQLException {
        Organisation selection = table.getSelectionModel().getSelectedItem();
        if (selection != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Confirm Deletion");
            alert.setContentText("Are you sure you want to delete the selected organisation?");

            ButtonType buttonTypeYes = new ButtonType("Yes");
            ButtonType buttonTypeNo = new ButtonType("No");

            alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);


            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == buttonTypeYes) {
                    try {
                        os.delete(selection.getId());
                        ObservableList<Organisation> list = FXCollections.observableList(os.read());
                        table.setItems(list);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select an organisation to delete.");
            alert.showAndWait();
        }
    }



    @FXML
    void goToDon(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/listDon.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    @FXML
    void goToOrg(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/listOrg.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }
}
