package controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.reclamation;
import javafx.scene.control.Button;
import services.DataValidation;
import services.ReclamationService;
import javafx.scene.control.Label;
import java.io.IOException;
import java.sql.SQLException;

public class editt {
    private ReclamationService rs = new ReclamationService();

    @FXML
    private Button annuler;
    @FXML
    private Button modifier;
    @FXML
    private TextField contenu;

    @FXML
    private TextField sujet;

    @FXML
    private TextField userid;
    @FXML
    private Label contenuLabel;
    @FXML
    private Label sujetLabel;

    reclamation data =reclamation.getInstance();
    @FXML
    void initialize() throws SQLException {
        contenu.setText(data.getContenu());
        sujet.setText(data.getSujet());
        userid.setText(String.valueOf(data.getUser_id()));
    }
    @FXML
    void ann(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/testtt.fxml"));
        contenu.getScene().setRoot(root);
    }

    @FXML
    void mod(ActionEvent event) throws SQLException, IOException {
        boolean sujetAlphabetic = DataValidation.textAlphabet(sujet, sujetLabel, "Please only enter letters from a - z");
        boolean sujetEmpty=DataValidation.textFieldIsNull(sujet,sujetLabel,"Should not be empty");
        boolean contenuEmpty=DataValidation.textFieldIsNull(contenu,contenuLabel,"Should not be empty");
        boolean sujetValid = !sujetEmpty && sujetAlphabetic;
        boolean contenuValid = !contenuEmpty;

        if (sujetValid && contenuValid) {
            data.setSujet(sujet.getText());
            data.setContenu(contenu.getText());
            rs.update(data);
            Parent root = FXMLLoader.load(getClass().getResource("/testtt.fxml"));
            contenu.getScene().setRoot(root);
        }

    }
    public void initData(reclamation r) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        contenu.setText(r.getContenu());
        sujet.setText(r.getSujet());
        userid.setText(String.valueOf(r.getUser_id()));
        Parent root = fxmlLoader.load(getClass().getResource("/editt.fxml"));
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.show();
    }


}