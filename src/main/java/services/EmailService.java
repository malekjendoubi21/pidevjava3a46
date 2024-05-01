package services;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class EmailService {

    public static void sendEmailWithAttachmentAndHTML(String recipientEmail, String subject, String htmlContent) throws MessagingException, IOException {
        // Set up mail server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        // Sender's credentials
        String senderEmail = "notreatment.noreply@gmail.com";
        String senderPassword = "vcyr lkkb opzr zjuu";

        // Create session
        javax.mail.Session session = javax.mail.Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        // Create email message
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject(subject);

        // Create multipart content
        Multipart multipart = new MimeMultipart();

        // Create HTML body part
        MimeBodyPart htmlBodyPart = new MimeBodyPart();
        htmlBodyPart.setContent(htmlContent, "text/html; charset=utf-8");
        multipart.addBodyPart(htmlBodyPart);

       /* // Add attachment
        MimeBodyPart attachmentBodyPart = new MimeBodyPart();
        attachmentBodyPart.attachFile(new File(attachmentFilePath));
        multipart.addBodyPart(attachmentBodyPart);*/

        // Set content
        message.setContent(multipart);

        // Send email
        Transport.send(message);
    }
}
