package controles;


import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Don;
import models.Organisation;
import services.DataValidation;
import services.DonService;
import services.EmailService;
import services.OrganisationService;
import com.stripe.Stripe;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import static controles.PDFGenerator.generateDonationListPDF;

public class ajouterDon implements Initializable {

    @FXML
    private Button ajouter;

    @FXML
    private TextField description;

    @FXML
    private Label descriptionLabel;

    @FXML
    private TextField email;

    @FXML
    private Label emailLabel;

    @FXML
    private TextField montant;

    @FXML
    private Label montantLabel;

    @FXML
    private TextField nom;
    @FXML
    private Button enterButton;

    @FXML
    private Label nomLabel;

    @FXML
    private TextField prenom;

    @FXML
    private Label prenomLabel;

    @FXML
    private TextField donImage;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private ComboBox<String> organisationComboBox;

    @FXML
    private ImageView donImageView;


    private final DonService ds = new DonService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        typeComboBox.setItems(FXCollections.observableArrayList("Dons Monétaires", "Dons d'équipements"));

        try {
            OrganisationService ls = new OrganisationService();
            List<Organisation> lsss = ls.read();
            List<String> names = lsss.stream()
                    .map(Organisation::getNom)
                    .toList();
            organisationComboBox.setItems(FXCollections.observableArrayList(names));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @FXML
    void BrowseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {

            Image image = new Image(file.toURI().toString());
            donImageView.setImage(image);
            donImage.setText(file.toURI().toString());

        }
    }

    @FXML
    void ajouter(ActionEvent event) {
        boolean nomAlphabet = DataValidation.textAlphabet(nom, nomLabel, "Please only enter letters from a - z");
        boolean nomEmpty = DataValidation.textFieldIsNull(nom, nomLabel, "Should not be empty");
        boolean prenomAlphabet = DataValidation.textAlphabet(prenom, prenomLabel, "Please only enter letters from a - z");
        boolean prenomEmpty = DataValidation.textFieldIsNull(prenom, prenomLabel, "Should not be empty");
        boolean emailFormat = DataValidation.emailFormat(email, emailLabel, "Please follow this RegEx: username@gmail.com");
        boolean emailEmpty = DataValidation.textFieldIsNull(email, emailLabel, "Should not be empty");
        boolean descriptionEmpty = DataValidation.textFieldIsNull(description, descriptionLabel, "Should not be empty");
        boolean montantFormat = DataValidation.textNumeric(montant, montantLabel, "Should not be empty");



        boolean nomValid = !nomEmpty && nomAlphabet;
        boolean prenomValid = !prenomEmpty && prenomAlphabet;
        boolean emailValid = !emailEmpty && emailFormat;
        boolean descriptionValid = !descriptionEmpty;
        boolean montantValid = !montantFormat;

        try {
            if (!fieldsAreValid()) {
                showErrorAlert("Check fields RegEx.");
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(montant.getText());
            } catch (NumberFormatException ex) {
                showErrorAlert("Please enter a valid donation amount.");
                return;
            }

            String type = typeComboBox.getValue();
            if (type == null) {
                showErrorAlert("Please select a donation type.");
                return;
            }

            String selectedOrganisation = organisationComboBox.getValue();
            if (selectedOrganisation == null) {
                showErrorAlert("Please select an organisation.");
                return;
            }

            long amountInCents = (long) (amount * 100);

            Don d = new Don();
            d.setNom(nom.getText());
            d.setPrenom(prenom.getText());
            d.setEmail(email.getText());
            d.setType(type);
            OrganisationService ls = new OrganisationService();
            d.setOrganisation_id(ls.getnomm(selectedOrganisation));
            d.setDescription(description.getText());
            d.setMontant((int) amountInCents);
            String imagePath = donImage.getText();
            d.setImage(imagePath);
            ds.create(d);

            // Process payment
            Stripe.apiKey = "sk_test_51OnL4vFaZiXCQR9PJzhuH3fizwwfBIuqfa6HwdlKpoJRR00xvemV4aJ";
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amountInCents)
                    .setCurrency("usd")
                    .build();
            PaymentIntent intent = PaymentIntent.create(params);
            System.out.println("Payment successful. PaymentIntent ID: " + intent.getId());


            String htmlContent =
                    "<!DOCTYPE html>\n" +
                            "<html lang=\"en\">\n" +
                            "<head>\n" +
                            "    <meta charset=\"UTF-8\">\n" +
                            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                            "    <title>Email Confirmation</title>\n" +
                            "    <style>\n" +
                            "        body {\n" +
                            "            font-family: Arial, sans-serif;\n" +
                            "            margin: 0;\n" +
                            "            padding: 0;\n" +
                            "        }\n" +
                            "\n" +
                            "        h1 {\n" +
                            "            color: #5AAC4E;\n" +
                            "            font-size: 24px;\n" +
                            "            text-align: center;\n" +
                            "        }\n" +
                            "\n" +
                            "        p {\n" +
                            "            font-size: 16px;\n" +
                            "            line-height: 1.5;\n" +
                            "            text-align: center;\n" +
                            "        }\n" +
                            "\n" +
                            "        .button {\n" +
                            "            background-color: #FF1145;\n" +
                            "            color: #fff;\n" +
                            "            padding: 10px 20px;\n" +
                            "            border: none;\n" +
                            "            border-radius: 5px;\n" +
                            "            cursor: pointer;\n" +
                            "            display: block;\n" +
                            "            margin: 20px auto;\n" +
                            "            text-decoration: none; /* Remove default underline */\n" +
                            "            text-align: center; /* Center the text */\n" +
                            "        }\n" +
                            "\n" +
                            "        .container {\n" +
                            "            background-color: #fff;\n" +
                            "            padding: 20px;\n" +
                            "            border-radius: 5px;\n" +
                            "            max-width: 600px;\n" +
                            "            margin: 20px auto;\n" +
                            "            position: relative; /* Add relative positioning to the container */\n" +
                            "        }\n" +
                            "\n" +
                            "        .image-container {\n" +
                            "            position: absolute; /* Set image container to absolute position */\n" +
                            "            top: 0; /* Align to the top of the container */\n" +
                            "            left: 0; /* Align to the left of the container */\n" +
                            "            width: 100%; /* Occupy the full width of the container */\n" +
                            "            text-align: center; /* Center the content horizontally */\n" +
                            "        }\n" +
                            "\n" +
                            "        img {\n" +
                            "            width: 100%; /* Set to whatever percentage you want */\n" +
                            "            max-width: 300px; /* Set the maximum width */\n" +
                            "            height: auto; /* Maintain aspect ratio */\n" +
                            "            display: block;\n" +
                            "            margin: auto;\n" +
                            "        }\n" +
                            "    </style>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "    <div class=\"container\">\n" +
                            "        <div class=\"image-container\">\n" +
                            "            <img src=\"https://media.giphy.com/media/vmZ6gYyDO65ffARJTm/giphy.gif?cid=790b7611h1mccrr6v6rf5srubs4zhm5lx2hlhrffr1lbrdzd&ep=v1_gifs_search&rid=giphy.gif&ct=g\" alt=\"Your Image\">\n" +
                            "        </div>\n" +
                            "        <h1>Donation Confirmation</h1>\n" +
                            "        <p>Dear Donor,</p>\n" +
                            "        <p>Thank you so much, with your money we will help many people in need.\n" +
                            "\n" +
                            "</p>\n" +
                            "        <p><a href=\"#\" class=\"button\" >You will find below your donation receipt.</a> \"\"></a></p>\n" +

                            "        <p>With profound appreciation,</p>\n" +
                            "        <p>NoTreatment Donations Team</p>\n" +
                            "    </div>\n" +
                            "</body>\n" +
                            "</html>";


            String pdfFilePath = generateDonationListPDF(d, "C:/Users/MAHOX/Desktop/Donation/Donation.pdf");
            EmailService.sendEmailWithAttachmentAndHTML(d.getEmail(), "Thank you for your donation!", htmlContent, pdfFilePath);

            navback(event);

        } catch (Exception e) {
            showErrorAlert("An error occurred while processing the donation.");
            e.printStackTrace();
        }
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




    private boolean fieldsAreValid() {
        return !nom.getText().isEmpty() && !prenom.getText().isEmpty() && !email.getText().isEmpty() &&
                typeComboBox.getValue() != null && !description.getText().isEmpty() && !montant.getText().isEmpty();
    }

    private void showErrorAlert(String message) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText(null);
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }

    @FXML
    void navback(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/listDon.fxml"));
            nom.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
