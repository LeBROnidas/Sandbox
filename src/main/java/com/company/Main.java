package com.company;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.util.Properties;
import java.util.Scanner;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;

import java.awt.print.*;

public class Main {

    public static void main(String[] args) {
        // write your code here
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("----------------------------------------------------");
            System.out.println("Sandbox Menue");
            System.out.println("Send Mail without Authentication...                1");
            System.out.println("Send Mail with Authentication...                   2");
            System.out.println("Create PDF...                                      3");
            System.out.println("Print PDF...                                       2");
            System.out.println("exit...                                            5");
            System.out.println("----------------------------------------------------");
            System.out.println("select: ");

            String input = scanner.nextLine();

            switch (Integer.parseInt(input)) {
                case 1:
                    sendWithoutAuthentication();
                    break;
                case 2:
                    sendWithAuthentication();
                    break;
                case 3:
                    createPDF();
                    break;
                case 4:
                    printPDF("TestCustomer.pdf");
                    break;
                case 5:
                    System.exit(0);
                    break;
                default:
                    System.out.println("invalid input...");
            }
        }


        // sendWithoutAuthentication();


    }

    public static void sendWithoutAuthentication() {
        System.out.println("SimpleEmail Start");

        String smtpHostServer = "192.168.4.8";
        String emailID = "bruno.maglica@hotmail.com";

        Properties props = System.getProperties();

        props.put("mail.smtp.host", smtpHostServer);

        Session session = Session.getInstance(props, null);
        sendEmail(session, emailID, "SimpleEmail Testing Subject", "SimpleEmail Testing Body");

    }

    public static void sendWithAuthentication() {
        final String fromEmail = "maglicabruno@gmail.com"; //requires valid gmail id
        final String password = "alpha007"; // correct password for gmail id
        final String toEmail = "bruno.maglica@hotmail.com"; // can be any email id

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

        sendEmail(session, toEmail, "10:1 dass ich den scheiß mit der Mail hinkriege.", "Was Los du warmer Hund bist du noch immer nicht fertig?");
    }

    public static void sendEmail(Session session, String toEmail, String subject, String body) {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Test Titel", JOptionPane.OK_CANCEL_OPTION);
        }
    }

    public static void createPDF() {

        String costumerName = "TestCustomer";

        String fileName = costumerName + ".pdf";
        FileOutputStream fos = null;
        File pdf = new File(fileName);
        try {
            pdf.createNewFile();
            fos = new FileOutputStream(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        PdfWriter writer = new PdfWriter(fos);

        PdfDocument pdfDocument = new PdfDocument(writer);

        Document document = new Document(pdfDocument, PageSize.A6);


        Paragraph title = new Paragraph("Reservierung");
        title.setFontSize(18);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setBold();

        document.add(title);

        document.add(new Paragraph("Beispiel Adresse 14/23/12")).setTextAlignment(TextAlignment.CENTER).setFontSize(10);

        document.add(new Paragraph("1234").setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph(costumerName).setBold().setTextAlignment(TextAlignment.CENTER).setFontSize(11));
        document.add(new Paragraph("12.10.2018 - 13.10.2018").setTextAlignment(TextAlignment.CENTER).setFontSize(11));

        document.add(new Paragraph("Raum 13/8").setFontSize(12).setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Lorep Ipsum Dolor")).setTextAlignment(TextAlignment.CENTER).setFontSize(11);

        document.add(new Paragraph("Security is very important").setFontSize(11).setBold());

        document.add(new Paragraph("Beispieltext").setFontSize(10));

        document.add(new Paragraph("Copyright Information").setTextAlignment(TextAlignment.CENTER).setFontSize(8));
        document.close();

        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfDocument.close();

        System.out.println("PDF was created.");
    }

    public static void printPDF(String filename) {
        try {
            PDDocument doc = PDDocument.load(new File("pdf" + File.separator + filename));
            PrinterJob job = PrinterJob.getPrinterJob();

            Paper paper = new Paper();
            paper.setSize(226, 226); // in 1/72 inch, 8 cm = 3,1496 inches -> 226.7712 width
            paper.setImageableArea(0, 0, paper.getWidth(), paper.getHeight());

            PageFormat pageFormat = new PageFormat();
            pageFormat.setPaper(paper);

            Book book = new Book();
            book.append(new PDFPrintable(doc, Scaling.SHRINK_TO_FIT), pageFormat, doc.getNumberOfPages());
            job.setPageable(book);

            job.print();
        } catch (PrinterException e) {
            System.out.println("Printing failed");
        } catch (IOException e) {
            System.out.println("Printing failed");
        }
        System.out.println("Printing successfull");
    }

}
