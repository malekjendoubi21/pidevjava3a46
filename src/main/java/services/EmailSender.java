package services;

import Util.MailApi;

import javax.mail.MessagingException;

public class EmailSender {

    public static void sendEmail(String recipient, String subject, String body) {
        try {
            MailApi.sendEmail(recipient, subject, body);
        } catch (MessagingException e) {
            System.out.println("Failed to send email: " + e.getMessage());
        }
    }
}
