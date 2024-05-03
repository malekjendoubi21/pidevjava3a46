/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;


import com.example.gestion_Yasmine.GestionYasmine;
import entities.Consultation;

import entities.DossierMedical;
import entities.Statut;
import javafx.scene.chart.PieChart;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import services.ServiceConsultation;
import services.ServiceDossierMedical;
import services.ServiceDossierMedical;
import entities.DossierMedical;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * FXML Controller class
 *
 * @author winxspace
 */
public class FXMLDossierMController implements Initializable {


    public TextField tf_patient_id;
    public TextArea tfmaladie;
    public TextField tfrecherche;
    public TextField tfgrpsang;
 
    @FXML
    private ComboBox<String> status;
     @FXML
    private TableView<DossierMedical> tvresultat;
      ServiceDossierMedical str=new ServiceDossierMedical();
    ObservableList<String> data=FXCollections.observableArrayList();
     // @FXML
      // private TableColumn<DossierMedical, Integer> cid;
       @FXML
       private TableColumn<DossierMedical, Integer> c_patient_id;
        @FXML
       private TableColumn<DossierMedical, String> cgrpsang;
        @FXML
        private TableColumn<DossierMedical, Statut> cstatus;

        @FXML
        private TableColumn<DossierMedical, String> cmaladie;

    private ServiceDossierMedical serviceDossierMedical = new ServiceDossierMedical();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<String> sts = new ArrayList<String>();
        sts.add("POSITIVE");
        sts.add("NEGATIVE");


        data.addAll(sts);
        status.setItems(data);
        tfrecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            rechercher(new ActionEvent()); // Appeler la méthode de recherche dès que le texte change
        });
    }
    private void loadDossier() {
        List<DossierMedical> consultations = serviceDossierMedical.afficher();
        ObservableList<DossierMedical> dataList = FXCollections.observableArrayList(consultations);
        tvresultat.setItems(dataList);
    }

    @FXML
    private void afficher(ActionEvent event) {
        ObservableList<DossierMedical> dataList = FXCollections.observableArrayList(str.afficher());
       // cid.setCellValueFactory(new PropertyValueFactory<>("id"));
        c_patient_id.setCellValueFactory(new PropertyValueFactory<>("patient_id"));
        cgrpsang.setCellValueFactory(new PropertyValueFactory<>("groupesang"));
        cmaladie.setCellValueFactory(new PropertyValueFactory<>("maladie_chronique"));
        cstatus.setCellValueFactory(new PropertyValueFactory<>("resultat_analyse"));
        tvresultat.setItems(dataList);
    }

@FXML
private void display(ActionEvent event) {
    DossierMedical selectedDossier = tvresultat.getSelectionModel().getSelectedItem(); // Get selected item from table view
    if (selectedDossier != null) {
        // Display selected row data in text fields
       tf_patient_id.setText(String.valueOf(selectedDossier.getPatient_id()));
        tfgrpsang.setText(selectedDossier.getGroupesang());
        tfmaladie.setText(selectedDossier.getMaladie_chronique());



    }
}

@FXML
private void modifier(ActionEvent event) {

    DossierMedical selectedDossier = tvresultat.getSelectionModel().getSelectedItem(); // Get selected item from table view
    if (selectedDossier != null) {
        // Update selected row data with values from text fields
        selectedDossier.setPatient_id(Integer.parseInt(tf_patient_id.getText()));
        selectedDossier.setGroupesang(tfgrpsang.getText());
        String sts = status.getSelectionModel().getSelectedItem().toString();
        switch(sts) {
            case "POSITIVE" : {
                selectedDossier.setResultat_analyse(Statut.POSITIVE);
                break;
            }
            case "NEGATIVE" : {
                selectedDossier.setResultat_analyse(Statut.NEGATIVE);
                break;
            }

            default : {
                selectedDossier.setResultat_analyse(Statut.POSITIVE);
                break;
            }
        }

        selectedDossier.setMaladie_chronique(tfmaladie.getText());
        str.modifier(selectedDossier, selectedDossier.getId());
        tf_patient_id.clear();
        tfmaladie.clear();
        tfgrpsang.clear();
        tvresultat.refresh();
        loadDossier();
    }
}


    @FXML
    private void supprimer(ActionEvent event) throws Exception {
        ServiceDossierMedical sr = new ServiceDossierMedical();
        if(tvresultat.getSelectionModel().getSelectedItem()!=null){
            int id=tvresultat.getSelectionModel().getSelectedItem().getId();
            sr.supprimer(id);
            loadDossier();

        }
    }







    @FXML
    private void ajouterDossier(ActionEvent event)
            throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXMLAjoutDossier.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
        {
            Stage stageclose = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stageclose.close();

        }
    }

    @FXML
    private void gstReponses(ActionEvent event)throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXMLConsul.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }


    @FXML
    private void handleGroupe(ActionEvent event) {
        try {
            // Load the FXML file for the new interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXMLGrpCarte.fxml"));
            Parent root = loader.load();

            // Create a new stage for the new interface
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Consultation Group");

            // Show the new interface
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void trierParGroupeSanguin(ActionEvent event) {
        ObservableList<DossierMedical> dataList = FXCollections.observableArrayList(str.trierParGroupeSanguin());
        tvresultat.setItems(dataList);
    }

    @FXML
    private void trierParResultatAnalyse(ActionEvent event) {
        ObservableList<DossierMedical> dataList = FXCollections.observableArrayList(str.trierParResultatAnalyse());
        tvresultat.setItems(dataList);
    }

    @FXML
    private void rechercher(ActionEvent event) {
        String termeRecherche = tfrecherche.getText().trim().toLowerCase(); // Récupérer le terme de recherche et le normaliser
        if (termeRecherche.isEmpty()) {
            // Si le champ de recherche est vide, afficher tous les dossiers médicaux
            loadDossier();
        } else {
            // Sinon, effectuer une recherche basée sur le terme de recherche
            List<DossierMedical> resultList = str.rechercherDossier(termeRecherche);
            ObservableList<DossierMedical> dataList = FXCollections.observableArrayList(resultList);
            tvresultat.setItems(dataList);
        }
    }


    public void gotostat(ActionEvent event){
         try {
        // Load the FXML file for the new interface
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/stat.fxml"));
        Parent root = loader.load();

        // Create a new stage for the new interface
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Statistiques");

        // Show the new interface
        stage.show();
         } catch (IOException e) {
        e.printStackTrace();
        }
    }

    public void generatepdf() {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.setLeading(14.5f);
                contentStream.newLineAtOffset(25, 725);

                // Header
                contentStream.showText("Dossier Medical List:");
                contentStream.newLine();
                contentStream.newLine();

                // Fetch your DossierMedical list
                ObservableList<DossierMedical> dossiermedicalList = FXCollections.observableArrayList(str.afficher());

                // Example of adding each DossierMedical to the PDF
                for (DossierMedical dossierMedical : dossiermedicalList) {
                    String text = "ID: " + dossierMedical.getId() +
                            ", Patient: " + dossierMedical.getPatient_id() +
                            ", Groupe Sang: " + dossierMedical.getGroupesang() +
                            ", Maladie Chronique: " + dossierMedical.getMaladie_chronique() +
                            ", Resultat Analyse: " + dossierMedical.getResultat_analyse();
                    // Replace tab characters with spaces
                    text = text.replace("\t", "    ");
                    contentStream.showText(text);
                    contentStream.newLine();
                }

                contentStream.endText();
            }
            // Save the document
            String filename = "DossierMedicalList.pdf";
            document.save(filename);
            System.out.println("PDF created: " + filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
