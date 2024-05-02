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
import models.docteur;
import services.docteurservice;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
public class adddocteur {

    @FXML
    private Button ajouter;

    @FXML
    private DatePicker birth;

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
    private ImageView userImageView;
    @FXML
    private TextField roles;
    @FXML
    private ComboBox<String> specialite;

    private final docteurservice ds =new docteurservice();
    public void initialize() {
        gender.setItems(FXCollections.observableArrayList("Homme", "Femme"));
        specialite.setItems(FXCollections.observableArrayList("Généraliste", "Dermatologue" ,"Cardiologue","Ophtalmologue","Pédiatre","Neurologue"));

    }

    @FXML
    void adddocteur(ActionEvent event) {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String emailText = email.getText();
            if (emailText.isEmpty() || !emailText.contains("@") || !emailText.endsWith(".com")) {
                showErrorAlert("Invalid email! Please enter a valid email address.");
                return;
            }

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

            LocalDate minBirthdate = LocalDate.now().minusYears(27);

            LocalDate selectedDate = birth.getValue();
            if (selectedDate == null || selectedDate.isAfter(LocalDate.now()) || selectedDate.isAfter(minBirthdate)) {
                showErrorAlert("Invalid birth date! Please enter a valid date where the user is at least 27 years old.");
                return;
            }

            String genderText = gender.getValue().toLowerCase();
            if (genderText.isEmpty() || (genderText.equals("feminin") || genderText.equals("masculine") || genderText.equals("f") || genderText.equals("m"))) {
                showErrorAlert("Invalid gender! Please enter a gender other than 'Feminin' or 'Masculine', and 'F' or 'M'.");
                return;
            }

            String specialiteText = specialite.getValue();
            if (specialiteText.isEmpty()) {
                showErrorAlert("Speciality field is empty! Please enter a valid speciality.");
                return;
            }
            docteur newUser = new docteur();
            newUser.setEmail(email.getText());
            try {
                String rolesJson = objectMapper.writeValueAsString(new String[]{"ROLE_DOCTEUR"});
                newUser.setRoles(rolesJson);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            newUser.setPassword(password.getText());
            newUser.setNom(nom.getText());
            newUser.setPrenom(prenom.getText());
            newUser.setNumtel(Integer.parseInt(numtel.getText())); // Conversion en int
          //  LocalDate selectedDate = birth.getValue();
            if (selectedDate != null) {
                LocalDateTime birthDateTime = selectedDate.atStartOfDay();
                newUser.setBirth(LocalDate.from(birthDateTime));
            }
            String imagePath = profileImage.getText();
            newUser.setProfileImage(imagePath);
            newUser.setGender(gender.getValue());
            newUser.setSpecialite(specialite.getValue());
            String logoClassName = "cup";

            ds.create(newUser);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("User added successfully!");
            alert.showAndWait();

            clearFields();
        } catch (SQLException e) {
            showErrorAlert("Error adding user: " + e.getMessage());
        } catch (NumberFormatException e) {
            showErrorAlert("Invalid phone number! Please enter a valid integer.");
        }

    }
    @FXML
    void browseImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            userImageView.setImage(image);

            profileImage.setText(file.toURI().toString());
        }
    }
    private void clearFields() {
        nom.clear();
        prenom.clear();
        email.clear();
        numtel.clear();
        password.clear();
        //gender.clear();
          birth.setValue(null);
        profileImage.clear();
        roles.clear();
      //  specialite.clear();
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
}