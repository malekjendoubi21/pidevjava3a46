package controles;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.application.Platform;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class stat {
    @FXML
    private PieChart pieChart;
    private final String USER = "root";
    private final String PASS = "";
    String url = "jdbc:mysql://localhost:3306/notreatment1";

    public void initialize() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection(url, USER, PASS)) {
            String sql = "SELECT p.titre, COUNT(c.id) AS NombreCommentaires "
                    + "FROM Publication p "
                    + "LEFT JOIN Commentaire c ON c.publication_id = p.id "
                    + "GROUP BY p.titre";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String titre = rs.getString("titre");
                int nombreCommentaires = rs.getInt("NombreCommentaires");
                pieChartData.add(new PieChart.Data(titre, nombreCommentaires));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        Platform.runLater(() -> {
            pieChart.setData(pieChartData);
        });
    }
}
