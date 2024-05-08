package controles;


import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import javafx.beans.property.SimpleBooleanProperty;
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
import services.*;
import com.stripe.Stripe;
import controles.payment;
import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

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

    @FXML
    private TextField card;

    @FXML
    private CheckBox checkBox;

    @FXML
    private TextField cvc;

    @FXML
    private TextField date;

    private final DonService ds = new DonService();

    @FXML
    private Label captchaLabel;

    @FXML
    private TextField captchaInput;

    @FXML
    private Button refreshCaptchaButton;

    @FXML
    private Label verificationMessageLabel;

    private int correctAnswer;

    private SimpleBooleanProperty captchaCorrect = new SimpleBooleanProperty(false);

    @FXML
    private ImageView imageDon;

    @FXML
    private TextField imageD;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshCaptcha();
        ajouter.disableProperty().bind(captchaCorrect.not());

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
    void browseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {

            Image image = new Image(file.toURI().toString());
            imageDon.setImage(image);
            imageD.setText(file.toURI().toString());

        }
    }

    /*@FXML
    void ajouter(ActionEvent event) throws SQLException {




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
            ds.create(d);


            navback(event);


    }*/


    @FXML
    void ajouter(ActionEvent event) throws SQLException, StripeException, MessagingException, IOException {

        try {
            int userAnswer = Integer.parseInt(captchaInput.getText());
            if (userAnswer == correctAnswer) {
                verificationMessageLabel.setText("CAPTCHA correct. Proceed with adding donation.");
                captchaCorrect.set(true);
            } else {
                verificationMessageLabel.setText("Incorrect CAPTCHA answer. Please try again.");
                refreshCaptcha();
            }
        } catch (NumberFormatException e) {
            verificationMessageLabel.setText("Invalid input. Please enter a valid number.");
        }


        if (nom.getText().isEmpty() || prenom.getText().isEmpty() || email.getText().isEmpty() || typeComboBox.getValue() == null) {
            showErrorAlert("Veuillez remplir tous les champs obligatoires.");
            return;
        }

        if (!isValidEmail(email.getText())) {
            showErrorAlert("Veuillez saisir une adresse e-mail valide.");
            return;
        }

        if (!isAlphaWithSpaces(nom.getText())) {
            showErrorAlert("Veuillez saisir uniquement des caractères alphabétiques pour le nom.");
            return;
        }

        if (!isAlphaWithSpaces(prenom.getText())) {
            showErrorAlert("Veuillez saisir uniquement des caractères alphabétiques pour le prénom.");
            return;
        }

        if (!checkBox.isSelected()) {
            showErrorAlert("Veuillez cocher la case pour confirmer.");
            return;
        }

        String type = typeComboBox.getValue();
        String selectedOrganisation = organisationComboBox.getValue();

        if (selectedOrganisation == null) {
            showErrorAlert("Veuillez sélectionner une organisation.");
            return;
        }

        // Create Don object and set its attributes
        Don d = new Don();
        d.setNom(nom.getText());
        d.setPrenom(prenom.getText());
        d.setEmail(email.getText()); // Set email
        d.setType(type);
        OrganisationService ls = new OrganisationService();
        d.setOrganisation_id(ls.getnomm(selectedOrganisation));
        d.setDescription(description.getText());

        // Ajouter une condition pour gérer le type de don
        if (type.equals("Dons Monétaires")) {
            double amount;
            try {
                amount = Double.parseDouble(montant.getText());
            } catch (NumberFormatException ex) {
                showErrorAlert("Veuillez saisir un montant de don valide.");
                return;
            }

            try {
                amount = Double.parseDouble(montant.getText());
                // Check if the amount is less than $0.50 USD
                if (amount < 0.50) {
                    showErrorAlert("Le montant doit être d'au moins 0,50 USD.");
                    return;
                }
            } catch (NumberFormatException ex) {
                showErrorAlert("Veuillez saisir un montant de don valide.");
                return;
            }

            long amountInCents = (long) (amount * 100); // Convert amount to cents

            Stripe.apiKey = "sk_test_51OnL4vFaZiXCQR9PJzhuH3fizwwfYLtFmkkBNC4EyD8tW1GFam8BkEAjGAVpszcAe2fBIuqfa6HwEHYUQkVjqtRR00xvemV4aJ";

            try {
                PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                        .setAmount(amountInCents)
                        .setCurrency("usd")
                        .build();
                PaymentIntent intent = PaymentIntent.create(params);
                System.out.println("Payment successful. PaymentIntent ID: " + intent.getId());

                d.setMontant((int) amountInCents);
            } catch (StripeException e) {
                showErrorAlert("Erreur lors du traitement du paiement : " + e.getMessage());
                return;
            }



            if (!imageD.getText().isEmpty()) {
                showErrorAlert("Vous ne pouvez pas ajouter d'image pour un don monétaire.");
                return;
            }
        } else if (type.equals("Dons d'équipements")) {
            if (!montant.getText().isEmpty()) {
                showErrorAlert("Vous ne pouvez pas ajouter de montant pour un don d'équipements.");
                return;
            }
        }

        ds.create(d);

        if (showConfirmationAlert("Voulez-vous ajouter ce don ?")) {

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
                            "        <p><a href=\"#\" class=\"button\" >You will find below your donation receipt.</a> \"</a></p>\n" +

                            "        <p>With profound appreciation,</p>\n" +
                            "        <p>NoTreatment Donations Team</p>\n" +
                            "    </div>\n" +
                            "</body>\n" +
                            "</html>";

            String pdfFilePath = generateDonationListPDF(d, "C:/Users/MAHOX/Desktop/Donn/Donation.pdf");
            EmailService.sendEmailWithAttachmentAndHTML(d.getEmail(), "Thank you for your donation!", htmlContent, pdfFilePath);
            showSuccessAlert("Don ajouté avec succès.");
        }

        navback(event);
    }



    @FXML
    void ajouterEq(ActionEvent event) throws SQLException, StripeException, MessagingException, IOException {

        try {
            int userAnswer = Integer.parseInt(captchaInput.getText());
            if (userAnswer == correctAnswer) {
                verificationMessageLabel.setText("CAPTCHA correct. Proceed with adding donation.");
                captchaCorrect.set(true);
            } else {
                verificationMessageLabel.setText("Incorrect CAPTCHA answer. Please try again.");
                refreshCaptcha();
            }
        } catch (NumberFormatException e) {
            verificationMessageLabel.setText("Invalid input. Please enter a valid number.");
        }

        // Validate input fields
        if (nom.getText().isEmpty() || prenom.getText().isEmpty() || email.getText().isEmpty() || typeComboBox.getValue() == null) {
            showErrorAlert("Veuillez remplir tous les champs obligatoires.");
            return;
        }

        if (!isValidEmail(email.getText())) {
            showErrorAlert("Veuillez saisir une adresse e-mail valide.");
            return;
        }

        if (!isAlphaWithSpaces(nom.getText())) {
            showErrorAlert("Veuillez saisir uniquement des caractères alphabétiques pour le nom.");
            return;
        }

        if (!isAlphaWithSpaces(prenom.getText())) {
            showErrorAlert("Veuillez saisir uniquement des caractères alphabétiques pour le prénom.");
            return;
        }



        if (!checkBox.isSelected()) {
            showErrorAlert("Veuillez cocher la case pour confirmer.");
            return;
        }
        // Vérifier si la case à cocher est cochée
        if (!checkBox.isSelected()) {
            showErrorAlert("Veuillez cocher la case pour confirmer.");
            return;
        }
        // Parse amount


        String type = typeComboBox.getValue();
        String selectedOrganisation = organisationComboBox.getValue();

        if (selectedOrganisation == null) {
            showErrorAlert("Veuillez sélectionner une organisation.");
            return;
        }



        // Create Don object and set its attributes
        Don d = new Don();
        d.setNom(nom.getText());
        d.setPrenom(prenom.getText());
        d.setEmail(email.getText()); // Set email
        d.setType(type);
        OrganisationService ls = new OrganisationService();
        d.setOrganisation_id(ls.getnomm(selectedOrganisation));
        d.setDescription(description.getText());

        ds.create(d);

        if (showConfirmationAlert("Voulez-vous ajouter ce don ?")) {

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
                            "        <p><a href=\"#\" class=\"button\" >You will find below your donation receipt.</a> \"</a></p>\n" +

                            "        <p>With profound appreciation,</p>\n" +
                            "        <p>NoTreatment Donations Team</p>\n" +
                            "    </div>\n" +
                            "</body>\n" +
                            "</html>";

            String pdfFilePath = generateDonationListPDF(d, "C:/Users/MAHOX/Desktop/Donn/Donation.pdf");
            EmailService.sendEmailWithAttachmentAndHTML(d.getEmail(), "Thank you for your donation!", htmlContent, pdfFilePath);
            showSuccessAlert("Don ajouté avec succès.");
        }

        navback(event);
    }


    @FXML
    private void verifyCaptcha() {
        try {
            int userAnswer = Integer.parseInt(captchaInput.getText());
            if (userAnswer == correctAnswer) {
                verificationMessageLabel.setText("CAPTCHA correct. Proceed with adding donation.");
                captchaCorrect.set(true);
            } else {
                verificationMessageLabel.setText("Incorrect CAPTCHA answer. Please try again.");
                refreshCaptcha();
                captchaCorrect.set(false);
            }
        } catch (NumberFormatException e) {
            verificationMessageLabel.setText("Invalid input. Please enter a valid number.");
        }
    }
    private char randomOperator(Random random) {
        char[] operators = {'+'};
        return operators[random.nextInt(operators.length)];
    }

    @FXML
    private void refreshCaptcha() {
        Random random = new Random();
        int num1 = random.nextInt(10);
        int num2 = random.nextInt(10);
        char operator = randomOperator(random);
        correctAnswer = calculateAnswer(num1, num2, operator);
        captchaLabel.setText(num1 + " " + operator + " " + num2 + " = ");
        captchaInput.clear();
        captchaCorrect.set(false);
    }
    private int calculateAnswer(int num1, int num2, char operator) {
        switch (operator) {
            case '+':
                return num1 + num2;
            case '-':
                return num1 - num2;
            case '*':
                return num1 * num2;
            default:
                throw new IllegalArgumentException("Invalid operator");
        }
    }

    private boolean isValidCardNumber(String cardNumber) {
        // Vérifier si la longueur du numéro de carte est correcte
        if (cardNumber.length() != 16) { // 16 chiffres sans espaces
            return false;
        }

        // Vérifier si le numéro de carte ne contient que des chiffres de 0 à 9
        if (!cardNumber.matches("[0-9]+")) {
            return false;
        }

        // Autres validations spécifiques si nécessaire...

        // Si le code atteint ce point, la validation est réussie
        return true;
    }

    private boolean isValidDate(String date) {
        // Vérifiez si la date correspond au format MM/AAAA
        if (!date.matches("\\d{2}/\\d{4}")) {
            return false;
        }

        // Parsez le mois et l'année
        String[] parts = date.split("/");
        int month = Integer.parseInt(parts[0]);
        int year = Integer.parseInt(parts[1]);

        // Vérifiez si le mois est entre 1 et 12 et si l'année est actuelle ou dans le futur
        return (month >= 1 && month <= 12) && (year >= 2024);
    }

    private boolean isValidCVC(String cvc) {
        // Vérifiez si le CVC est composé de 3 chiffres
        if (!cvc.matches("\\d{3}")) {
            return false;
        }

        // Vérifiez si le CVC est différent de "000"
        return !cvc.equals("000");
    }

    private boolean isAlphaWithSpaces(String str) {
        return str.matches("[a-zA-Z ]+");
    }

    private boolean isAlpha(String name) {
        return name.matches("[a-zA-Z]+");
    }

    private boolean isValidEmail(String email) {
        return email.matches("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b");
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(message);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
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
    void goHome(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Home2.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    @FXML
    void orgF(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/listOrgF.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }


    @FXML
    void don(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/payment.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }


    private boolean fieldsAreValid() {
        return !nom.getText().isEmpty() && !prenom.getText().isEmpty() && !email.getText().isEmpty() &&
                typeComboBox.getValue() != null && !description.getText().isEmpty() && !montant.getText().isEmpty();
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
