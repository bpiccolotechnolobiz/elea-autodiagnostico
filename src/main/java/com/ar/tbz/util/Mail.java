package com.ar.tbz.util;

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
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ar.tbz.conexion.Conexion;
import com.ar.tbz.domain.Legajo;
import com.ar.tbz.domain.Parametro;
import com.ar.tbz.domain.Resultado;
import com.ar.tbz.services.EstadisticaService;
import com.ar.tbz.services.LugarAccesoService;
import com.ar.tbz.services.PreguntaService;

@Component
public class Mail {

//	private static final String LOGO_ELEA_PNG = "elea-logo.png";
	@Value("${logo.elea.file}")
	private String logoElea;
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

	@Value("${event.file.employee}")
	String eventFileEmpl;
	@Value("${event.file.external}")
	String eventFileExt;
	@Value("${event.file.all}")
	String eventFileAll;
	private static final String CID_IMAGE_EVENT = "imageEvent";
	private static final String CID_IMAGE_QR = "QR";
	private static final String CID_IMAGE_LOGO_ELEA = "logoElea";

	public void envioMail2(Resultado resultado) throws Exception {

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
		
		// Config Elea
		properties.put("mail.smtp.ssl.enable", "false");
		properties.put("mail.smtp.auth", "false");
		// Config TBIZ
//		properties.put("mail.smtp.ssl.enable", "true");
//		properties.put("mail.smtp.auth", "true");

		// Get the Session object.// and pass username and password
		// Config Elea
		Session session = Session.getDefaultInstance(properties);
		// Config TBIZ
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
		
		// Imagen evento
		boolean existeImgEventoEmpleados = false;
		boolean existeImgEventoExternos = false;
		File imageEvent = new File(eventFileAll);
		if(imageEvent.exists()) {
			existeImgEventoEmpleados = true;
			existeImgEventoExternos = true;
		}
		imageEvent = new File(eventFileEmpl);
		if(imageEvent.exists()) {
			existeImgEventoEmpleados = true;
		}
		imageEvent = new File(eventFileExt);
		if(imageEvent.exists()) {
			existeImgEventoExternos = true;
		}
		
		// CID imgs
		Map<String, String> inlineImages = new HashMap<String, String>();
		inlineImages.put(CID_IMAGE_LOGO_ELEA, logoElea);
		if (resultado.isResultado()) {
			inlineImages.put(CID_IMAGE_QR, fileNameQR);
			if(existeImgEventoEmpleados && existeImgEventoExternos) {
				inlineImages.put(CID_IMAGE_EVENT, eventFileAll);
			} else if(existeImgEventoEmpleados && !resultado.getLegajo().getNroLegajo().equals("0")) {
				inlineImages.put(CID_IMAGE_EVENT, eventFileEmpl);
			} else if(existeImgEventoExternos && resultado.getLegajo().getNroLegajo().equals("0")) {
				inlineImages.put(CID_IMAGE_EVENT, eventFileExt);
			}
		} // se verifica tambien el nro de legajo para que no adjunte la imagen en el mail por mas q no
		  // la agregue despues al cuerpo del mismo

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
			cuerpoMail = crearCuerpoMail(resultado, existeImgEventoEmpleados, existeImgEventoExternos);

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

	// CUERPO MAIL
	public String crearCuerpoMail(Resultado resultado, boolean existeImgEventoEmpleados, boolean existeImgEventoExternos) throws IOException {
		Legajo legajo = resultado.getLegajo();

		StringBuffer cuerpoMail = new StringBuffer();

		cuerpoMail.append(
				"<div style=\"color:black;font-family:Arial, 'Helvetica Neue', Helvetica, sans-serif;font-size:14px;background-color:#fff;margin:0 auto;padding:3em 0 3em;width:90%;\">");

		cuerpoMail.append(headerMail());

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


		// Img evento
		if (resultado.isResultado() && 
				((existeImgEventoEmpleados && !legajo.getNroLegajo().equals("0"))
				|| (existeImgEventoExternos && legajo.getNroLegajo().equals("0")))) {
			cuerpoMail.append("<div style=\"text-align:center;\"><img src=\"cid:" + CID_IMAGE_EVENT + "\" alt=\"imagen-evento\" width=\"150\" height=\"150\"></div>");
		}
		
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
							+ "    <div style=\"text-align:center;\"><img src=\"cid:" + CID_IMAGE_QR + "\" "
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
				+ "        <img style=\"width:50%;max-width:215px;\" src=\"cid:" + CID_IMAGE_LOGO_ELEA + "\" alt=\"elea-logo\">\r\n"
				+ "    </div>\r\n" + "    <br>";
	}

	// FOOTER
	private static String footerMail() {
		return "<br>\r\n" + "    <p>Este correo es automático, por favor no responder.</p>\r\n"
				+ "    <p style=\"margin-top: 0;\">Gracias.<br>\r\n" + "        Saludos.<br>\r\n"
				+ "        APP - Autodiagnóstico.<p>";
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