package controles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import models.publication;
import services.publicationservice;
import utils.MyDatabase;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import toolkit.QRCodeGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class add {

    public ImageView qrcodeimage;
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


        if(titre.getText().isEmpty() || contenu.getText().isEmpty() || imagep.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
        } else {

            publication p = new publication(titre.getText(), contenu.getText(), imagep.getText());

            ps.create(p);

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
            if (column == 3) {
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

                titre.setText(selection.getTitre());
                contenu.setText(selection.getContenu());
                imagep.setText(selection.getImage());
                displaypubqrcode(selection );

            }
        }}
    private void displaypubqrcode(publication p )
    {
        String Data =
                "Titre : " +p.getTitre() + " " + "\n" +
                        "Image: " + p.getImage() + "\n" +
                        "Contenu: " + p.getContenu()
                ;
        BufferedImage qrCodeImage = QRCodeGenerator.generateQRCode(Data, 200, 200);

        if (qrCodeImage != null) {
            Image fxImage = SwingFXUtils.toFXImage(qrCodeImage, null);
            qrcodeimage.setImage(fxImage);
        }




    }
    @FXML
    void goToPub(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/add.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }



    @FXML
    void back(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/add.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

}
