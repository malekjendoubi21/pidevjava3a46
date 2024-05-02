package controles;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.SessionManager;
import models.user;
import nl.captcha.Captcha;
import nl.captcha.backgrounds.FlatColorBackgroundProducer;
import nl.captcha.gimpy.FishEyeGimpyRenderer;
import services.userservice;
import toolkit.MyAnimation;
import toolkit.MyTools;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.List;

public class login {
    user user= new user();
    userservice us= new userservice();

    private EventObject event;


    @FXML
    private TextField captchaInput;

    @FXML
    private TextField email;
    @FXML
    private Text createAcc;

    @FXML
    private ImageView logo;

    @FXML
    private TextField password;

    @FXML
    private ImageView um_logoviewLogin;

    @FXML
    private ImageView captchImg;
    Captcha captcha= new Captcha.Builder(250, 150).build();
    boolean captchaIsCorrect;




    public void initialize() {
        generateCaptcha();
        captchaIsCorrect=false;


    }

    @FXML
    void login(ActionEvent event) {


        System.out.println("loginbtn");
        try {
            List<user> usersL= us.read();
            System.out.println(captchaInput.getText()+" c le input");

            System.out.println("captchaInput.getText().isEmpty()"+captchaInput.getText().isEmpty());
            if (captchaInput.getText().isEmpty()) {
                MyAnimation.shake(captchaInput);
                MyTools.showAlertError("Captcha is required. Please enter the captcha.");

                return;
            }

            System.out.println("isValidCaptcha()"+isValidCaptcha());
            if (!isValidCaptcha()) {
                MyAnimation.shake(captchaInput);
                MyTools.showAlertError("Captcha is incorrect. Please try again");
                return;
            }


            for (user value : usersL) {
                String normal=password.getText();
                if (value.getEmail().equals(email.getText())) {
                    if (value.getPassword().equals(normal)) {
                        user = value;
                        userservice.loggedIn=user;
                        System.out.println("user" + user);
                        System.out.println("logedin"+userservice.loggedIn);
                    }
                }
            }

            if (user != null && user.getRoles() != null) {

                if (user.getRoles().contains("ROLE_ADMIN")) {
                    SessionManager.setCurrentUser(user);
                String profileImagePath = user.getProfileImage();

                ImageView imageView = new ImageView(profileImagePath);
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Login Successful");
                alert.setHeaderText("Welcome, " + user.getEmail() + "!");
                alert.setGraphic(imageView);
                alert.showAndWait();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/back.fxml"));
                Parent root = loader.load();
                back back = loader.getController();

                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Obtenez la fenêtre actuelle
                stage.setScene(scene);
                stage.show();}
                   else
                   {
                    SessionManager.setCurrentUser(user);
                    String profileImagePath = user.getProfileImage();

                    ImageView imageView = new ImageView(profileImagePath);
                    imageView.setFitWidth(100);
                    imageView.setFitHeight(100);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Login Successful");
                    alert.setHeaderText("Welcome, " + user.getEmail() + "!");
                    alert.setGraphic(imageView);
                    alert.showAndWait();

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/home2.fxml"));
                    Parent root = loader.load();
                       homeController home2 = loader.getController();

                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Obtenez la fenêtre actuelle
                    stage.setScene(scene);
                    stage.show();

                }
            }  else {
                MyTools.showAlertError("User credentials incorrect. Please try again");
            }


        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }


    }



    public Captcha generateCaptcha() {

        Captcha.Builder builder = new Captcha.Builder(250, 150)
                .addText()
                .addBackground(new FlatColorBackgroundProducer(Color.PINK))
                .addNoise()
                .gimp(new FishEyeGimpyRenderer())
                .addBorder();

        Captcha captcha = builder.build();
        System.out.println(captcha.getAnswer());
        captchImg.setImage(SwingFXUtils.toFXImage(captcha.getImage(), null));
        return captcha;
    }

    boolean isValidCaptcha(){

        if (captcha != null) {
            System.out.println(captcha.getAnswer());

            if (captcha.getAnswer().equals(captchaInput.getText())) {

                System.out.println("isValidCaptcha if");
                captchaIsCorrect = true;
                return true;
            } else {
                System.out.println("isValidCaptcha else");
                MyAnimation.shake(captchaInput);
                captcha = generateCaptcha();
                return false;
            }
        }
        System.err.println("Captcha is not initialized");
        return false;
    }




    void goTo(String file, Node node){
        if (node == null) {
            System.err.println("Node is null. Cannot proceed with navigation.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(file));
            Parent root= loader.load();
            node.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("error"+e.getMessage());
            throw new RuntimeException(e);
        }
    }


/*
    @FXML
    void add(ActionEvent event) {

        try {
            // Charger le fichier FXML d'adduser
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateprofile.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec le contenu de adduser
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle à partir de l'événement (actionEvent)
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Définir la nouvelle scène
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/


    public void add(javafx.scene.input.MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void reset(MouseEvent mouseEvent) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ForgottenPwd.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
