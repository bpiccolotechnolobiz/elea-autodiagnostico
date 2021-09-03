package com.ar.tbz.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ar.tbz.domain.Legajo;
import com.ar.tbz.domain.Resultado;
import com.ar.tbz.services.EmpleadoService;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@Component
public class PdfCreateFile {

	@Autowired
	EmpleadoService empleadoService;

//	public static void main(String args[]) throws Exception {
//		
//		Resultado resultado = new Resultado();
//		new PdfCreateFile().execute(resultado);
//		
//
//	}// end main
	public void generarPDF(String fileNamePDF, String fileNameQR, Resultado resultado) {
		Document document = new Document();
		
		FileOutputStream file = null;

		try {
			file = new FileOutputStream(fileNamePDF);
			

			// -----------------------------Create a new Document object

			// You need PdfWriter to generate PDF document
			PdfWriter.getInstance(document, file);

			// Opening document for writing PDF
			document.open();
			
			// Titulo
			Paragraph title = new Paragraph("Elea te cuida");
			title.setAlignment(Element.ALIGN_CENTER);
			
			document.add(title);
			document.add(Chunk.NEWLINE);
			
			//
			Date fecha_autodiagnostico = DateUtil.formatParse(DateUtil.FULL_TIMESTAMP_PATTERN, resultado.getFecha_autodiagnostico());
			Date fecha_hasta_resultado = DateUtil.formatParse(DateUtil.FULL_TIMESTAMP_PATTERN, resultado.getFecha_hasta_resultado());
			String pattern = "dd/MM/yyyy HH:mm:ss";
			
			document.add(new Paragraph("Nombre y apellido: " + resultado.getLegajo().getNombre() + " " + resultado.getLegajo().getApellido()));
			document.add(new Paragraph("DNI: " + resultado.getLegajo().getDni()));
			document.add(Chunk.NEWLINE);
			document.add(new Paragraph("Fecha generación: " + DateUtil.formatSdf(pattern, fecha_autodiagnostico)));
			document.add(new Paragraph("Fecha vencimiento: " + DateUtil.formatSdf(pattern, fecha_hasta_resultado)));

			document.add(Chunk.NEWLINE);
			
			// QR
			document.add(new Paragraph("Presente el siguiente QR a quien corresponda:"));
			Image image = Image.getInstance(fileNameQR);
			image.setAlignment(Element.ALIGN_CENTER);
			
			document.add(image);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// close the document
			document.close();
			// closing FileOutputStream
			try {
				if (file != null) {
					file.close();
				}
			} catch (IOException io) {
				/* Failed to close */
			}
		}
	}

	public Document buildPDFDocument(Resultado resultado, Image qrImage) throws Exception {
		// TODO Auto-generated method stub
		Document document = new Document();
		boolean recuperarNroLegajoDeTabla = true;
		// boolean recuperarNroLegajoDeTabla = false ;

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
			legajo = empleadoService.findByLegajo("" + nroLegajoRecuperaDNI); // ESTO FUNCIONA SOLO CON EMPLEADOS,
																				// VISITANTES
			// NO

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
//			file = new FileOutputStream(new File("c://Users//asman//tmp//pdf//" + archivoNombre + ".pdf"));
//			   file = new FileOutputStream(new File("c://tmp//pdf//"+archivoNombre+ ".pdf"));
			file = new FileOutputStream("autodiagnostico.pdf");
			// File("c://Users//elea//tmp//pdf//"+archivoNombre+ ".pdf"));

			// Create a new Document object

			// You need PdfWriter to generate PDF document
			PdfWriter.getInstance(document, file);

			// Opening document for writing PDF
			document.open();

			// si es true levanta los datos del cargaDatosMail
			// Writing content
			document.add(new Paragraph("Fecha test: " + resultado.getFecha_autodiagnostico()));
			document.add(new Paragraph("Fecha hasta: " + resultado.getFecha_hasta_resultado()));
			document.add(new Paragraph("Comentario: " + resultado.getComentario()));

//			Graphics2D g2d = bufferedImage.createGraphics();
//			g2d.drawImage(bufferedImage, 0, 0, null);
//			g2d.dispose();

//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			ImageIO.write(bufferedImage, "PNG", baos);
//
//			Image img = com.itextpdf.text.Image.getInstance(baos.toByteArray());

			document.add(qrImage);

			// document.add(new Paragraph(new Date(new
			// java.util.Date().getTime()).toString()));

			// Add meta data information to PDF file document.addCreationDate();
			document.addAuthor("Elea-Autodiagnostico");

			document.addTitle("Autodiagnostico COVID-19");
			document.addCreator("Elea Laboratorio");

			// close the document
			document.close();

			System.out.println("Your PDF File is succesfully created 2");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// closing FileOutputStream
			try {
				if (file != null) {
					file.close();
				}
			} catch (IOException io) {
				/* Failed to close */
			}
		}
		return document;
	}

	public void crearPDF(Resultado resultado, Image img, String fileName) throws Exception {
		Document document = new Document();
		FileOutputStream file = null;

		Legajo legajo = resultado.getLegajo();
		String nroLegajo = "";
		if (legajo != null) {
			nroLegajo = resultado.getLegajo().getNroLegajo();
		}

		String dni = "";

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
			archivoNombre = fecha + "_" + dni;
		}

		try {

			archivoNombre = archivoNombre.replace(":", "-"); // NO SE ESTÁ USANDO

//			file = new FileOutputStream("autodiagnostico.pdf"); // RUTA + archivoNombre
			file = new FileOutputStream(fileName);

			// Create a new Document object

			// You need PdfWriter to generate PDF document
			PdfWriter.getInstance(document, file);

			// Opening document for writing PDF
			document.open();

			// si es true levanta los datos del cargaDatosMail
			// Writing content
			document.add(new Paragraph(
					"Nombre: " + resultado.getLegajo().getNombre() + " " + resultado.getLegajo().getApellido()));
			document.add(Chunk.NEWLINE);
			document.add(new Paragraph("Resultado: " + (resultado.isResultado() ? "Habilitado" : "No habilitado")));
			document.add(new Paragraph("Fecha generado: " + resultado.getFecha_autodiagnostico()));
			document.add(new Paragraph("Fecha vencimiento: " + resultado.getFecha_hasta_resultado()));

			document.add(img);

			// document.add(new Paragraph(new Date(new
			// java.util.Date().getTime()).toString()));

			// Add meta data information to PDF file document.addCreationDate();
			document.addAuthor("Elea-Autodiagnostico");

			document.addTitle("Autodiagnostico COVID-19");
			document.addCreator("Elea Laboratorio");

			// close the document
			document.close();

			System.out.println("Your PDF File is succesfully created 2");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// closing FileOutputStream
			try {
				if (file != null) {
					file.close();
				}
			} catch (IOException io) {
				/* Failed to close */
			}
		}
	}
}
// Output: Your PDF File is successfully created
