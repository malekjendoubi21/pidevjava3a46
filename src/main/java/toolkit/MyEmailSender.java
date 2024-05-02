package toolkit;

import controles.ForgottenPwdController;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
public class MyEmailSender {



    public static void send(String to,String sub, String content){
        //Get properties object
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("malekeljendoubi@gmail.com","etobfmeytrntffat");
                    }
                });
        //compose message
        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
            message.setSubject(sub);
            // message.setText(msg);
            message.setContent(content,"text/html; charset=utf-8");
            //send message
            Transport.send(message);
            System.out.println("message sent successfully");
        } catch (MessagingException e) {throw new RuntimeException(e);}
    }


}






class SendMailSSL{
    public static void main(String[] args) {
        int code= 12344;
        ForgottenPwdController fpdc= new ForgottenPwdController();
        String mail= fpdc.getHtmlContent(String.valueOf(code));


        MyEmailSender.send("malekeljendoubi@gmail.com","Reset your password ",mail);


    }
}
