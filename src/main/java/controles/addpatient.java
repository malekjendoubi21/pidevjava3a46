package controles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.patient;
import services.patientservice;
import toolkit.MyTools;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
public class addpatient {

    @FXML
    private Button ajouter;

    @FXML
    private DatePicker birth;
    @FXML
    private ImageView userImageView;
    @FXML
    private TextField email;
    @FXML
    private ComboBox<String> gender;


    @FXML
    private TextField nom;

    @FXML
    private TextField numtel;

    @FXML
    private TextField password;

    @FXML
    private TextField prenom;

    @FXML
    private TextField profileImage;

    @FXML
    private TextField roles;
    private final patientservice  us =new patientservice();
    public void initialize() {

        gender.setItems(FXCollections.observableArrayList("Homme", "Femme"));


    }

    @FXML
    void adddocteur(ActionEvent event) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            LocalDate selectedDate = birth.getValue();
            if (selectedDate == null || selectedDate.isAfter(LocalDate.now())) {
                showErrorAlert("Invalid birth date! Please enter a valid date before today.");
                return;
            }


            String emailText = email.getText();
            if (emailText.isEmpty() || !emailText.contains("@") || !emailText.endsWith(".com")) {
                showErrorAlert("Invalid email! Please enter a valid email address.");
                return;
            }

            // Vérification du mot de passe
            String passwordText = password.getText();
            if (passwordText.isEmpty() || !passwordText.matches("^(?=.*[A-Z])(?=.*\\d).{8,}$")) {
                showErrorAlert("Invalid password! Password must contain at least one uppercase letter, one digit, and have a minimum length of 8 characters.");
                return;
            }

            String numtelText = numtel.getText();
            if (numtelText.isEmpty() || !numtelText.matches("^\\d{8}$") || Integer.parseInt(numtelText) < 0) {
                showErrorAlert("Invalid phone number! Please enter a valid 8-digit positive integer.");
                return;
            }

            String nomText = nom.getText();
            if (nomText.isEmpty() || !nomText.matches("[a-zA-Z]+")) {
                showErrorAlert("Invalid name! Please enter a valid name containing letters only.");
                return;
            }

            String prenomText = prenom.getText();
            if (prenomText.isEmpty() || !prenomText.matches("[a-zA-Z]+")) {
                showErrorAlert("Invalid last name! Please enter a valid last name containing letters only.");
                return;
            }


            String genderText = gender.getValue().toLowerCase();
            if (genderText.isEmpty() || (genderText.equals("Homme") || genderText.equals("Femme") || genderText.equals("f") || genderText.equals("m"))) {
                showErrorAlert("Invalid gender! Please enter a gender other than 'Homme' or 'Homme', and 'F' or 'M'.");
                return;
            }
            patient newpatient = new patient();
            newpatient.setEmail(email.getText());
            try {
                String rolesJson = objectMapper.writeValueAsString(new String[]{"ROLE_PATIENT"});
                newpatient.setRoles(rolesJson);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

          //  newpatient.setPassword(PasswordEncryptor.encrypt(password.getText()));

           newpatient.setPassword(password.getText());
            newpatient.setNom(nom.getText());
            newpatient.setPrenom(prenom.getText());
            newpatient.setNumtel(Integer.parseInt(numtel.getText()));
         //   LocalDate selectedDate = birth.getValue();
            if (selectedDate != null) {
                LocalDateTime birthDateTime = selectedDate.atStartOfDay();
                newpatient.setBirth(LocalDate.from(birthDateTime));
            }
            String imagePath = profileImage.getText();
            newpatient.setProfileImage(imagePath);
            newpatient.setGender(gender.getValue());

            us.create(newpatient);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Patient added successfully!");
            alert.showAndWait();

            clearFields();
            MyTools.showAlertInfo("Success", "Patient added successfully! ");
            MyTools.goTo("/login.fxml", gender);
        } catch (SQLException e) {
            showErrorAlert("Error adding Patient: " + e.getMessage());
        } catch (NumberFormatException e) {
            showErrorAlert("Invalid phone number! Please enter a valid integer.");
        }
    }
    private void clearFields() {
        nom.clear();
        prenom.clear();
        email.clear();
        numtel.clear();
        password.clear();
       // gender.clear();
           birth.setValue(null);
        profileImage.clear();
        roles.clear();
    }






    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void annuler(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void browseImage(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            userImageView.setImage(image);

            profileImage.setText(file.toURI().toString());
        }

    }

}
