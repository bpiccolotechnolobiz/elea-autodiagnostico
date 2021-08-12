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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ar.tbz.conexion.Conexion;
import com.ar.tbz.domain.Legajo;
import com.ar.tbz.domain.LugarAcceso;
import com.ar.tbz.domain.Resultado;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class Servicios {

	private static Log log = LogFactory.getLog(Servicios.class);

	// grabar archivo
	public static void grabarArchivo(String nombreFile, String archivo) {
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
	public static void crearMail(Resultado resultado) throws Exception {
		new Mail().envioMail(resultado);
//		Servicios.grabarArchivo("c://tmp//mail", textoMail);
	}

	// crear datos PDF ------------------------------------
	public static void crearPDF(Resultado resultado) throws Exception {
		// TODO Auto-generated method stub
		new PdfCreateFile().execute(resultado);

	}

	// crear codigo qr------------------------------------
//	public static void generarQRCode(Resultado resultado) throws UnirestException {
//		String body = "{\n" + 
//				"\"data\":\"https://www.qrcode-monkey.com\",\n" + 
//				"\"config\":{\n" + 
//				"\"body\":\"square\",\n" + 
//				"\"eye\":\"frame0\",\n" + 
//				"\"bodyColor\":\"#3f51b5\",\n" + 
//				"\"logo\":\"#facebook\"\n" + 
//				"},\n" + 
//				"\"size\":300,\n" + 
//				"\"download\":false,\n" + 
//				"\"file\":\"svg\"\n" + 
//				"}";
//		
//		Unirest.get("/qr/custom" + "/{placeholder}")
//		  .routeParam("placeholder", "value")
//		  .asJson();
//	}

	public static void crearQRCode(Resultado resultado) throws Exception {

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
			legajo = Servicios.recuperarDniLegajo("" + nroLegajoRecuperaDNI);

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
				legajo = recuperarDniLegajo("34567891");
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

	public synchronized static List<String> recuperarPreguntas(Integer idAutodiagnostico) throws Exception {

		System.out.println("Entra recuperar preguntas y respuestas según idAutodiagnostico");

		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		List<String> listPregRtas = new ArrayList<String>();
		String preguntasRespuestas = "";
		try {
			conn = Conexion.generarConexion();

			String query = "SELECT A.descripcionPregunta, B.respuestaPregunta FROM preguntas A, respuestas B WHERE B.idPregunta=A.idPregunta AND B.idAutodiagnostico="
					+ idAutodiagnostico + " ORDER BY A.idPregunta ASC;";
			pstm = conn.prepareStatement(query);
			rs = pstm.executeQuery();

			while (rs.next()) {
				preguntasRespuestas = rs.getString("descripcionPregunta") + "," + rs.getString("respuestaPregunta");
				listPregRtas.add(preguntasRespuestas);
			}

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					// TODO: handle exception
					throw new Exception(e2);
				}
			}
		}

		return listPregRtas;

	}

	// ------------------------------------------- lugares de acceso
	public synchronized static List<LugarAcceso> recuperarLugaresDeAcceso() throws Exception {

		System.out.println("Entra recupera Lugares de Acceso ");

		List<LugarAcceso> listLugaresAcceso = new ArrayList<LugarAcceso>();
		LugarAcceso lugarAcceso = null;

		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
		boolean status = false;
		try {

			conn = Conexion.generarConexion();

			String query = "SELECT * from ELEA_AUTODIAGNOSTICO.dbo.lugarAcceso";
			pstm = conn.prepareStatement(query);
			rs = pstm.executeQuery();
			while (rs.next()) {
				lugarAcceso = new LugarAcceso();
				lugarAcceso.setIdLugarAcceso(rs.getInt("idLugarAcceso"));
				lugarAcceso.setDescripcionLugarAcceso(rs.getString("descripcionLugarAcceso"));

				listLugaresAcceso.add(lugarAcceso);
			}

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					// TODO: handle exception
					throw new Exception(e2);
				}
			}
		}

		return listLugaresAcceso;

	} // end method recuperarLugaresDeAcceso

	public synchronized static String recuperarLugarDeAcceso(Integer idAcceso) throws Exception {

		System.out.println("Entra recupera Lugar de Acceso ");

		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
		boolean status = false;
		try {

			conn = Conexion.generarConexion();

			String query = "SELECT * from ELEA_AUTODIAGNOSTICO.dbo.lugarAcceso  el  where  el.idLugarAcceso = ?";
			pstm = conn.prepareStatement(query);
			pstm.setInt(1, idAcceso);
			rs = pstm.executeQuery();
			while (rs.next()) {
				return rs.getString("descripcionLugarAcceso");
			}

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					// TODO: handle exception
					throw new Exception(e2);
				}
			}
		}

		return null;

	} // end method recuperarDniLegadjo

	// ------------------------------------------- legajo by dni y legajo

	/**
	 * Recupera el empleado usando como key el nro de legajo ingresado en la
	 * pantalla Retorna una insancia de Legajo para mostrar los datos en la pantalla
	 * 
	 * @param nroLegajo
	 * @return Legajo
	 * @throws Exception
	 */

	public synchronized static Legajo recuperarDniLegajo(String nroLegajo) throws Exception {

		System.out.println("Entra recuperarDniLegajo ");

		List<Legajo> legajos = new ArrayList<Legajo>();
		Legajo legajo = null;

		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
		boolean status = false;
		try {

			System.out.println("Entra -->  recuperarDniLegajo :: generarConexion ");

			conn = Conexion.generarConexion();

			System.out.println("Sale -->  recuperarDniLegajo :: generarConexion ");

			String query = "SELECT * from ELEA_AUTODIAGNOSTICO.dbo.empleadosActivos  lg  where  lg.nroLegajo = ?";

			pstm = conn.prepareStatement(query);
			pstm.setString(1, nroLegajo);

			System.out.println("Entra -->  recuperarDniLegajo :: ejecutaQuery ");

			rs = pstm.executeQuery();

			System.out.println("Sale -->  recuperarDniLegajo :: ejecutaQuery ");

			while (rs.next()) {
				System.out.println("Entra -->  recuperarDniLegajo :: rs.next() ");

				legajo = new Legajo();

				legajo.setNroLegajo(rs.getString("nrolegajo"));
				legajo.setDni(rs.getString("dni"));
				legajo.setNombre(rs.getString("nombre"));
				legajo.setApellido(rs.getString("apellido"));
				legajo.setTelefono((String) rs.getString("telefono"));
				legajo.setEmailLaboral((String) rs.getString("emailLaboral"));

				legajos.add(legajo);
			}
			System.out.println("Sale -->  recuperarDniLegajo :: rs.next() ");
			int xdebug1 = 0;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					// TODO: handle exception
					throw new Exception(e2);
				}
			}
		}

		System.out.println("\n\n\n --------------------------------------------->  Retorna legajo recuperado  -->  "
				+ " -- " + legajo);

		return legajo;

	} // end method recuperarDniLegadjo

	// ------------------------------------------- Grabar datos en AutoDiagnostico

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

			/*
			 * Insert example
			 * 
			 * INSERT INTO ELEA_AUTODIAGNOSTICO.dbo.autodiagnostico (nroLegajo, dni, nombre,
			 * apellido, telefono, empresa, mail, estadoSintomas, estadoContactoEstrecho,
			 * estadoAntecedentes, resultado, fecha_autodiagnostico, fecha_hasta_resultado,
			 * comentario, modificadoPor, modificadoEn, idLugarAcceso) VALUES('', '', '',
			 * '', '', '', '', 0, 0, 0, 0, '', '', '', '', '', 0);
			 * 
			 * Link --> sql timestamp
			 * https://www.dariawan.com/tutorials/java/java-sql-date-java-sql-time-and-java-
			 * sql-timestamp/
			 * 
			 */

//			pstm = conn.prepareStatement(query);
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

//			pstm.setObject(14, resultado.getFecha_autodiagnostico());
//			pstm.setObject(15, resultado.getFecha_hasta_resultado());

			pstm.setString(14, resultado.getFecha_autodiagnostico());
			pstm.setString(15, resultado.getFecha_hasta_resultado());

			pstm.setObject(16, resultado.getComentario());
			pstm.setObject(17, resultado.getModificadoPor());
			pstm.setObject(18, resultado.getModificadoEn());

			// --------------------------------------------------
			/*
			 * -- ELEA_AUTODIAGNOSTICO.dbo.autodiagnostico definition
			 * 
			 * -- Drop table
			 * 
			 * -- DROP TABLE ELEA_AUTODIAGNOSTICO.dbo.autodiagnostico;
			 * 
			 * CREATE TABLE ELEA_AUTODIAGNOSTICO.dbo.autodiagnostico ( idAutodiagnostico int
			 * IDENTITY(1,1) NOT NULL, nroLegajo varchar(10) COLLATE
			 * SQL_Latin1_General_CP1_CI_AS NULL, dni varchar(10) COLLATE
			 * SQL_Latin1_General_CP1_CI_AS NOT NULL, nombre varchar(25) COLLATE
			 * SQL_Latin1_General_CP1_CI_AS NOT NULL, apellido varchar(25) COLLATE
			 * SQL_Latin1_General_CP1_CI_AS NOT NULL, telefono varchar(20) COLLATE
			 * SQL_Latin1_General_CP1_CI_AS NOT NULL, empresa varchar(50) COLLATE
			 * SQL_Latin1_General_CP1_CI_AS NOT NULL, mail varchar(50) COLLATE
			 * SQL_Latin1_General_CP1_CI_AS NOT NULL, estadoSintomas bit NOT NULL,
			 * estadoContactoEstrecho bit NOT NULL, estadoAntecedentes bit NOT NULL,
			 * resultado bit NOT NULL, fecha_autodiagnostico datetime NOT NULL,
			 * fecha_hasta_resultado datetime NOT NULL, comentario varchar(100) COLLATE
			 * SQL_Latin1_General_CP1_CI_AS NULL, modificadoPor varchar(10) COLLATE
			 * SQL_Latin1_General_CP1_CI_AS NULL, modificadoEn datetime NULL, idLugarAcceso
			 * int NOT NULL, CONSTRAINT PK__autodiag__0A767AEC5B6FF7D0 PRIMARY KEY
			 * (idAutodiagnostico) );
			 * 
			 * 
			 * -- ELEA_AUTODIAGNOSTICO.dbo.autodiagnostico foreign keys
			 * 
			 * ALTER TABLE ELEA_AUTODIAGNOSTICO.dbo.autodiagnostico ADD CONSTRAINT
			 * FK__autodiagn__idLug__2CF2ADDF FOREIGN KEY (idLugarAcceso) REFERENCES
			 * ELEA_AUTODIAGNOSTICO.dbo.lugarAcceso(idLugarAcceso);
			 */
			// ----------------------------------------------------

			/*
			 * Autodiagnosticos (tabla)
			 * 
			 * idAutodiagnostico int nroLegajo String dni String nombre String apellido
			 * String telefono String empresa String mail String idLugarAcceso int
			 * estadoSintomas boolean estadoContactoEstrecho boolean estadoAntecedentes
			 * boolean resultado boolean fecha_autodiagnostico DateTime
			 * fecha_hasta_resultado DateTime comentario string modificadoPor int
			 * modificadoEn DateTime
			 * 
			 * 
			 */

			/*
			 * Resultado
			 * 
			 * private Legajo legajo; private String temperaturaLabel ; private String
			 * sintemasLabel ; private String contactosEstrechoLabel ; private String
			 * antecedentesLabel ; private String temperatura ; private String sintomas ;
			 * // @@ idSintomaLabel, 0/1 @@............@@ private String antecedentes ;
			 * // @@ idAntecedenteLabel, 0/1 @@............@@ ;
			 * 
			 * // --------------------------------------------- nuevo modelo
			 * 
			 * private boolean estadoSintomas; private boolean estadoContactoEstrecho;
			 * private boolean estadoAntecedentes;
			 * 
			 * private boolean resultado; private Date fecha_autodiagnostico; private Date
			 * fecha_hasta_resultado; private String comentario; private int modificadoPor;
			 * private Date modificadoEn;
			 * 
			 * // -------------------------------------------------
			 * 
			 * 
			 * 
			 * Legajo
			 * 
			 * private Integer idAutodiagnostico; private Integer version;
			 * 
			 * private String nroLegajo; private String dni; private String nombre; private
			 * String apellido; private String telefono; private String empresa; private
			 * String mail; private int idLugarAcceso;
			 * 
			 */

			System.out.println("query autodiagnostico " + query.toString());
			pstm.executeUpdate();

			// recuperamos el id generado

			// query = "SELECT * from EVAL_AUTODIAGNOSTICO.dbo.autodiagnostico where
			// fecha_autodiagnostico = ?";
			// query = "SELECT * from ELEA_AUTODIAGNOSTICO.dbo.autodiagnostico order by
			// idAutodiagnostico desc ";

			////////////////////////////////////////////////////////
			// Recuperar el ID del autodiagnostico
			int llave = 0;
			ResultSet rsKeysAutodiag = pstm.getGeneratedKeys();
			if (rsKeysAutodiag != null && rsKeysAutodiag.next()) {
				// resultSet.getInt(numeroDeLaColumna) retorna el valor de una columna
				llave = rsKeysAutodiag.getInt(1);
			}

			idAutodiagnostico = llave;
			resultado.getLegajo().setIdAutodiagnostico(idAutodiagnostico);
			////////////////////////////////////////////////////////

//			query = "SELECT * from ELEA_AUTODIAGNOSTICO.dbo.autodiagnostico where  fecha_autodiagnostico = ? and nroLegajo = ?";
//			pstm = conn.prepareStatement(query);
//			String fecha = resultado.getFecha_autodiagnostico().replace("T", " ");
//			fecha = fecha.substring(0,17) + "00";
//			pstm.setString(1, fecha);
//			pstm.setString(2, resultado.getLegajo().getNroLegajo());
//			rs = pstm.executeQuery();

//			while (rs.next()) {
//				idAutodiagnostico = rs.getInt("idAutodiagnostico");
//				// recuperar el dni y comparar para ver si es el usuario correcto ?
//				resultado.getLegajo().setIdAutodiagnostico(idAutodiagnostico);
//				System.out.println("Id autodiagnostico luego de guardarse en resultado: " + idAutodiagnostico);
//				break;
//			}

			System.out.println("Id autodiagnostico " + idAutodiagnostico);

			// actualizacion de la transaccion

			// Grabar respuesta PANTALLA TEMPERATURA
			query = "INSERT INTO ELEA_AUTODIAGNOSTICO.dbo.respuestas values(?, ?, ? )";

			pstm = conn.prepareStatement(query);

			pstm.setObject(1, idAutodiagnostico);
			pstm.setObject(2, 1);
			pstm.setObject(3, resultado.getTemperatura());
			pstm.executeUpdate();

			System.out.println("Grabando respuesta temperatura: [" + query + "]");

			// Grabar respuestas PANTALLA SINTOMAS
			StringTokenizer token = new StringTokenizer(resultado.getSintomas(), "@@");

			int totalTokens = token.countTokens();
			int contadorTokens = 0;

			System.out.println("Total tokens Sintomas " + totalTokens);

			while (token.hasMoreTokens()) {

				query = "INSERT INTO ELEA_AUTODIAGNOSTICO.dbo.respuestas values(?, ?, ? )";

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

			// Grabar respuestas PANTALLA ANTECEDENTES
			token = new StringTokenizer(resultado.getAntecedentes(), "@@");

			totalTokens = token.countTokens();
			contadorTokens = 0;

			System.out.println("Total tokens Antecedentes " + totalTokens);

			while (token.hasMoreTokens()) {

				query = "INSERT INTO ELEA_AUTODIAGNOSTICO.dbo.respuestas values(?, ?, ? )";

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

	public static void crearAutodiagnostico(Resultado resultado) {
		// TODO Auto-generated method stub

	}

	// ------------------------------------------------------------------ cfrear
	// Transacciones

	public static void crearTransacciones(Resultado resultado) {
		// TODO Auto-generated method stub

	}

	public static int bloquear(Integer id, String comentario, String fechaHora) {
		String query = "UPDATE ELEA_AUTODIAGNOSTICO.dbo.autodiagnostico SET comentario = ?, "
				+ "modificadoEn = ?, fecha_hasta_resultado = ? where idAutodiagnostico = ? ";
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try {

			conn = Conexion.generarConexion();

			pstm = conn.prepareStatement(query);
			pstm.setString(1, comentario);
			pstm.setDate(2, new java.sql.Date(System.currentTimeMillis()));
			pstm.setDate(3, new java.sql.Date(sf.parse(fechaHora).getTime()));
			pstm.setInt(4, id);
			int result = pstm.executeUpdate();
			return result;
		} catch (Exception e) {
			log.info("Exception bloquear: " + e.getMessage());
		}
		return 0;
	}

} // end class servicios

// -------------------------------------------------------

/*
 * Resumen de autoevaluación
 * 
 * Mi temperatura es: 37.0
 * 
 * Sin síntomas
 * 
 * Sin contacto estrecho
 * 
 * Sin antecedentes
 * 
 * 
 * 
 * 
 * 
 */

/*
 * 
 * • FORM AUTOEVALUACION - TEMPERATURA
 * 
 * 37 • FORM AUTOEVALUACION - SINTOMAS
 * 
 * [ "no", "no", "no", "no", "no", "no", "no", "no", "no" ] • FORM
 * AUTOEVALUACION - ANTECEDENTES
 * 
 * [ false, false, false, false, false, false, false, false, false ] • ESTADO
 * SINTOMAS
 * 
 * false • ESTADO CONTACTO ESTRECHO
 * 
 * false • ESTADO ANTECEDENTES
 * 
 * false
 * 
 * 
 */

// -----------------------------------------------------------------

/*
 * 
 * Respuestas
 * 
 * -- ELEA_AUTODIAGNOSTICO.dbo.respuestas definition
 * 
 * -- Drop table
 * 
 * -- DROP TABLE ELEA_AUTODIAGNOSTICO.dbo.respuestas;
 * 
 * CREATE TABLE ELEA_AUTODIAGNOSTICO.dbo.respuestas ( idAutodiagnostico int
 * IDENTITY(1,1) NOT NULL, idPregunta int NOT NULL, respuestaPregunta
 * varchar(10) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL );
 * 
 * 
 * -- ELEA_AUTODIAGNOSTICO.dbo.respuestas foreign keys
 * 
 * ALTER TABLE ELEA_AUTODIAGNOSTICO.dbo.respuestas ADD CONSTRAINT
 * FK__respuesta__idAut__2739D489 FOREIGN KEY (idAutodiagnostico) REFERENCES
 * ELEA_AUTODIAGNOSTICO.dbo.autodiagnostico(idAutodiagnostico); ALTER TABLE
 * ELEA_AUTODIAGNOSTICO.dbo.respuestas ADD CONSTRAINT
 * FK__respuesta__idPre__282DF8C2 FOREIGN KEY (idPregunta) REFERENCES
 * ELEA_AUTODIAGNOSTICO.dbo.preguntas(idPregunta);
 * 
 * 
 * 
 * // se agrega el campo id al comienzo para hacerlo autoincremental
 * 
 * CREATE TABLE ELEA_AUTODIAGNOSTICO.dbo.respuestas ( id int IDENTITY(1,1) NOT
 * NULL, idAutodiagnostico int NOT NULL, idPregunta int NOT NULL,
 * respuestaPregunta varchar(10) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL
 * );
 * 
 * 
 * 
 * 
 */
