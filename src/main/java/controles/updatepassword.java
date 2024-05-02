package controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import models.SessionManager;
import models.user;
import services.userservice;
import toolkit.MyTools;

import java.io.IOException;
import java.sql.SQLException;

public class updatepassword {

    @FXML
    private TextField anmtp;

    @FXML
    private TextField cmtp;

    @FXML
    private TextField nmtp;


    userservice us= new userservice();


    private user currentUser;

    @FXML
    void confirmNewPwd(ActionEvent event) {
        try {

            String mtp = nmtp.getText();
            if (mtp.isEmpty() || !mtp.matches("^(?=.*[A-Z])(?=.*\\d).{8,}$")) {
                showErrorAlert("Invalid password! Password must contain at least one uppercase letter, one digit, and have a minimum length of 8 characters.");
                return;
            }
            String mtpp = cmtp.getText();
            if (mtpp.isEmpty() || !mtpp.matches("^(?=.*[A-Z])(?=.*\\d).{8,}$")) {
                showErrorAlert("Invalid password! Password must contain at least one uppercase letter, one digit, and have a minimum length of 8 characters.");
                return;
            }
            String ancienpwd = anmtp.getText();
            currentUser = SessionManager.getCurrentUser();
            String email = currentUser.getEmail();
            String pwd = currentUser.getPassword();
            //String mtp = nmtp.getText();
            //String mtpp = cmtp.getText();

            if (ancienpwd.equals(pwd) && mtp.equals(mtpp)) {
                us.updatePassword(email, nmtp.getText());
                MyTools.showAlertInfo("Password updated", "Your Password was updated successfully! ");
                MyTools.goTo("/login.fxml", nmtp);
            } else {
                // Afficher une alerte si les conditions ne sont pas remplies
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Password Update Failed");
                alert.setContentText("Please make sure your old password is correct and the new passwords match.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void annler(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/profile.fxml"));
        nmtp.getScene().setRoot(root);
    }
}
