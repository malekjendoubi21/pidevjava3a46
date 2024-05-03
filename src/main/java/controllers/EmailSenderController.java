package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import services.EmailSender;

public class EmailSenderController {

    @FXML
    private TextField recipientField;

    @FXML
    private TextField subjectField;

    @FXML
    private TextArea messageArea;

    @FXML
    private Label statusLabel;

    @FXML
    public void sendEmail() {
        String recipient = recipientField.getText();
        String subject = subjectField.getText();
        String message = messageArea.getText();

        // Validate input fields (e.g., check if recipient email is valid)
        if (recipient.isEmpty() || subject.isEmpty() || message.isEmpty()) {
            statusLabel.setText("Please fill in all fields.");
            return;
        }

        // Send email using EmailSender service
        EmailSender.sendEmail(recipient, subject, message);
        statusLabel.setText("Email sent successfully!");
    }
}
