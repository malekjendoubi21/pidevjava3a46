package controles;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.PieChart;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.MyDatabase;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StatisticsController {

    @FXML
    private PieChart pieChart;
    private Connection connection;
    private Map<String, Integer> stats = new HashMap<>();
    private int totalDonations = 0;

    public void initialize() {

        connection = MyDatabase.getInstance().getConnection();
        loadChartData();
    }

    public void loadChartData() {
        String query = "SELECT organisation.nom, COUNT(don.id) as totalDons FROM don JOIN organisation ON don.organisation_id = organisation.id GROUP BY organisation.nom";

        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            stats.clear();
            totalDonations = 0;

            while (rs.next()) {
                String orgName = rs.getString("nom");
                int total = rs.getInt("totalDons");
                stats.put(orgName, total);
                totalDonations += total;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        updatePieChart();
    }

    private void updatePieChart() {
        pieChart.getData().clear();
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            String orgName = entry.getKey();
            int total = entry.getValue();
            double percentage = ((double) total / totalDonations) * 100;
            PieChart.Data slice = new PieChart.Data(orgName + " (" + String.format("%.2f", percentage) + "%)", total);
            pieChart.getData().add(slice);
        }

        pieChart.setLegendVisible(false);
        pieChart.setLabelsVisible(true);
        pieChart.setStartAngle(90);
        pieChart.setTitle("Donation Statistics by Organisation");
    }

    @FXML
    private void exportToCSV() {
        StringBuilder csvContent = new StringBuilder();
        csvContent.append("Organisation,Number of Donations\n");
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            csvContent.append(entry.getKey()).append(",").append(entry.getValue()).append("\n");
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv"));
        File selectedFile = fileChooser.showSaveDialog(pieChart.getScene().getWindow());
        if (selectedFile != null) {
            try (FileOutputStream fileOut = new FileOutputStream(selectedFile)) {
                fileOut.write(csvContent.toString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void exportAsImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Chart as Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"));
        File selectedFile = fileChooser.showSaveDialog(pieChart.getScene().getWindow());
        if (selectedFile != null) {
            WritableImage image = pieChart.snapshot(new SnapshotParameters(), null);
            try {
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                ImageIO.write(bufferedImage, "png", selectedFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void refreshChart() {
        loadChartData();
    }

    public void onClose() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void retour(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/listOrg.fxml")));
        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }
}
