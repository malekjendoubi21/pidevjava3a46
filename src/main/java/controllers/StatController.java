package controllers;

import Util.DataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StatController {




    Connection ste = DataSource.getInstance().getConn();

    @FXML
    private PieChart pieChart;

    @FXML
    void afficherStatistique(ActionEvent event) {

    }

    private ObservableList<PieChart.Data> contc() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        try {

                 ResultSet resultSet = ste.createStatement().executeQuery("SELECT  resultat_analyse, COUNT(*) FROM dossiermedical GROUP BY resultat_analyse"); {

                while (resultSet.next()) {
                    String nom = resultSet.getString("resultat_analyse");
                    int nombreEvenements = resultSet.getInt(2);

                    PieChart.Data slice = new PieChart.Data( nom + "  :", nombreEvenements);
                    pieChartData.add(slice);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pieChartData;
    }
    @FXML
    void initialize() {
        ObservableList<PieChart.Data> pieChartData = contc();
        pieChart.setData(pieChartData);
    }
}