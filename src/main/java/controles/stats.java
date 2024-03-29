package controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import models.reclamation;
import services.ReclamationService;

import java.io.*;
import java.sql.SQLException;
import java.util.List;


public class stats{
    private final ReclamationService rs=new ReclamationService();
    @FXML
    private Button backk;

    @FXML
    private Button displ;
    @FXML
    void displaystats(ActionEvent event) throws IOException, SQLException, InterruptedException {
        List<reclamation> list = rs.read();
        FileWriter fileWriter = new FileWriter("C:/Users/Mega-PC/Desktop/Workshop/intermed.txt"); // Change the file path
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        for (reclamation rec : list) {
            bufferedWriter.write(rec.getContenu()+"\n");
        }

        bufferedWriter.close();

        String pythonScriptPath = "C:/Users/Mega-PC/Desktop/Workshop/fir.py";
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("python", pythonScriptPath);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        int exitCode = process.waitFor();

        BufferedReader reader1 = new BufferedReader(new FileReader("C:/Users/Mega-PC/Desktop/Workshop/res.txt"));


        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Numbers");
        yAxis.setLabel("Values");


        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Number Data");


        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Data");

            String linee;
        while ((linee = reader1.readLine()) != null) {
            String[] parts = linee.split(":");

            if (parts.length == 2) {
                String category = parts[0];
                int value = Integer.parseInt(parts[1]);

                series.getData().add(new XYChart.Data<>(category, value));
            }
        }
        reader.close();
        barChart.setTitle("Stats reclamations");
        barChart.getData().add(series);
        System.out.println(barChart.getData());
        StackPane root = new StackPane();
        root.getChildren().add(barChart);

        Scene scene = new Scene(root, 800, 600);

        Stage stage = new Stage();
        stage.setTitle("Reclamation Stats");
        stage.setScene(scene);
        stage.show();


    }

    @FXML
    void gobackk(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/homeb.fxml"));
        backk.getScene().setRoot(root);

    }

}
