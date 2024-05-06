package controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Organisation;
import services.OrganisationService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class editOrg {

    private OrganisationService os = new OrganisationService();

    @FXML
    private TextField nom;

    @FXML
    private TextField email;

    @FXML
    private TextField num_tel;

    @FXML
    private TextField adresse;

    @FXML
    private TextField search;

    @FXML
    private Button retour;

    @FXML
    private Button modifier;

    Organisation data =Organisation.getInstance();

    @FXML
    void initialize() throws SQLException {
        nom.setText(data.getNom());
        adresse.setText(data.getAdresse());
        email.setText(data.getEmail());
        num_tel.setText(data.getNum_tel());
    }

    @FXML
    void retour(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/listOrg.fxml"));
        nom.getScene().setRoot(root);
        email.getScene().setRoot(root);
        adresse.getScene().setRoot(root);
        num_tel.getScene().setRoot(root);

    }

    @FXML
    void update(ActionEvent event) throws SQLException, IOException {
     /*   boolean sujetAlphabetic = DataValidation.textAlphabet(sujet, sujetLabel, "Please only enter letters from a - z");
        boolean sujetEmpty=DataValidation.textFieldIsNull(sujet,sujetLabel,"Should not be empty");
        boolean contenuEmpty=DataValidation.textFieldIsNull(contenu,contenuLabel,"Should not be empty");
        boolean sujetValid = !sujetEmpty && sujetAlphabetic;
        boolean contenuValid = !contenuEmpty;

        if (sujetValid && contenuValid) {*/
        data.setNom(nom.getText());
        data.setAdresse(adresse.getText());
        data.setEmail(email.getText());
        data.setNum_tel(num_tel.getText());

        System.out.println(data);
        os.update(data);
        Parent root = FXMLLoader.load(getClass().getResource("/listOrg.fxml"));

        modifier.getScene().setRoot(root);
       /* prenom.getScene().setRoot(root);
        email.getScene().setRoot(root);
        description.getScene().setRoot(root);
        typeComboBox.getScene().setRoot(root);
        montant.getScene().setRoot(root);
        organisation.getScene().setRoot(root);*/


    }

    public void initData(Organisation o) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        nom.setText(o.getNom());
        adresse.setText(o.getAdresse());
        email.setText(o.getEmail());
        num_tel.setText(o.getNum_tel());
        Parent root = fxmlLoader.load(getClass().getResource("/editOrg.fxml"));
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.show();
    }

    @FXML
    void goToDon(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/listDon.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    @FXML
    void goToDon2(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/listDon.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    @FXML
    void goToHome(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Home2.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }
}
