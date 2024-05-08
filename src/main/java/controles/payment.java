package controles;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import models.Don;
import services.EmailService;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Objects;

import static controles.PDFGenerator.generateDonationListPDF;

public class payment {

    @FXML
    private TextField card;

    @FXML
    private CheckBox checkBox;

    @FXML
    private TextField cvc;

    @FXML
    private TextField date;

    @FXML
    private Button donate;

    @FXML
    private TextField email;

    public void donate(javafx.event.ActionEvent event) throws IOException {



        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Home2.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }
}


