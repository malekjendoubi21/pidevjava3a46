package controles;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import models.user;
import services.userservice;
import toolkit.MyTools;

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
            String notencrypted= newpwdTf.getText();

        //    String encrypted= PasswordEncryptor.encrypt(newpwdTf.getText());
            us.updatePassword(userMail,newpwdTf.getText());
            MyTools.showAlertInfo("Password updated","Your Password was updated successfully! Go to the login page to access your account.");
            MyTools.goTo("/login.fxml",newpwdTf);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }

}
