package com.ar.tbz.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
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
//		properties.put("mail.smtp.ssl.enable", "false");
//		properties.put("mail.smtp.auth", "false");
//		properties.put("mail.smtp.ssl.enable", "true");
//		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.ssl.enable", "false");
		properties.put("mail.smtp.auth", "false");

		// Get the Session object.// and pass username and password
		Session session = Session.getDefaultInstance(properties);
//		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
//
//			protected PasswordAuthentication getPasswordAuthentication() {
//
//				return new PasswordAuthentication(from, password);
//
//			}
//
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
//			cuerpoMail = crearCuerpoMailParaUsuario(resultado);
			cuerpoMail = crearCuerpoMail(resultado);
//			message.setContent(cuerpoMail, "text/html");

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
//			Document pdf = pdfCreateFile.buildPDFDocument(resultado);

			Image image = qrService.generateQR();

			pdfCreateFile.crearPDF(resultado, image);

//			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//			PdfWriter pdfWriter = PdfWriter.getInstance(pdf, byteArrayOutputStream); // Do this BEFORE document.open()

//			File file = new File("autodiagnostico.pdf");
//			OutputStream outputStream = new FileOutputStream(file);
//			byteArrayOutputStream.writeTo(outputStream);

//			pdf.open();
//			createPDF(pdf); // Whatever function that you use to create your PDF
//			pdf.close();

			// ByteArrayDataSource bds = new ByteArrayDataSource(pdf., "application/pdf");
			// pdfPart.setHeader("Content-ID", "<" + imagenesDirectorio[i] + ">");
//			pdfPart.setContent(mp);
//			pdfPart.attachFile(file);

			DataSource source = new FileDataSource("autodiagnostico.pdf"); // RUTA + NOMBRE DEL ARCHIVO A DESCARGAR
			pdfPart.setDataHandler(new DataHandler(source));
			String timestamp = DateUtil.formatSdf("yyyyMMddHHmm", new Date());
			pdfPart.setFileName(resultado.getLegajo().getDni() + timestamp + ".pdf"); // NOMBRE CON EL CUÁL SE VA A
																						// DESCARGAR

			pdfPart.setDisposition(MimeBodyPart.ATTACHMENT);
//			File file = File.createTempFile("autodiagnostico", "pdf");
			multipart.addBodyPart(pdfPart);

//			att.setDataHandler(new DataHandler(bds));
			// Agregar el multipart al cuerpo del mensaje
			message.setContent(multipart);

			System.out.println("sending...");

			// Send message
			Transport.send(message);
			System.out.println("Mail enviado a Usuario");

			// Enviando al Médico en caso No habilitado
			if (!resultado.isResultado()) {
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(to2));
//				cuerpoMail = crearCuerpoMailParaMedico(resultado);
				message.setContent(cuerpoMail, "text/html");

				System.out.println("sending...");

				// Send message
				Transport.send(message);
				System.out.println("Mail enviado a Consultorio Médico");
			}
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}

	}

	// CUERPO MAIL
	public String crearCuerpoMail(Resultado resultado) {
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
		cuerpoMail.append("</ul>" + "<div style=\"font-size: 16px;\">");

		String fechaAutodiagnostico = formatearFecha(resultado.getFecha_autodiagnostico());
		String fechaHastaResultado = formatearFecha(resultado.getFecha_hasta_resultado());

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
							+ "    <div style=\"text-align:center;\"><img src=\"http://34.239.14.244/assets/qr-code.png\" alt=\"qr-resultado\" width=\"150\" height=\"150\"></div>");
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

	// MAIL PARA USUARIO
//	public static String crearCuerpoMailParaUsuario(Resultado resultado) {
//		Legajo legajo = resultado.getLegajo();
//
//		StringBuffer cuerpoMail = new StringBuffer();
//
//		cuerpoMail.append(
//				"<div style=\"color:black;font-family:Arial, 'Helvetica Neue', Helvetica, sans-serif;font-size:14px;background-color:#fff;margin:0 auto;padding:3em 0 3em;width:90%;\">");
//
//		// Agregando header
//		cuerpoMail.append(headerMail());
//
//		// Datos del usuario
//		cuerpoMail.append("<h3>Estimado " + legajo.getNombre() + " " + legajo.getApellido() + "</h3>");
//		// fin datos del usuario
//
//		// Informar si tiene sintomas y/o contacto estrecho
//		String resultadoLeyenda = "";
//		String estadoSintomasLeyenda = "";
//		String estadoContactoEstrechoLeyenda = "";
//		String estadoAntecedentesLeyenda = "";
//
//		if (resultado.isResultado()) {
//			resultadoLeyenda = "<strong>Habilitado</strong>";
//		} else {
//			resultadoLeyenda = "<strong>No Habilitado</strong>";
//		}
//
//		if (resultado.isEstadoSintomas()) {
//			estadoSintomasLeyenda = "padece síntomas";
//		} else {
//			estadoSintomasLeyenda = "no padece síntomas";
//		}
//
//		if (resultado.isEstadoContactoEstrecho()) {
//			estadoContactoEstrechoLeyenda = "tuvo contacto estrecho";
//		} else {
//			estadoContactoEstrechoLeyenda = "no tuvo contacto estrecho";
//		}
//
//		if (resultado.isEstadoAntecedentes()) {
//			estadoAntecedentesLeyenda = "con antecedentes médicos";
//		} else {
//			estadoAntecedentesLeyenda = "sin antecedentes médicos";
//		}
//
//		cuerpoMail.append("<p style=\"margin-bottom: 0;\">Su resultado ha sido " + resultadoLeyenda
//				+ " debido a que usted " + estadoSintomasLeyenda + " y " + estadoContactoEstrechoLeyenda + ".</p>");
//		cuerpoMail.append("<p>A modo informativo, usted es una persona " + estadoAntecedentesLeyenda
//				+ " aunque lo previamente mencionado no impacta de ningún modo en el resultado.</p>");
//
//		cuerpoMail.append("<p>Esta es la declaración jurada que usted presentó:</p>");
//
//		cuerpoMail.append(tablaPreguntasRespuestasMail(resultado));
//		// fin informe
//
//		cuerpoMail.append("<br>");
//
//		// Habilitado: QR - No habilitado: Contacto médico
//		if (resultado.isResultado()) {
//			cuerpoMail.append(
//					"<p>Presente este código QR a quien corresponda para certificar que su resultado fue habilitado.</p>\r\n"
//							+ "    <div style=\"text-align:center;\"><img src=\"http://34.239.14.244/assets/qr-code.png\" alt=\"qr-resultado\" width=\"150\" height=\"150\"></div>");
//		} else {
//			cuerpoMail.append(
//					"<p>Usted no está habilitado para concurrir al trabajo. Por favor, comuníquese con Servicio Médico por teléfono o Whatsapp al 011-3867-3669.</p>");
//		}
//
//		// Pie del mail
//		cuerpoMail.append(footerMail());
//		// fin del pie
//
//		cuerpoMail.append("</div>");
//
////		System.out.println(cuerpoMail);
//
//		return cuerpoMail.toString();
//	}

	// MAIL PARA MEDICO
//	public static String crearCuerpoMailParaMedico(Resultado resultado) {
//		Legajo legajo = resultado.getLegajo();
//
//		StringBuffer cuerpoMail = new StringBuffer();
//
//		cuerpoMail.append(
//				"<div style=\"color:black;font-family:Arial, 'Helvetica Neue', Helvetica, sans-serif;font-size:14px;background-color:#fff;margin:0 auto;padding:3em 0 3em;width:90%;\">");
//
//		// Agregando estilos
////		cuerpoMail += stylesMail();
//
//		// Agregando header
//		cuerpoMail.append(headerMail());
//
//		// Comienzo mail
//		cuerpoMail.append("<h3>Estimados,</h3>");
//		// fin comienzo
//
//		// Datos del usuario
//		cuerpoMail.append("<p style=\"margin-bottom:0;\">El usuario " + legajo.getNombre() + " " + legajo.getApellido()
//				+ " cuya información es:</p>");
//
//		cuerpoMail.append("<ul style=\"margin-top: .5em;\">");
//
//		if (!legajo.getNroLegajo().equals("0")) {
//			cuerpoMail.append(
//
//					"<li aria-level=\"1\">\r\n" + "        <strong>Número de legajo:</strong> " + legajo.getNroLegajo()
//							+ "\r\n" + "    </li>\r\n" + "    <li aria-level=\"1\">\r\n"
//							+ "        <strong>Categoría:</strong> Empleado\r\n" + "    </li>");
//		} else {
//			cuerpoMail.append("<li aria-level=\"1\">\r\n" + "        <strong>Categoría:</strong> Visitante Externo\r\n"
//					+ "    </li>\r\n" + "    <li aria-level=\"1\">\r\n" + "        <strong>Empresa:</strong> "
//					+ legajo.getEmpresa() + "\r\n" + "    </li>");
//		}
//
//		cuerpoMail.append("<li aria-level=\"1\">\r\n" + "        <strong>DNI:</strong> " + legajo.getDni() + "\r\n"
//				+ "    </li>\r\n" + "    <li aria-level=\"1\">\r\n" + "        <strong>Teléfono:</strong> "
//				+ legajo.getTelefono() + "\r\n" + "    </li>\r\n" + "    <li aria-level=\"1\">\r\n"
//				+ "        <strong>Correo electrónico:</strong> " + legajo.getEmailUsuario() + "\r\n" + "    </li>");
//
//		String lugarAccesoStr = "";
//		try {
//			lugarAccesoStr = Servicios.recuperarLugarDeAcceso(legajo.getIdLugarAcceso());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		cuerpoMail.append("<li aria-level=\"1\">\r\n" + "        <strong>Lugar de acceso:</strong> " + lugarAccesoStr
//				+ "\r\n" + "    </li>");
//
//		String fechaAutodiagnostico = formatearFecha(resultado.getFecha_autodiagnostico());
//		String fechaHastaResultado = formatearFecha(resultado.getFecha_hasta_resultado());
//
//		cuerpoMail.append(
//				"<li aria-level=\"1\" style=\"color:black;font-family:Arial, 'Helvetica Neue', Helvetica, sans-serif;font-size:24px>\r\n"
//						+ "        <strong>Fecha de autodiagnóstico:</strong> " + fechaAutodiagnostico + "\r\n"
//						+ "    </li>\r\n"
//						+ "    <li aria-level=\"1\" style=\"color:black;font-family:Arial, 'Helvetica Neue', Helvetica, sans-serif;font-size:24px>\r\n"
//						+ "        <strong>Fecha hasta de no habilitación:</strong> " + fechaHastaResultado + "\r\n"
//						+ "    </li>\r\n" + "</ul>");
//		// fin datos del usuario
//
//		// Informar si tiene sintomas y/o contacto estrecho
//		cuerpoMail.append("<p style=\"margin-bottom: 2em;\">");
//
//		if (resultado.isEstadoSintomas() && resultado.isEstadoContactoEstrecho()) {
//			cuerpoMail.append("Padece de síntomas y tuvo contacto estrecho");
//		} else if (resultado.isEstadoSintomas()) {
//			cuerpoMail.append("Padece de síntomas");
//		} else {
//			cuerpoMail.append("Tuvo contacto estrecho");
//		}
//		cuerpoMail.append(" por lo tanto su resultado es <strong>No Habilitado</strong>.</p>");
//
//		cuerpoMail.append(
//				"<p>A continuación se presenta la declaración jurada que ingresó el usuario. Por supuesto esto se encuentra correctamente almacenado en la base de datos. Los datos son:</p>");
//
//		cuerpoMail.append(tablaPreguntasRespuestasMail(resultado));
//		// fin informe
//
//		// Pie del mail
//		cuerpoMail.append(footerMail());
//		// fin del pie
//
//		cuerpoMail.append("</div>");
//
////		System.out.println(cuerpoMail);
//
//		return cuerpoMail.toString();
//	}

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

	// TABLA PREGUNTAS Y RESPUESTAS
	private String tablaPreguntasRespuestasMail(Resultado resultado) {
		Legajo legajo = resultado.getLegajo();

		List<String> preguntasRespuestas = null;

		try {
			preguntasRespuestas = preguntaService.recuperarPreguntas(legajo.getIdAutodiagnostico());
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
