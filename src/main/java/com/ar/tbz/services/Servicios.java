package com.ar.tbz.services;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ar.tbz.conexion.Conexion;
import com.ar.tbz.domain.Legajo;
import com.ar.tbz.domain.Resultado;
import com.ar.tbz.util.DateUtil;
import com.ar.tbz.util.Mail;
import com.ar.tbz.util.PdfCreateFile;
import com.ar.tbz.util.QRService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Service
public class Servicios {

	@Autowired
	Mail mail;

	@Autowired
	PdfCreateFile pdfBuilder;
	@Autowired
	EmpleadoService empleadoService;
	@Autowired
	QRService qrService;

	private static Log log = LogFactory.getLog(Servicios.class);

	// grabar archivo
	public void grabarArchivo(String nombreFile, String archivo) {
		System.out.println("Texto del Mail \n\n\n " + archivo);
		try {
			String ruta = nombreFile;

			File file = new File(ruta);

			// Si el archivo no existe es creado
			if (file.exists()) {
				file.delete();
			}
			// Si el archivo no existe es creado
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(archivo);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// crear datos mail ------------------------------------
	public void crearMail(Resultado resultado) throws Exception {
//		mail.envioMail(resultado);
		mail.envioMail2(resultado);
//		Servicios.grabarArchivo("c://tmp//mail", textoMail);
	}

	public void crearQRCode(Resultado resultado) throws Exception {

		// se usa para no recuperar de la tabla (false)
		boolean recuperarNroLegajoDeTabla = true;
		// boolean recuperarNroLegajoDeTabla = false ;

		// generamos el nombre del archivo

		Legajo legajo = resultado.getLegajo();

		String nroLegajo = "";
		if (legajo != null) {
			nroLegajo = resultado.getLegajo().getNroLegajo();
		} // end if

		String dni = "";
		int nroLegajoRecuperaDNI = 0;
		// usado para test de funcionamiento
		// recupera datos de la tabla real
		if (recuperarNroLegajoDeTabla) {
			dni = "34567891";
			legajo = empleadoService.findByLegajo("" + nroLegajoRecuperaDNI);

		} // end if recupertar

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
		} // end if nroLegajo

		archivoNombre = archivoNombre.replace(":", "-");

		final int qrTamAncho = 400;
		final int qrTamAlto = 400;
		final String formato = "png";

		// final String ruta = "c://Users//JulioMOliveira//tmp//miCodigoQR3.png";

		archivoNombre = "qrgenerado";
		final String ruta = "c://tmp//qr//" + archivoNombre + ".png";
		// final String ruta = "c://Users//elea//tmp//qr//"+archivoNombre+ ".pdf";

		// cambiar valores para ejecutar desde la app front end

		// boolean testConsole = true;
		boolean testConsole = false;

		boolean reucuperarLegajo = true;
		// boolean reucuperarLegajo = false ;

		StringBuilder sb = new StringBuilder();
		String data = "";

		if (!testConsole) {
			if (!reucuperarLegajo) {
				sb.append("\nELEA");
				sb.append("\n");
				sb.append("\nAutodiagnostico COVID-19");
				sb.append("\n");
				sb.append("\nEmpleado:	   		 	 Juan Perez");
				sb.append("\n");
				sb.append("\nNro. Legajo:	 	 	 12345678");
				sb.append("\n");
				sb.append("\nDNI:  					 15.345.678");
				sb.append("\n");
				sb.append("\nFecha realizacion: 	 05-07-2021 08:34:12");
				sb.append("\n");
				sb.append("\nFecha autorizado hasta: 06-07-2021 08:34:1");
				sb.append("\n");
				sb.append("\nResultado final:		 AUTORIZADO");
			} else {
				// recuperando datos de la tabla empleadosActivos
				legajo = empleadoService.findByLegajo("34567891");
				sb.append("\nELEA");
				sb.append("\n");
				sb.append("\nAutodiagnostico COVID-19");
				sb.append("\n");
				sb.append("\nEmpleado:	   		 	 " + legajo.getNombre());
				sb.append("\n");
				sb.append("\nNro. Legajo:	 	 	 " + legajo.getNroLegajo());
				sb.append("\n");
				sb.append("\nDNI:  					 " + legajo.getDni());
				sb.append("\n");
				sb.append("\nFecha realizacion: 	 05-07-2021 08:34:12");
				sb.append("\n");
				sb.append("\nFecha autorizado hasta: 06-07-2021 08:34:1");
				sb.append("\n");
				sb.append("\nResultado final:		 AUTORIZADO");
			}

			data = sb.toString();

		} else {
			System.out.println("Generacion codigo QR - Introduce el texto a codificar ");
			System.out.println("\n Ingresar * para finalizar y generar el codigo:");
			while (true) {
				Scanner sc = new Scanner(System.in);
				String lineaIngresada = sc.nextLine();
				if (lineaIngresada.equals("*")) {
					data = sb.toString();
					break;
				}
				sb.append(lineaIngresada);
			}
		} // end if testConsole

		System.out.println("Codificando...");
		BitMatrix matriz;
		Writer writer = new QRCodeWriter();
		try {
			matriz = writer.encode(data, BarcodeFormat.QR_CODE, qrTamAncho, qrTamAlto);
		} catch (WriterException e) {
			e.printStackTrace(System.err);
			return;
		}
		BufferedImage imagen = new BufferedImage(qrTamAncho, qrTamAlto, BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < qrTamAlto; y++) {
			for (int x = 0; x < qrTamAncho; x++) {
				int valor = (matriz.get(x, y) ? 0 : 1) & 0xff;
				imagen.setRGB(x, y, (valor == 0 ? 0 : 0xFFFFFF));
			}
		}
		FileOutputStream qrCode = new FileOutputStream(ruta);
		ImageIO.write(imagen, formato, qrCode);
		System.out.println("Listo!");
		qrCode.close();

	}

	/**
	 * Actualiza la base de datosde AutoDiagnostico con los datos enviados por el
	 * Front End Utiliza para esto el nuevo id enviado en el paso inicial
	 * 
	 * @param datos
	 * @throws Exception
	 */
	public synchronized static int grabarAutoDiagnostico(Resultado resultado) throws Exception {

		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy hh-mm-ss");
		boolean status = false;
		int idAutodiagnostico = 0;
		try {

			conn = Conexion.generarConexion();

			// ELEA_AUTODIAGNOSTICO.dbo.empleadosActivos

			String query = "INSERT INTO ELEA_AUTODIAGNOSTICO.dbo.autodiagnostico "

					+ "(nroLegajo, dni, nombre, apellido, telefono, empresa, emailLaboral, emailUsuario, idLugarAcceso, estadoSintomas, estadoContactoEstrecho, estadoAntecedentes, resultado, "

					+ "fecha_autodiagnostico, fecha_hasta_resultado, comentario, modificadoPor, modificadoEn)"

					+ "values( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?, ? ) "; // son 17

			pstm = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

			pstm.setObject(1, resultado.getLegajo().getNroLegajo());
			pstm.setObject(2, resultado.getLegajo().getDni());
			pstm.setObject(3, resultado.getLegajo().getNombre());
			pstm.setObject(4, resultado.getLegajo().getApellido());
			pstm.setObject(5, resultado.getLegajo().getTelefono());
			pstm.setObject(6, resultado.getLegajo().getEmpresa());
			pstm.setObject(7, resultado.getLegajo().getEmailLaboral());
			pstm.setObject(8, resultado.getLegajo().getEmailUsuario());

			pstm.setObject(9, resultado.getLegajo().getIdLugarAcceso());

			pstm.setObject(10, resultado.isEstadoSintomas());
			pstm.setObject(11, resultado.isEstadoContactoEstrecho());
			pstm.setObject(12, resultado.isEstadoAntecedentes());
			pstm.setObject(13, resultado.isResultado());

			pstm.setString(14, resultado.getFecha_autodiagnostico());
			pstm.setString(15, resultado.getFecha_hasta_resultado());

			pstm.setObject(16, resultado.getComentario());
			pstm.setObject(17, resultado.getModificadoPor());
			pstm.setObject(18, resultado.getModificadoEn());

			System.out.println("query autodiagnostico " + query.toString());
			pstm.executeUpdate();

			// Recuperar el ID del autodiagnostico
			int llave = 0;
			ResultSet rsKeysAutodiag = pstm.getGeneratedKeys();
			if (rsKeysAutodiag != null && rsKeysAutodiag.next()) {
				// resultSet.getInt(numeroDeLaColumna) retorna el valor de una columna
				llave = rsKeysAutodiag.getInt(1);
			}

			idAutodiagnostico = llave;
			resultado.getLegajo().setIdAutodiagnostico(idAutodiagnostico);

			System.out.println("Id autodiagnostico " + idAutodiagnostico);

			// actualizacion de la transaccion

			// Grabar respuesta PANTALLA TEMPERATURA
			query = "INSERT INTO ELEA_AUTODIAGNOSTICO.dbo.respuestas values(?, ?, ? )";

			pstm = conn.prepareStatement(query);

			pstm.setObject(1, idAutodiagnostico); // idAutodiagnostico
			pstm.setObject(2, 1); // idPregunta
			pstm.setObject(3, resultado.getTemperatura()); // respuestaPregunta
			pstm.executeUpdate();

			System.out.println("Grabando respuesta temperatura: [" + query + "]");

			// Grabar respuestas PANTALLA SINTOMAS
			StringTokenizer token = new StringTokenizer(resultado.getSintomas(), "@@");

			int totalTokens = token.countTokens();
			int contadorTokens = 0;

			System.out.println("Total tokens Sintomas " + totalTokens);

			while (token.hasMoreTokens()) {

//				query = "INSERT INTO ELEA_AUTODIAGNOSTICO.dbo.respuestas values(?, ?, ? )";

				pstm = conn.prepareStatement(query);

				String[] datos = token.nextToken().split(",");
				pstm.setObject(1, idAutodiagnostico);
				pstm.setObject(2, datos[0]);
				pstm.setObject(3, "" + datos[1]);
				pstm.executeUpdate();
				contadorTokens++;

				System.out.println(query);
			}

			if (totalTokens != contadorTokens) {
				System.out.println("Error en la generacion de las respuestas al autodiagnostico - Sintomas ");
			}

			// Grabar respuestas PANTALLA CONTACTO ESTRECHO
			token = new StringTokenizer(resultado.getContactoEstrecho(), "@@");

			totalTokens = token.countTokens();
			contadorTokens = 0;

			System.out.println("Total tokens Contacto Estrecho " + totalTokens);

			while (token.hasMoreTokens()) {
//				query = "INSERT INTO ELEA_AUTODIAGNOSTICO.dbo.respuestas values(?, ?, ? )";

				pstm = conn.prepareStatement(query);

				String[] datos = token.nextToken().split(",");
				pstm.setObject(1, idAutodiagnostico);
				pstm.setObject(2, datos[0]);
				pstm.setObject(3, "" + datos[1]);
				pstm.executeUpdate();
				contadorTokens++;

				System.out.println(query);
			}

			if (totalTokens != contadorTokens) {
				System.out.println("Error en la generacion de las respuestas al autodiagnostico - Contacto Estrecho");
			}

			// Grabar respuestas PANTALLA ANTECEDENTES
			// antecedentes
			token = new StringTokenizer(resultado.getAntecedentes(), "@@");

			totalTokens = token.countTokens();
			contadorTokens = 0;

			System.out.println("Total tokens Antecedentes " + totalTokens);

			while (token.hasMoreTokens()) {

//				query = "INSERT INTO ELEA_AUTODIAGNOSTICO.dbo.respuestas values(?, ?, ? )";

				pstm = conn.prepareStatement(query);

				String[] datos = token.nextToken().split(",");
				pstm.setObject(1, idAutodiagnostico);
				pstm.setObject(2, datos[0]);
				pstm.setObject(3, "" + datos[1]);
				pstm.executeUpdate();
				contadorTokens++;

				System.out.println(query);
			}

			if (totalTokens != contadorTokens) {
				System.out.println("Error en la generacion de las respuestas al autodiagnostico - Antecedentes");
			}

			// vacunas
			token = new StringTokenizer(resultado.getVacunas(), "@@");

			totalTokens = token.countTokens();
			contadorTokens = 0;

			System.out.println("Total tokens Vacunas " + totalTokens);

			while (token.hasMoreTokens()) {

//				query = "INSERT INTO ELEA_AUTODIAGNOSTICO.dbo.respuestas values(?, ?, ? )";

				pstm = conn.prepareStatement(query);

				String[] datos = token.nextToken().split(",");
				pstm.setObject(1, idAutodiagnostico);
				pstm.setObject(2, datos[0]);
				pstm.setObject(3, "" + datos[1]);
				pstm.executeUpdate();
				contadorTokens++;

				System.out.println(query);
			}

			if (totalTokens != contadorTokens) {
				System.out.println("Error en la generacion de las respuestas al autodiagnostico - Vacunas");
			}

		} catch (Exception e) {
			// todo mensaje de error

			// Crear y ejecutar un metodo de error ( debe enviar un mail al admnistrados,
			// logear el error en la consola del tomcat, y retornar el error al Front End )
			throw new Exception(e);

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}

		return idAutodiagnostico;

	} // END method grabar autodiagnosticos

	public void crearAutodiagnostico(Resultado resultado) {
		// TODO Auto-generated method stub

	}

	// ------------------------------------------------------------------ cfrear
	// Transacciones

	public void crearTransacciones(Resultado resultado) {
		// TODO Auto-generated method stub

	}

	public int bloquear(Integer id, String comentario, String fechaHora) {
		String query = "UPDATE ELEA_AUTODIAGNOSTICO.dbo.autodiagnostico SET comentario = ?, "
				+ "modificadoEn = ?, fecha_hasta_resultado = ? where idAutodiagnostico = ? ";
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		SimpleDateFormat sf = new SimpleDateFormat(DateUtil.FULL_TIMESTAMP_PATTERN);
		try {

			conn = Conexion.generarConexion();

			pstm = conn.prepareStatement(query);
			pstm.setString(1, comentario);
			pstm.setTimestamp(2, new java.sql.Timestamp(System.currentTimeMillis()));
			pstm.setTimestamp(3, new java.sql.Timestamp(sf.parse(fechaHora).getTime()));
			pstm.setInt(4, id);
			int result = pstm.executeUpdate();
			return result;
		} catch (Exception e) {
			log.info("Exception bloquear: " + e.getMessage());
		}
		return 0;
	}

}