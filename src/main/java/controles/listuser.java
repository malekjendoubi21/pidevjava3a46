package controles;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import models.user;
import services.userservice;
import toolkit.QRCodeGenerator;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLException;

public class listuser {
    userservice us = new userservice();
    @FXML
    private Button adduser;
    @FXML
    private Label idlabel;
    @FXML
    private TableView<user> aff;
    private TableColumn<user, Integer> useridColumn;
    private TableColumn<user, String> usernomColumn;

    private TableColumn<user, String> userprenomColumn;
    private TableColumn<user, String> usergenderColumn;
    private TableColumn<user, Integer> usernumtelColumn;
   private TableColumn<user, String> userrolesColumn;
    private TableColumn<user, String> userspecialiteColumn;

    private TableColumn<user, String> useremailColumn;
    private TableColumn<user, DatePicker> userbirthColumn;
    private boolean isEditable = false;

    @FXML
    private Button recher;
    @FXML
    private DatePicker birth;

    @FXML
    private Button delete;

    @FXML
    private Button edit;

    @FXML
    private TextField email;

    @FXML
    private TextField gender;

    @FXML
    private TextField nom;

    @FXML
    private TextField numtel;

    @FXML
    private TextField password;

    @FXML
    private TextField prenom;

    @FXML
    private TextField profileImage;
    @FXML
    private ImageView qrCodeImageView;
    @FXML
    private TextField roles;

    @FXML
    private TextField specialite;

    @FXML
    private TextField search;
    @FXML
    void addnew(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/back.fxml"));
        aff.getScene().setRoot(root);
    }

    @FXML
    void initialize() throws SQLException {
        ObservableList<user> list = FXCollections.observableList(us.read());
        aff.setItems(list);
      //  useridColumn = new TableColumn<>("id");
        usernomColumn = new TableColumn<>("nom");
        userprenomColumn = new TableColumn<>("prenom");
        usergenderColumn = new TableColumn<>("gender");
        useremailColumn = new TableColumn<>("email");
        usernumtelColumn = new TableColumn<>("numtel");
        userspecialiteColumn = new TableColumn<>("specialite");
        userrolesColumn = new TableColumn<>("roles");
        userbirthColumn = new TableColumn<>("birth");


        //useridColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        userprenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        usergenderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        usernumtelColumn.setCellValueFactory(new PropertyValueFactory<>("numtel"));
        useremailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        userspecialiteColumn.setCellValueFactory(new PropertyValueFactory<>("specialite"));
        userrolesColumn.setCellValueFactory(new PropertyValueFactory<>("roles"));
        userbirthColumn.setCellValueFactory(new PropertyValueFactory<>("birth"));

        aff.getColumns().addAll( usernomColumn, userprenomColumn , userrolesColumn,userbirthColumn, usergenderColumn, usernumtelColumn, useremailColumn, userspecialiteColumn);
        aff.setItems(list);

        FilteredList<user> filteredData = new FilteredList<>(list, b->true);
        search.textProperty().addListener((observable,oldValue,newValue ) -> {
            filteredData.setPredicate(user -> {


                if (newValue.isEmpty() ||newValue.isBlank() || newValue == null){
                    return true;
                }
                String searchKeyword =newValue.toLowerCase();
                if( user.getNom().toLowerCase().indexOf(searchKeyword)>-1){
                    return true;
                }
                if( user.getPrenom().toLowerCase().indexOf(searchKeyword)>-1){
                    return true;
                }
                else
                    return false;
            });

        });

        SortedList<user> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(aff.comparatorProperty());
        aff.setItems(sortedData);
    }
    @FXML
    void initializedata(user user) throws SQLException {
       ObservableList<user> list = FXCollections.observableList(us.read());
        aff.setItems(list);
        //useridColumn = new TableColumn<>("id");
        usernomColumn = new TableColumn<>("nom");
        userprenomColumn = new TableColumn<>("prenom");
        usergenderColumn = new TableColumn<>("gender");
        useremailColumn = new TableColumn<>("email");
        usernumtelColumn = new TableColumn<>("numtel");

        //useridColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        userprenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        usergenderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        usernumtelColumn.setCellValueFactory(new PropertyValueFactory<>("numtel"));
        useremailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        aff.getColumns().addAll( usernomColumn, userprenomColumn, usergenderColumn, usernumtelColumn, useremailColumn);
        aff.setItems(list);

    }
    @FXML
    public void rowClick(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 1) {
            user selection = aff.getSelectionModel().getSelectedItem();

            if (selection != null) {
            //    idlabel.setText(String.valueOf(selection.getId()));
                nom.setText(selection.getNom());
                prenom.setText(selection.getPrenom());
                gender.setText(selection.getGender());
                numtel.setText(String.valueOf(selection.getNumtel()));
                email.setText(selection.getEmail());
                roles.setText(selection.getRoles());
                birth.setValue(selection.getBirth());
                lockFields();

                String userData =
                        "nom et prenom " +selection.getNom() + " " + selection.getPrenom() + "\n" +
                        "Gender: " + selection.getGender() + "\n" +
                        "Email: " + selection.getEmail()+ "\n"+
                        "Roles"+ selection.getRoles()
                        ;
                BufferedImage qrCodeImage = QRCodeGenerator.generateQRCode(userData, 200, 200);

                // Afficher le QR code où vous le souhaitez, peut-être dans un ImageView
                if (qrCodeImage != null) {
                    Image fxImage = SwingFXUtils.toFXImage(qrCodeImage, null);
                    qrCodeImageView.setImage(fxImage);
                }
            }
        }
    }


    @FXML
    public void recherche(javafx.event.ActionEvent actionEvent) throws SQLException {
        us.rech(search.getText());
        ObservableList<user> list = FXCollections.observableList(us.rech(search.getText()));
        aff.setItems(list);
    }
    private void lockFields() {
        isEditable = false;
        roles.setDisable(true);
        gender.setDisable(true);
        email.setDisable(true);

    }

    private void unlockFields() {
        isEditable = true;
        roles.setDisable(false);
        gender.setDisable(false);
        email.setDisable(false);


    }

}



