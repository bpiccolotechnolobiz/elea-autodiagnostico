package com.ar.tbz.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ar.tbz.conexion.Conexion;
import com.ar.tbz.domain.Legajo;
import com.ar.tbz.domain.Resultado;
import com.ar.tbz.services.EmpleadoService;

@Component
public class SendMail {

	@Autowired
	EmpleadoService empleadoService;

//    public static void main(String[] args) throws Exception {
//    	Resultado resultado = new Resultado();
//    	new SendMail().execute(resultado);
//    }	
//
// 

	public void execute(Resultado resultado) throws Exception {

		Properties propertiesFile = new Properties();
		propertiesFile.load(new FileInputStream(new File(Conexion.BACKEND_PROPERTIES_FILE)));

		boolean recuperarNroLegajoDeTabla = true;
		// boolean recuperarNroLegajoDeTabla = false ;

		// Recipient's email ID needs to be mentioned.
		String to = "autodiagnosticoElea@gmail.com";

		// Sender's email ID needs to be mentioned
		String from = propertiesFile.getProperty("email.from");

		// Assuming you are sending email from through gmails smtp
		String host = propertiesFile.getProperty("email.smtp");

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.put("mail.smtp.host", host);
//        properties.put("mail.smtp.port", "465");  // 995 ' 587   465
		properties.put("mail.smtp.port", propertiesFile.getProperty("email.port")); // 995 ' 587 465
//		properties.put("mail.smtp.ssl.enable", "true");
//		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		// Get the Session object.// and pass
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication("autodiagnosticoElea@gmail.com", "Elea2021");

			}

		});

		// session.setDebug(true);
		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			message.setSubject("This is the Subject Line!");

			Multipart multipart = new MimeMultipart();

			MimeBodyPart attachmentPart = new MimeBodyPart();

			MimeBodyPart textPart = new MimeBodyPart();

			OutputStream file = null;
			Legajo legajo = resultado.getLegajo();
			String nroLegajo = "";
			if (legajo != null) {
				nroLegajo = resultado.getLegajo().getNroLegajo();
			}

			String dni = "";
			int nroLegajoRecuperaDNI = 0;
			// usado para test de funcionamiento
			// recupera datos de la tabla real
			if (recuperarNroLegajoDeTabla) {
				dni = "34567891";
				legajo = empleadoService.findByLegajo("" + nroLegajoRecuperaDNI);

			}
			String archivoNombre = "";
			if (nroLegajo == null) {
				archivoNombre = "TestPDF.pdf";
			} else {
				String fecha = resultado.getFecha_autodiagnostico();
				if (resultado.getLegajo() != null && resultado.getLegajo().getDni() != null) {
					dni = resultado.getLegajo().getDni();
				}
				if (fecha == null) {
					fecha = "2021-07-03T08:23:45";
				}
				archivoNombre = fecha + "-" + dni;
			}

			try {

				archivoNombre = archivoNombre.replace(":", "-");

				// file = new FileOutputStream(new
				// File("c://Users//JulioMOliveira//tmp//"+archivoNombre+ ".pdf"));
				file = new FileOutputStream(new File("c://tmp//mail//" + archivoNombre + ".pdf"));
				// file = new FileOutputStream(new
				// File("c://Users//elea//mail//pdf//"+archivoNombre+ ".pdf"));

				File f = new File("C:\\tmp\\");
				attachmentPart.attachFile(f);
				textPart.setText("This is text");
				multipart.addBodyPart(textPart);
				multipart.addBodyPart(attachmentPart);

			} catch (IOException e) {

				e.printStackTrace();

			}

			message.setContent(multipart);

			System.out.println("sending...");
			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}

	}

}