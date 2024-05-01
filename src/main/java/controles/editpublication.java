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
import models.publication;
import services.publicationservice;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class editpublication {

    private publicationservice os = new publicationservice();

    @FXML
    private TextField contenu;

    @FXML
    private TextField titre;

    @FXML
    private Button retour;

    @FXML
    private Button modifier;

    publication data =publication.getInstance();

    @FXML
    void initialize() throws SQLException {

        contenu.setText(data.getContenu());
        titre.setText(data.getTitre());

    }

    @FXML
    void retour(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/add.fxml"));
        titre.getScene().setRoot(root);
        contenu.getScene().setRoot(root);

    }

    @FXML
    void update(ActionEvent event) throws SQLException, IOException {



        data.setTitre(titre.getText());
        data.setContenu(contenu.getText());

        System.out.println(data);
        os.update(data);
        Parent root = FXMLLoader.load(getClass().getResource("/add.fxml"));

        modifier.getScene().setRoot(root);

    }

    public void initData(publication l) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        titre.setText(l.getTitre());
        contenu.setText(l.getContenu());
        Parent root = fxmlLoader.load(getClass().getResource("/editpublication.fxml"));
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root));
        newStage.show();
    }




}