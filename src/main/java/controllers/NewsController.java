package controllers;


import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class NewsController implements Initializable {


    private static boolean news = false;
    private static final WebView tmp = new WebView();
    private static final WebHistory history = tmp.getEngine().getHistory();
    private static final ObservableList<WebHistory.Entry> entries = history.getEntries();
    @FXML
    BorderPane main_hook;
    @FXML
    BorderPane hold;




    public NewsController() {

    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            URL ck = new URL("https://www.emro.who.int/health-topics/influenza/resources.html");
            if (!news) {
                (ck.openConnection()).connect();
                // BorderPane hold = FXMLLoader.load(this.getClass().getResource("/ui_3.fxml"));
                tmp.getEngine().load("https://www.emro.who.int/health-topics/influenza/resources.html");
                tmp.setZoom(0.75D);
                hold.setCenter(tmp);
                //this.main_hook.setCenter(hold);
                news = true;
            }
        } catch (Exception no) {
            main_hook.setCenter(new Label("No Internet Connection"));
            main_hook.getCenter().setStyle("-fx-font: 50px \"Arial\"; -fx-text-fill: black;");
            news = true;
        }
    }









    public void news(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Home2.fxml")));
        Scene scene = ((Node) e.getSource()).getScene();
        scene.setRoot(root);
    }



    public void reload(ActionEvent e) {
        tmp.getEngine().load("https://www.emro.who.int/health-topics/influenza/resources.html");
    }



}





