package com.company;

import com.sun.mail.util.MailConnectException;
import javafx.scene.control.Alert;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.util.Date;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
	// write your code here

            sendWithoutAuthentication();

    }

    public static void sendWithoutAuthentication()
    {
        System.out.println("SimpleEmail Start");

        String smtpHostServer = "192.168.4.8";
        String emailID = "bruno.maglica@hotmail.com";

        Properties props = System.getProperties();

        props.put("mail.smtp.host", smtpHostServer);

        Session session = Session.getInstance(props, null);
        sendEmail(session, emailID,"SimpleEmail Testing Subject", "SimpleEmail Testing Body");

    }

    public static void sendWithAuthentication()
    {
        final String fromEmail = "maglicabruno@gmail.com"; //requires valid gmail id
        final String password = "alpha007"; // correct password for gmail id
        final String toEmail = "brunomaglica@hotmail.com"; // can be any email id

        System.out.println("TLSEmail Start");
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);

            }
        };
        Session session = Session.getInstance(props, auth);

        sendEmail(session, toEmail,"10:1 dass ich den scheiß mit der Mail hinkriege.", "Was Los du warmer Hund bist du noch immer nicht fertig?");
    }

    public static void sendEmail(Session session, String toEmail, String subject, String body){
        try
        {
            MimeMessage msg = new MimeMessage(session);

            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("no_reply@example.com", "NoReply-JD"));

            msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));

            msg.setSubject(subject, "UTF-8");

            msg.setText(body, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            System.out.println("Message is ready");
            Transport.send(msg);

            System.out.println("EMail Sent Successfully!!");
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Test Titel", JOptionPane.OK_CANCEL_OPTION);
        }
    }
}
