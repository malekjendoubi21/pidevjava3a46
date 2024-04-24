package controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
            String notencrypted= nmtp.getText();
            currentUser = SessionManager.getCurrentUser();
             String email=    currentUser.getEmail();
             String pwd = currentUser.getPassword();
            String mtp = nmtp.getText();

            String mtpp = cmtp.getText();

            if (mtp.equals(pwd) || mtp.equals(mtpp)  ) {
                 us.updatePassword(email, nmtp.getText());
                 MyTools.showAlertInfo("Password updated", "Your Password was updated successfully! ");
                 MyTools.goTo("/login.fxml", nmtp);
             }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }


    public void annler(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/profile.fxml"));
        nmtp.getScene().setRoot(root);
    }
}
