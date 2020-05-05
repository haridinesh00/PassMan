package firstproject;

//Java program to send email 

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Scanner;
class mail {

    public static void main(String[] args) {
    	
        final String username = "freetrialprime0000@gmail.com";
        final String password = "Tryitfree00";
        Scanner scn = new Scanner(System.in);
        Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        prop.put("mail.smtp.user", username);
        prop.put("mail.smtp.password", password);
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        System.out.println("Enter email id to send mail.....");
        String mail_id=scn.nextLine();
        System.out.println("Enter subject of message to sent");
        String msg_sub=scn.nextLine();
        System.out.println("Enter message to sent(enter msg in a single line)");
        String msg_text=scn.nextLine();
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(mail_id)
            );
            message.setSubject(msg_sub);
            message.setText(msg_text);

            Transport.send(message);
            /*
            System.out.println("Message Sent!");
            System.out.println("Message Sent!");*/
            System.out.println("Message Sent!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    scn.close();
    }

}