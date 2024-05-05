package controles;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.layout.VBox;
import models.rendezvouz;
import services.rendezvouzService;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.List;

public class CalendarController {
    ZonedDateTime dateFocus;
    ZonedDateTime today;
    @FXML
    private FlowPane calendar;

    @FXML
    private Text month;
    @FXML
    private AnchorPane main;

    @FXML
    private Text year;
    private YearMonth currentYearMonth;

    private rendezvouzService RendezvouzService;

    public CalendarController() {
        RendezvouzService = new rendezvouzService();
        currentYearMonth = YearMonth.now();
    }

    @FXML
    void forwardOneMonth(ActionEvent event) {
        currentYearMonth = currentYearMonth.plusMonths(1);
        try {
            drawCalendar();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void backOneMonth(ActionEvent event) {
        currentYearMonth = currentYearMonth.minusMonths(1);
        try {
            drawCalendar();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void drawCalendar() throws SQLException {
        year.setText(String.valueOf(currentYearMonth.getYear()));
        month.setText(currentYearMonth.getMonth().toString());

        calendar.getChildren().clear();

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        int monthMaxDate = currentYearMonth.lengthOfMonth();
        int dateOffset = ZonedDateTime.of(currentYearMonth.getYear(), currentYearMonth.getMonthValue(), 1, 0, 0, 0, 0, ZonedDateTime.now().getZone()).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j + 1) + (7 * i);
                if (calculatedDate > dateOffset) {
                    int currentDate = calculatedDate - dateOffset;
                    if (currentDate <= monthMaxDate) {
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = - (rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);

                        LocalDate currentDateLocalDate = LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonth(), currentDate);
                        rendezvouzService service = new rendezvouzService();
                        List<rendezvouz> rdvs = service.getRendezvousByDate(Timestamp.valueOf(currentDateLocalDate.atStartOfDay()));
                        if (!rdvs.isEmpty()) {
                            VBox rendezvousInfo = new VBox();
                            rendezvousInfo.setStyle("-fx-font-weight: bold"); // Make the text bold
                            double rendezvousInfoTranslationY = (rectangleHeight / 2) * 0.75;
                            rendezvousInfo.setTranslateY(rendezvousInfoTranslationY);
                            for (rendezvouz rdv : rdvs) {
                                Text emailText = new Text(rdv.getEmail());
                                Text dateText = new Text(rdv.getDaterdv().toString()); // Assuming rdv.getDaterdv() returns a Timestamp
                                rendezvousInfo.getChildren().addAll(emailText, dateText);
                            }
                            stackPane.getChildren().add(rendezvousInfo);
                            rectangle.setFill(Color.RED);
                        }
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }

    private boolean hasEvents(LocalDate date) {
        try {
            // Assuming you have a method in your rendezvouzService to get events by date
            rendezvouzService eservice = new rendezvouzService();
            List<rendezvouz> events = eservice.getRendezvousByDate(Timestamp.valueOf(date.atStartOfDay()));

            // Check if there are any events for the given date
            return !events.isEmpty();
        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    void initialize() {
        try {
            drawCalendar();
        } catch (SQLException rdv) {
            throw new RuntimeException(rdv);
        }
    }

    @FXML
    void events(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Calendar.fxml"));
            main.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("error"+e.getMessage());
        }
    }
}
