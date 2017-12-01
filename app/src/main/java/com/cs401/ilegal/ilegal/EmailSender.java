package com.cs401.ilegal.ilegal;

/**
 * Created by pooja on 9/21/2017.
 */

import android.util.Log;

import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {
    public void sendEmail(String host, String port,
                          final String userName, final String password, String toAddress,
                          String subject, String message) throws AddressException,
            MessagingException {

        //SET SMTP SERVER PROPERTIES
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        //CREATE A NEW SESSION WITH AN AUTHENTICATOR
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };

        Session session = Session.getInstance(properties, auth);

        //CREATE A NEW EMAIL MESSAGE
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = { new InternetAddress(toAddress) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());

        //SEND STYLED EMAIL MESSAGE
        msg.setContent(message, "text/html");
        Transport.send(msg);
    }

    public boolean sendEmailToUser(String userEmail, String emailSubject, String emailMessage) {
        //SMTP SERVER INFORMATION
        String host = "smtp.office365.com";
        String port = "587";
        String mailFrom = "info@ilegalselfhelp.com";
        String password = "2017Trojans!";

        //OUTGOING MESSAGE INFORMATION
        String mailTo = "info@ilegalselfhelp.com";
        String subject = "New Message from iLegal User" + userEmail;
        String message = "<body style=\"margin: 0; padding: 0;\">\n" +
                "                <table align=\"center\" border=\"1\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\">\n" +
                "                <tr> <td align=\\\"center\\\" bgcolor=\\\"#3ABEFF\\\" style=\\\"padding: 40px 0 30px 0;\\\">\n" +
                "                <img src= \"\" width=\"300\" height=\"100\" style=\"display: block;\" />\n" +
                "                </td> </tr>\n" +
                "                <tr> <td bgcolor=\"#F2F7F2\" style=\"padding: 40px 30px 40px 30px;\">\n" +
                "                <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "                <tr> <td> <b>From: </b>" + userEmail + "</td> </tr>\n" +
                "                <tr> <td style=\"padding: 20px 0 30px 0;\"> <b>Subject: </b>" + emailSubject + "\n" +
                "                <br/><br/> <b>Message: </b> " + emailMessage + "</td> </tr>\n" +
                "                </table>\n" +
                "                </td> </tr>\n" +
                "                <tr> <td bgcolor=\"#2176FF\" style=\"padding: 30px 30px 30px 30px;\">\n" +
                "                <font color=\"white\">iLegalSelfHelp, 2017 </font>\n" +
                "                </td> </tr>\n" +
                "                </table>\n" +
                "                </body>";

        //SEND EMAIL
        try {
            sendEmail(host, port, mailFrom, password, mailTo, subject, message);
            return true;
        } catch (Exception ex) {
            Log.v("email", ex.getMessage(), ex);
            //Log.v("email", ex.getLocalizedMessage());
            //System.out.println(ex.getMessage());
            //System.out.println(ex.getLocalizedMessage());
            return false;
        }
    }
}