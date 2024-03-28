package controles;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.user;
import services.userservice;
import toolkit.MyTools;
import toolkit.PasswordEncryptor;

import java.sql.SQLException;

public class ResetPwdController extends controller {
    userservice us= new userservice();
    String userMail="";

    @FXML
    private TextField newpwdTf;


    void initData(user currUser){
        userMail= currUser.getEmail();
    }

    @FXML
    void cancel(ActionEvent event) {
        MyTools.goTo("/login.fxml",newpwdTf);


    }

    @FXML
    void confirmNewPwd(ActionEvent event) {
        try {
            //TODO CRITIQUE REVENIR ICI PR LE ENCRYPT
            String notencrypted= newpwdTf.getText();

            String encrypted= PasswordEncryptor.encrypt(newpwdTf.getText());
            us.updatePassword(userMail,encrypted);
            MyTools.showAlertInfo("Password updated","Your Password was updated successfully! Go to the login page to access your account.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }

}
