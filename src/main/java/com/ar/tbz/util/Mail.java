package com.ar.tbz.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
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
import com.ar.tbz.domain.Parametro;
import com.ar.tbz.domain.Resultado;
import com.ar.tbz.services.EstadisticaService;
import com.ar.tbz.services.LugarAccesoService;
import com.ar.tbz.services.PreguntaService;
import com.itextpdf.text.Image;

@Component
public class Mail {

	private static final String QR_PNG = "qr.png";
	@Autowired
	LugarAccesoService lugarAccesoService;
	@Autowired
	EstadisticaService estadisticaService;
	@Autowired
	PreguntaService preguntaService;
	@Autowired
	PdfCreateFile pdfCreateFile;
	@Autowired
	QRService qrService;

	public void envioMail2(Resultado resultado) throws Exception {
		System.out.println("Enviando mail");

		Properties propertiesFile = new Properties();
		propertiesFile.load(new FileInputStream(new File(Conexion.BACKEND_PROPERTIES_FILE)));

		// ---------------------------------------------CONFIG ENVIO MAIL

		// Sender's email ID needs to be mentioned
		String from = propertiesFile.getProperty("email.from");
		String password = propertiesFile.getProperty("email.password");
		String nameFrom = propertiesFile.getProperty("email.name");

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.put("mail.smtp.host", propertiesFile.getProperty("email.smtp"));
		properties.put("mail.smtp.port", propertiesFile.getProperty("email.port"));
		properties.put("mail.smtp.ssl.enable", "false");
		properties.put("mail.smtp.auth", "false");
//		properties.put("mail.smtp.ssl.enable", "true");
//		properties.put("mail.smtp.auth", "true");

		// Get the Session object.// and pass username and password
		Session session = Session.getDefaultInstance(properties);
//		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
//			protected PasswordAuthentication getPasswordAuthentication() {
//				return new PasswordAuthentication(from, password);
//			}
//		});

		// Used to debug SMTP issues
		session.setDebug(false);

		// Recipient's email ID needs to be mentioned.
		String toUsuario = resultado.getLegajo().getEmailUsuario();
		String toConsultorio = propertiesFile.getProperty("email.to");
		List<Parametro> parametros = estadisticaService.obtenerParametros();
		for (Parametro param : parametros) {
			if (param.getDescripcionParametro().contains("Email del consultorio")) {
				toConsultorio = param.getValorParametro();
			}
		}

		// ---------------------------------------------CONFIG CONTENIDO MAIL
		String cuerpoMail = "";

		// Nombre del archivo PDF
		String timestamp = DateUtil.formatSdf("yyyyMMddHHmm", new Date());
		String fileNamePDF = resultado.getLegajo().getDni() + timestamp + ".pdf";
		// Nombre de la imagen QR
		String fileNameQR = resultado.getLegajo().getDni() + QR_PNG;

		// CID imgs
		String cidKeyQR = "QR";
		Map<String, String> inlineImages = new HashMap<String, String>();
		if (resultado.isResultado()) {
			inlineImages.put(cidKeyQR, fileNameQR);
		}

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from, nameFrom));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(toUsuario));

			// Set Subject: header field
			String resultadoLeyenda = "";
			if (resultado.isResultado()) {
				resultadoLeyenda = "Habilitado";
			} else {
				resultadoLeyenda = "No habilitado";
			}
			message.setSubject(resultado.getLegajo().getNombre() + " " + resultado.getLegajo().getApellido()
					+ " - Resultado de Autodiagnóstico: " + resultadoLeyenda);

			// Hora del envio
			message.setSentDate(new Date());

			Multipart multipart = new MimeMultipart();

			if (resultado.isResultado()) {
				// ---------------QR
				qrService.generarQR(fileNameQR, resultado);

				// ---------------PDF
				pdfCreateFile.generarPDF(fileNamePDF, fileNameQR, resultado);

				MimeBodyPart pdfPart = new MimeBodyPart();

				DataSource source = new FileDataSource(fileNamePDF); // RUTA + NOMBRE DEL ARCHIVO A DESCARGAR
				pdfPart.setDataHandler(new DataHandler(source));
				pdfPart.setFileName(fileNamePDF); // NOMBRE CON EL CUÁL SE VA A DESCARGAR

				pdfPart.setDisposition(MimeBodyPart.ATTACHMENT);
				multipart.addBodyPart(pdfPart);
			}

			// ---------------CUERPO MAIL
			cuerpoMail = crearCuerpoMail(resultado, cidKeyQR);

			// Creo la parte del mensaje HTML
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setContent(cuerpoMail, "text/html; charset=utf-8");

			// Agregar la parte del mensaje HTML al multiPart
			multipart.addBodyPart(mimeBodyPart);

			// ---------------AGREGAR IMAGENES AL MULITPART
			if (inlineImages != null && inlineImages.size() > 0) {
				Set<String> setImageID = inlineImages.keySet();

				for (String contentId : setImageID) {
					MimeBodyPart imagePart = new MimeBodyPart();
					imagePart.setHeader("Content-ID", "<" + contentId + ">");
					imagePart.setDisposition(MimeBodyPart.INLINE);

					String imageFilePath = inlineImages.get(contentId);
					try {
						imagePart.attachFile(imageFilePath);
					} catch (IOException ex) {
						ex.printStackTrace();
					}

					multipart.addBodyPart(imagePart);
				}
			}

			// Agregar el multipart al cuerpo del mensaje
			message.setContent(multipart);

			System.out.println("sending...");

			// Send message
			Transport.send(message);
			System.out.println("Mail enviado a Usuario");

			// Enviando al Médico en caso No habilitado
			if (!resultado.isResultado()) {
				String[] toAddresses = toConsultorio.replace(" ", "").split(",");
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddresses[0]));

				if (toAddresses.length > 1) {
					InternetAddress[] cc = new InternetAddress[toAddresses.length - 1];
					for (int i = 1; i <= toAddresses.length - 1; i++) {
						cc[i - 1] = new InternetAddress(toAddresses[i]);
					}

					message.addRecipients(Message.RecipientType.CC, cc);
				}

				message.setContent(cuerpoMail, "text/html; charset=utf-8");

				System.out.println("sending...");

				// Send message
				Transport.send(message);
				System.out.println("Mail enviado a Consultorio Médico");
			}

			// Eliminar PDF y QR
			if (resultado.isResultado()) {
				Path pathFileNamePDF = Paths.get(fileNamePDF);
				Files.delete(pathFileNamePDF);
				Path pathFileNameQR = Paths.get(fileNameQR);
				Files.delete(pathFileNameQR);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ENVIO MAIL
	public void envioMail(Resultado resultado) throws Exception {
		System.out.println("Enviando mail");

		// Sender's email ID needs to be mentioned
//		String from = "test.technolobiz@gmail.com";
//		String password = "test.0321";
//		String password = "test.0321";

		Properties propertiesFile = new Properties();
		propertiesFile.load(new FileInputStream(new File(Conexion.BACKEND_PROPERTIES_FILE)));

		// Recipient's email ID needs to be mentioned.
		String to = resultado.getLegajo().getEmailUsuario();
//		String to2 = "consultorio.medico@elea.com";
		String to2 = propertiesFile.getProperty("email.to");
		List<Parametro> parametros = estadisticaService.obtenerParametros();
		for (Parametro param : parametros) {
			if (param.getDescripcionParametro().contains("Email del consultorio")) {
				to2 = param.getValorParametro();
			}
		}
		// Assuming you are sending email from through gmails smtp
//		String host = "smtp.gmail.com";

		String from = propertiesFile.getProperty("email.from");
		String password = propertiesFile.getProperty("email.password");
		String nameFrom = propertiesFile.getProperty("email.name");
		// Assuming you are sending email from through gmails smtp

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.put("mail.smtp.host", propertiesFile.getProperty("email.smtp"));
//		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.port", propertiesFile.getProperty("email.port"));

		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");
//		properties.put("mail.smtp.ssl.enable", "false");
//		properties.put("mail.smtp.auth", "false");

		// Get the Session object.// and pass username and password
		Session session = Session.getDefaultInstance(properties);
//		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
//			protected PasswordAuthentication getPasswordAuthentication() {
//				return new PasswordAuthentication(from, password);
//			}
//		});

		// Used to debug SMTP issues
		session.setDebug(false);

		String cuerpoMail = "";
		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from, nameFrom));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			String resultadoLeyenda = "";
			if (resultado.isResultado()) {
				resultadoLeyenda = "Habilitado";
			} else {
				resultadoLeyenda = "No habilitado";
			}
			message.setSubject(resultado.getLegajo().getNombre() + " " + resultado.getLegajo().getApellido()
					+ " - Resultado de Autodiagnóstico: " + resultadoLeyenda);

			// Hora del envio
			message.setSentDate(new Date());

			// -------------------------------------------PREPARANDO CUERPO DEL MAIL
			Multipart multipart = new MimeMultipart();

			// Imagenes
//			String rutaImgElea = "..\\..\\..\\..\\..\\..\\..\\img\\elea-logo.png";
//			String rutaImgTechnolobiz = "..\\..\\..\\..\\..\\..\\..\\img\\TechnoloBiz-logo.png";

			// Url del directorio donde estan las imagenes
			String urlImagenes = "..\\\\..\\\\..\\\\..\\\\..\\\\..\\\\..\\\\img";
			File directorio = new File(urlImagenes);

			// Obtener los nombres de las imagenes en el directorio
			String[] imagenesDirectorio = directorio.list();

			// Now set the actual message
//			Image qrImage = qrService.generateQR(resultado);
			ByteArrayOutputStream baos = qrService.generateQR(resultado);
			Image qrImage = com.itextpdf.text.Image.getInstance(baos.toByteArray());

			// Nombre del pdf
			String timestamp = DateUtil.formatSdf("yyyyMMddHHmm", new Date());
			String fileName = resultado.getLegajo().getDni() + timestamp + ".pdf";

			pdfCreateFile.crearPDF(resultado, qrImage, fileName);
			String qrFileName = resultado.getLegajo().getDni() + QR_PNG;
//			File outputfile = new File(qrFileName);
			cuerpoMail = crearCuerpoMail(resultado, "");

			// Creo la parte del mensaje HTML
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setContent(cuerpoMail, "text/html");

			// Validar que el directorio si tenga las imagenes
			if (imagenesDirectorio != null) {
				for (int i = 0; i < imagenesDirectorio.length; i++) {

					MimeBodyPart imagePart = new MimeBodyPart();
					imagePart.setHeader("Content-ID", "<" + imagenesDirectorio[i] + ">");
					imagePart.setDisposition(MimeBodyPart.INLINE);
					imagePart.attachFile(urlImagenes + imagenesDirectorio[i]);
					multipart.addBodyPart(imagePart);
				}
			} else {
				System.out.println("No hay imagenes en el directorio");
			}

			// Agregar la parte del mensaje HTML al multiPart
			multipart.addBodyPart(mimeBodyPart);

			// PDF
			MimeBodyPart pdfPart = new MimeBodyPart();

//			String timestamp = DateUtil.formatSdf("yyyyMMddHHmm", new Date());
//			String fileName = resultado.getLegajo().getDni() + timestamp + ".pdf";

			DataSource source = new FileDataSource(fileName); // RUTA + NOMBRE DEL ARCHIVO A DESCARGAR
			pdfPart.setDataHandler(new DataHandler(source));
			pdfPart.setFileName(fileName); // NOMBRE CON EL CUÁL SE VA A
											// DESCARGAR

			pdfPart.setDisposition(MimeBodyPart.ATTACHMENT);
			multipart.addBodyPart(pdfPart);

			byte[] imageBytes = baos.toByteArray();
			String qrFile = resultado.getLegajo().getDni() + QR_PNG;
			ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
			BufferedImage bImage2 = ImageIO.read(bis);
			File outputFile = new File(qrFile);
			ImageIO.write(bImage2, "png", outputFile);

			String pathImg = new File(outputFile.toURI()).getAbsolutePath();

			DataSource fds = new FileDataSource(pathImg);

			MimeBodyPart messageBodyPartQR = new MimeBodyPart();
			messageBodyPartQR.setDataHandler(new DataHandler(fds));
			messageBodyPartQR.setHeader("Content-ID", "<image>");

			// add image to the multipart
			multipart.addBodyPart(messageBodyPartQR);

//			att.setDataHandler(new DataHandler(bds));
			// Agregar el multipart al cuerpo del mensaje
			message.setContent(multipart);

			System.out.println("sending...");

			// Send message
			Transport.send(message);
			System.out.println("Mail enviado a Usuario");

			// Enviando al Médico en caso No habilitado
			if (!resultado.isResultado()) {
				String[] toAddresses = to2.replace(" ", "").split(",");
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddresses[0]));

				if (toAddresses.length > 1) {
					InternetAddress[] cc = new InternetAddress[toAddresses.length - 1];
					for (int i = 1; i <= toAddresses.length - 1; i++) {
						cc[i - 1] = new InternetAddress(toAddresses[i]);
					}

					message.addRecipients(Message.RecipientType.CC, cc);
				}

				message.setContent(cuerpoMail, "text/html");

				System.out.println("sending...");

				// Send message
				Transport.send(message);
				System.out.println("Mail enviado a Consultorio Médico");
			}

			Path pathFilename = Paths.get(fileName);
			Files.delete(pathFilename);
			Path pathFilenameQR = Paths.get(pathImg);
			Files.delete(pathFilenameQR);
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}

	}

	// CUERPO MAIL
	public String crearCuerpoMail(Resultado resultado, String cidKeyQR) throws IOException {
		Legajo legajo = resultado.getLegajo();

		StringBuffer cuerpoMail = new StringBuffer();

		cuerpoMail.append(
				"<div style=\"color:black;font-family:Arial, 'Helvetica Neue', Helvetica, sans-serif;font-size:14px;background-color:#fff;margin:0 auto;padding:3em 0 3em;width:90%;\">");

		// Agregando estilos
//		cuerpoMail += stylesMail();

		// Agregando header
		cuerpoMail.append(headerMail());

		// Datos del usuario
		cuerpoMail.append("<p>Estimado/a,</p>"
				+ "<p style=\"margin-bottom:0;\">El resultado del autodiagnóstico realizado por<strong> "
				+ legajo.getNombre() + " " + legajo.getApellido() + "</strong> es:</p>");

		cuerpoMail.append("<ul style=\"margin-top:0.5em; font-size: 1em;\">");

		if (!legajo.getNroLegajo().equals("0")) {
			cuerpoMail.append(

					"<li aria-level=\"1\">\r\n" + "        <strong>Número de legajo:</strong> " + legajo.getNroLegajo()
							+ "\r\n" + "    </li>\r\n" + "    <li aria-level=\"1\">\r\n"
							+ "        <strong>Categoría:</strong> Empleado\r\n" + "    </li>");
		} else {
			cuerpoMail.append("<li aria-level=\"1\">\r\n" + "        <strong>Categoría:</strong> Visitante Externo\r\n"
					+ "    </li>\r\n" + "    <li aria-level=\"1\">\r\n" + "        <strong>Empresa:</strong> "
					+ legajo.getEmpresa() + "\r\n" + "    </li>");
		}

		cuerpoMail.append("<li aria-level=\"1\">\r\n" + "        <strong>DNI:</strong> " + legajo.getDni() + "\r\n"
				+ "    </li>\r\n" + "    <li aria-level=\"1\">\r\n" + "        <strong>Teléfono:</strong> "
				+ legajo.getTelefono() + "\r\n" + "    </li>\r\n" + "    <li aria-level=\"1\">\r\n"
				+ "        <strong>Correo electrónico:</strong> " + legajo.getEmailUsuario() + "\r\n" + "    </li>");

		String lugarAccesoStr = "";
		try {
			lugarAccesoStr = lugarAccesoService.recuperarLugarDeAcceso(legajo.getIdLugarAcceso())
					.getDescripcionLugarAcceso();
		} catch (Exception e) {
			e.printStackTrace();
		}
		cuerpoMail.append("<li aria-level=\"1\">\r\n" + "        <strong>Lugar de acceso:</strong> " + lugarAccesoStr
				+ "\r\n" + "    </li>");
		cuerpoMail.append("</u><br>");
		// fin datos del usuario

		// Datos del autodiagnostico
		cuerpoMail.append("<p style=\"margin: 2px 0 8px; font-size: 40px;\">");
		if (resultado.isResultado()) {
			cuerpoMail.append("<strong style=\"color: #28a745;\">Habilitado</strong>");
		} else {
			cuerpoMail.append("<strong style=\"color: #dc3545;\">No habilitado</strong>");
		}
		cuerpoMail.append("</p>" + "<ul style=\"list-style: none; margin:0 0 2em;\">");

		cuerpoMail.append("<li>" + (resultado.isEstadoSintomas() ? "Con" : "Sin") + " síntomas</li>");
		cuerpoMail.append("<li>" + (resultado.isEstadoContactoEstrecho() ? "Con" : "Sin") + " contacto estrecho</li>");
		cuerpoMail.append("<li>" + (resultado.isEstadoAntecedentes() ? "Con" : "Sin") + " antecedentes</li>");

		// TODO: Agregar vacunas en esta parte
		cuerpoMail.append(this.respuestasVacunacionMail(resultado));

		cuerpoMail.append("</ul>");

		String fechaAutodiagnostico = formatearFecha(resultado.getFecha_autodiagnostico());
		String fechaHastaResultado = formatearFecha(resultado.getFecha_hasta_resultado());

		cuerpoMail.append("<div style=\"font-size: 16px;\">");
		cuerpoMail.append(
				"<p style=\"margin-bottom: 0;\"><strong style=\"color: #3f51b5;\">Fecha de autodiagnóstico:</strong><br>"
						+ fechaAutodiagnostico + "</p>");
		cuerpoMail.append(
				"<p style=\"margin-bottom: 0;\"><strong style=\"color: #3f51b5;\">Fecha de vencimiento:</strong><br>"
						+ fechaHastaResultado + "</p>" + "</div>");
		// fin datos del autodiagnostico

		// Habilitado -> QR
		if (resultado.isResultado()) {

			cuerpoMail.append(
					"<br><p>Presente este código QR a quien corresponda para certificar que su resultado fue habilitado.</p>\r\n"
							+ "    <div style=\"text-align:center;\"><img src=\"cid:" + cidKeyQR + "\" "
							+ "alt=\"qr-resultado\" width=\"150\" height=\"150\"></div>");

		}

		// Respuestas
		cuerpoMail.append("<p>A continuación se presenta la declaración jurada realizada:</p>");

		cuerpoMail.append(tablaPreguntasRespuestasMail(resultado));
		// fin informe

		// Pie del mail
		cuerpoMail.append(footerMail());
		// fin del pie

		cuerpoMail.append("</div>");

//		System.out.println(cuerpoMail);

		return cuerpoMail.toString();
	}

	private static String headerMail() {
		return "<div style=\"text-align:center;\">\r\n"
				+ "        <img style=\"width:50%;max-width:215px;\" src=\"http://34.239.14.244/assets/elea-logo-v2@2x.png\" alt=\"elea-logo\">\r\n"
//				+ "        <img style=\"width:50%;max-width:215px;\" src=\"cid:elea-logo.png\" alt=\"elea-logo\">\r\n"
				+ "    </div>\r\n" + "    <br>";
	}

	// FOOTER
	private static String footerMail() {
		return "<br>\r\n" + "    <p>Este correo es automático, por favor no responder.</p>\r\n"
				+ "    <p style=\"margin-top: 0;\">Gracias.<br>\r\n" + "        Saludos.<br>\r\n"
				+ "        APP - Autodiagnóstico.<p>";
//				+ "    <div style=\"text-align:center;\">\r\n"
//				+ "        <div style=\"text-align:left;\"><img style=\"width:25%;max-width:175px;\" src=\"http://34.239.14.244/assets/elea-logo-v2@2x.png\" alt=\"elea-logo\"></div>\r\n"
//				+ "        <div style=\"text-align:left;\"><img style=\"width:30%;max-width:250px;\" src=\"http://34.239.14.244/assets/TechnoloBiz.png\" alt=\"technolobiz-logo\"></div>\r\n"
//				+ "    </div>";
	}

	// RESPUESTAS VACUNACION
	private String respuestasVacunacionMail(Resultado resultado) {
		Legajo legajo = resultado.getLegajo();

		List<String> preguntasRespuestas = null;

		try {
			preguntasRespuestas = preguntaService.recuperarPreguntasRespuestas(legajo.getIdAutodiagnostico(), false);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String html = "";

		for (String preguntaRespuesta : preguntasRespuestas) {
			String[] splitPregRta = preguntaRespuesta.split("@@");
			String pregunta = splitPregRta[0];
			String respuestaValor = splitPregRta[1];

			html += "<li>" + pregunta.replace(".", "") + ": " + respuestaValor + "</li>";
		}

		return html;
	}

	// TABLA PREGUNTAS Y RESPUESTAS
	private String tablaPreguntasRespuestasMail(Resultado resultado) {
		Legajo legajo = resultado.getLegajo();

		List<String> preguntasRespuestas = null;

		try {
			preguntasRespuestas = preguntaService.recuperarPreguntasRespuestas(legajo.getIdAutodiagnostico(), true);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String tabla = "<table style=\"border:1px solid black;border-collapse:collapse;background-color:#C5CAE9;margin:0 auto;width:90%;\">";

		for (String preguntaRespuesta : preguntasRespuestas) {
			String[] splitPregRta = preguntaRespuesta.split("@@");
			String pregunta = splitPregRta[0];
			String respuestaValor = splitPregRta[1];
			String respuesta = "";
			if (respuestaValor.equals("0")) {
				respuesta = "NO";
			} else if (respuestaValor.equals("1")) {
				respuesta = "SI";
			} else {
//				respuesta = respuestaValor + " °C";
				respuesta = respuestaValor;
			}

			tabla += "<tr>\r\n"
					+ "            <td style=\"border:1px solid black;border-collapse:collapse;padding:8px;\"><strong>"
					+ pregunta + "</strong></td>\r\n"
					+ "            <td style=\"border:1px solid black;border-collapse:collapse;padding:8px;\">"
					+ respuesta + "</td>\r\n" + "        </tr>";
		}

		tabla += "</table>";

		return tabla;
	}

	private static String formatearFecha(String fecha) {
		String[] fechaSpliteadaDiaHora = fecha.split("T");
		String[] diaSpliteado = fechaSpliteadaDiaHora[0].split("-");

		String fechaFormateada = diaSpliteado[2] + "/" + diaSpliteado[1] + "/" + diaSpliteado[0] + " "
				+ fechaSpliteadaDiaHora[1];

		return fechaFormateada;
	}
}