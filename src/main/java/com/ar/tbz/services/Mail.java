package com.ar.tbz.services;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.ar.tbz.domain.Legajo;
import com.ar.tbz.domain.Resultado;

public class Mail {

	// ENVIO MAIL
	public static void envioMail(Resultado resultado) throws IOException {
		System.out.println("Enviando mail");

		// Sender's email ID needs to be mentioned
		String from = "test.technolobiz@gmail.com";
		String password = "test.0321";

		// Recipient's email ID needs to be mentioned.
		String to = resultado.getLegajo().getEmailUsuario();
		String to2 = "consultorio.medico@elea.com";

		// Assuming you are sending email from through gmails smtp
		String host = "smtp.gmail.com";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", "465");
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.auth", "true");

		// Get the Session object.// and pass username and password
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {

				return new PasswordAuthentication(from, password);

			}

		});

		// Used to debug SMTP issues
		session.setDebug(true);

		String cuerpoMail = "";
		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			String resultadoLeyenda = "";
			if (resultado.isResultado()) {
				resultadoLeyenda = "Habilitado";
			} else {
				resultadoLeyenda = "No habilitado";
			}
			message.setSubject(resultado.getLegajo().getNombre() + " " + resultado.getLegajo().getApellido() + " - Resultado de Autodiagn??stico: " + resultadoLeyenda);
			
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
			cuerpoMail = crearCuerpoMailParaUsuario(resultado);
//			message.setContent(cuerpoMail, "text/html");
			
			// Creo la parte del mensaje HTML
			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setContent(cuerpoMail, "text/html");
			
			// Validar que el directorio si tenga las imagenes
			if (imagenesDirectorio != null) {
			    for (int i = 0; i < imagenesDirectorio.length; i++) {
			 
			    	MimeBodyPart imagePart = new MimeBodyPart();
			    	imagePart.setHeader("Content-ID", "<"
			    						+ imagenesDirectorio[i] + ">");
			    	imagePart.setDisposition(MimeBodyPart.INLINE);
			    	imagePart.attachFile(urlImagenes
			    				+ imagenesDirectorio[i]);
			    	multipart.addBodyPart(imagePart);
			    }
			} else {
				System.out.println("No hay imagenes en el directorio");
			}
			
			// Agregar la parte del mensaje HTML al multiPart
			multipart.addBodyPart(mimeBodyPart);
			
			// Agregar el multipart al cuerpo del mensaje
			message.setContent(multipart);

			System.out.println("sending...");

			// Send message
			Transport.send(message);
			System.out.println("Mail enviado a Usuario");
			
			// Enviando al M??dico en caso No habilitado
			if (!resultado.isResultado()) {
				message.setRecipient(Message.RecipientType.TO, new InternetAddress(to2));
				cuerpoMail = crearCuerpoMailParaMedico(resultado);
				message.setContent(cuerpoMail, "text/html");

				System.out.println("sending...");

				// Send message
				Transport.send(message);
				System.out.println("Mail enviado a Consultorio M??dico");
			}
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}

	}

	// MAIL PARA USUARIO
	public static String crearCuerpoMailParaUsuario(Resultado resultado) {
		Legajo legajo = resultado.getLegajo();
		
		StringBuffer cuerpoMail = new StringBuffer();
		
		cuerpoMail.append("<div style=\"color:black;font-family:Arial, 'Helvetica Neue', Helvetica, sans-serif;font-size:14px;background-color:#fff;margin:0 auto;padding:3em 0 3em;width:90%;\">");

		// Agregando header
		cuerpoMail.append(headerMail());

		// Datos del usuario
		cuerpoMail.append("<h3>Estimado " + legajo.getNombre() + " " + legajo.getApellido() + "</h3>");
		// fin datos del usuario

		// Informar si tiene sintomas y/o contacto estrecho
		String resultadoLeyenda = "";
		String estadoSintomasLeyenda = "";
		String estadoContactoEstrechoLeyenda = "";
		String estadoAntecedentesLeyenda = "";
		
		if (resultado.isResultado()) {
			resultadoLeyenda = "<strong>Habilitado</strong>";
		} else {
			resultadoLeyenda = "<strong>No Habilitado</strong>";
		}
		
		if (resultado.isEstadoSintomas()) {
			estadoSintomasLeyenda = "padece s??ntomas";
		} else {
			estadoSintomasLeyenda = "no padece s??ntomas";
		}

		if (resultado.isEstadoContactoEstrecho()) {
			estadoContactoEstrechoLeyenda = "tuvo contacto estrecho";
		} else {
			estadoContactoEstrechoLeyenda = "no tuvo contacto estrecho";
		}

		if (resultado.isEstadoAntecedentes()) {
			estadoAntecedentesLeyenda = "con antecedentes m??dicos";
		} else {
			estadoAntecedentesLeyenda = "sin antecedentes m??dicos";
		}
		
		cuerpoMail.append("<p style=\"margin-bottom: 0;\">Su resultado ha sido " + resultadoLeyenda + 
					  " debido a que usted " + estadoSintomasLeyenda +  " y " + estadoContactoEstrechoLeyenda + ".</p>");
		cuerpoMail.append("<p>A modo informativo, usted es una persona " + estadoAntecedentesLeyenda + " aunque lo previamente mencionado no impacta de ning??n modo en el resultado.</p>");

		cuerpoMail.append("<p>Esta es la declaraci??n jurada que usted present??:</p>");

		cuerpoMail.append(tablaPreguntasRespuestasMail(resultado));
		// fin informe
		
		cuerpoMail.append("<br>");
		
		// Habilitado: QR	-	No habilitado: Contacto m??dico
		if (resultado.isResultado()) {
			cuerpoMail.append("<p>Presente este c??digo QR a quien corresponda para certificar que su resultado fue habilitado.</p>\r\n" + 
					"    <div style=\"text-align:center;\"><img src=\"http://34.239.14.244/assets/qr-code.png\" alt=\"qr-resultado\" width=\"150\" height=\"150\"></div>");
		} else {
			cuerpoMail.append("<p>Usted no est?? habilitado para concurrir al trabajo. Por favor, comun??quese con Servicio M??dico por tel??fono o Whatsapp al 011-3867-3669.</p>");
		}

		// Pie del mail
		cuerpoMail.append(footerMail());
		// fin del pie

		cuerpoMail.append("</div>");

//		System.out.println(cuerpoMail);

		return cuerpoMail.toString();
	}

	// MAIL PARA MEDICO
	public static String crearCuerpoMailParaMedico(Resultado resultado) {
		Legajo legajo = resultado.getLegajo();
		
		StringBuffer cuerpoMail = new StringBuffer();

		cuerpoMail.append("<div style=\"color:black;font-family:Arial, 'Helvetica Neue', Helvetica, sans-serif;font-size:14px;background-color:#fff;margin:0 auto;padding:3em 0 3em;width:90%;\">");

		// Agregando estilos
//		cuerpoMail += stylesMail();

		// Agregando header
		cuerpoMail.append(headerMail());

		// Comienzo mail
		cuerpoMail.append("<h3>Estimados,</h3>");
		// fin comienzo

		// Datos del usuario
		cuerpoMail.append("<p style=\"margin-bottom:0;\">El usuario " + legajo.getNombre() + " " + legajo.getApellido()
				+ " cuya informaci??n es:</p>");

		cuerpoMail.append("<ul style=\"margin-top: .5em;\">");
		
		if (!legajo.getNroLegajo().equals("0")) {
			cuerpoMail.append("<li aria-level=\"1\">\r\n" +
					"        <strong>N??mero de legajo:</strong> "+ legajo.getNroLegajo() + "\r\n" 
					+ "    </li>\r\n" 
					+ "    <li aria-level=\"1\">\r\n"
					+ "        <strong>Categor??a:</strong> Empleado\r\n" 
					+ "    </li>");
		} else {
			cuerpoMail.append("<li aria-level=\"1\">\r\n" 
					+ "        <strong>Categor??a:</strong> Visitante Externo\r\n"
					+ "    </li>\r\n" 
					+ "    <li aria-level=\"1\">\r\n" 
					+ "        <strong>Empresa:</strong> " + legajo.getEmpresa() + "\r\n" 
					+ "    </li>");
		}

		cuerpoMail.append("<li aria-level=\"1\">\r\n" 
				+ "        <strong>DNI:</strong> " + legajo.getDni() + "\r\n"
				+ "    </li>\r\n" 
				+ "    <li aria-level=\"1\">\r\n" 
				+ "        <strong>Tel??fono:</strong> " + legajo.getTelefono() + "\r\n" 
				+ "    </li>\r\n" + "    <li aria-level=\"1\">\r\n"
				+ "        <strong>Correo electr??nico:</strong> " + legajo.getEmailUsuario() + "\r\n" 
				+ "    </li>");

		String lugarAccesoStr = "";
		try {
			lugarAccesoStr = Servicios.recuperarLugarDeAcceso(legajo.getIdLugarAcceso());
		} catch (Exception e) {
			e.printStackTrace();
		}
		cuerpoMail.append("<li aria-level=\"1\">\r\n" 
				+ "        <strong>Lugar de acceso:</strong> " + lugarAccesoStr + "\r\n" 
				+ "    </li>");

		String fechaAutodiagnostico = formatearFecha(resultado.getFecha_autodiagnostico());
		String fechaHastaResultado = formatearFecha(resultado.getFecha_hasta_resultado());
		
		cuerpoMail.append("<li aria-level=\"1\">\r\n" 
				+ "        <strong>Fecha de autodiagn??stico:</strong> " + fechaAutodiagnostico + "\r\n" 
				+ "    </li>\r\n" 
				+ "    <li aria-level=\"1\">\r\n"
				+ "        <strong>Fecha hasta de no habilitaci??n:</strong> " + fechaHastaResultado + "\r\n" 
				+ "    </li>\r\n" 
				+ "</ul>");
		// fin datos del usuario

		// Informar si tiene sintomas y/o contacto estrecho
		cuerpoMail.append("<p style=\"margin-bottom: 2em;\">");

		if (resultado.isEstadoSintomas() && resultado.isEstadoContactoEstrecho()) {
			cuerpoMail.append("Padece de s??ntomas y tuvo contacto estrecho");
		} else if (resultado.isEstadoSintomas()) {
			cuerpoMail.append("Padece de s??ntomas");
		} else {
			cuerpoMail.append("Tuvo contacto estrecho");
		}
		cuerpoMail.append(" por lo tanto su resultado es <strong>No Habilitado</strong>.</p>");

		cuerpoMail.append("<p>A continuaci??n se presenta la declaraci??n jurada que ingres?? el usuario. Por supuesto esto se encuentra correctamente almacenado en la base de datos. Los datos son:</p>");

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
		return "<br>\r\n" + "    <p>Este correo es autom??tico, por favor no responder.</p>\r\n"
				+ "    <p style=\"margin-top: 0;\">Gracias.<br>\r\n" 
				+ "        Saludos.<br>\r\n" 
				+ "        APP - Autodiagn??stico.<p>"
				+ "    <div style=\"text-align:center;\">\r\n"
				+ "        <div style=\"text-align:left;\"><img style=\"width:25%;max-width:175px;\" src=\"http://34.239.14.244/assets/elea-logo-v2@2x.png\" alt=\"elea-logo\"></div>\r\n"
				+ "        <div style=\"text-align:left;\"><img style=\"width:30%;max-width:250px;\" src=\"http://34.239.14.244/assets/TechnoloBiz.png\" alt=\"technolobiz-logo\"></div>\r\n"
				+ "    </div>";
	}

	// TABLA PREGUNTAS Y RESPUESTAS
	private static String tablaPreguntasRespuestasMail(Resultado resultado) {
		Legajo legajo = resultado.getLegajo();

		List<String> preguntasRespuestas = null;

		try {
			preguntasRespuestas = Servicios.recuperarPreguntas(legajo.getIdAutodiagnostico());
		} catch (Exception e) {
			e.printStackTrace();
		}

		String tabla = "<table style=\"border:1px solid black;border-collapse:collapse;background-color:#C5CAE9;margin:0 auto;width:90%;\">";

		for (String preguntaRespuesta : preguntasRespuestas) {
			String[] splitPregRta = preguntaRespuesta.split(",");
			String pregunta = splitPregRta[0];
			String respuestaValor = splitPregRta[1];
			String respuesta = "";
			if (respuestaValor.equals("0")) {
				respuesta = "NO";
			} else if (respuestaValor.equals("1")) {
				respuesta = "SI";
			} else {
				respuesta = respuestaValor + " ??C";
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
		
		String fechaFormateada = diaSpliteado[2] + "/" + diaSpliteado[1] + "/" + diaSpliteado[0] + " " + fechaSpliteadaDiaHora[1];		
		
		return fechaFormateada;
	}
}
