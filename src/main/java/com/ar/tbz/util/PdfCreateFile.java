package com.ar.tbz.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ar.tbz.domain.Legajo;
import com.ar.tbz.domain.Resultado;
import com.ar.tbz.services.EmpleadoService;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@Component
public class PdfCreateFile {

	@Autowired
	EmpleadoService empleadoService;
	@Autowired
	QRService qrService;
//	public static void main(String args[]) throws Exception {
//		
//		Resultado resultado = new Resultado();
//		new PdfCreateFile().execute(resultado);
//		
//
//	}// end main

	public Document buildPDFDocument(Resultado resultado) throws Exception {
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
			file = new FileOutputStream(new File("c://Users//asman//tmp//pdf//" + archivoNombre + ".pdf"));
//			   file = new FileOutputStream(new File("c://tmp//pdf//"+archivoNombre+ ".pdf"));
			// file = new FileOutputStream(new
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

			BufferedImage image = qrService.generateQR();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "PNG", baos);

			Image img = Image.getInstance(baos.toByteArray());
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
		return document;
	}
}
// Output: Your PDF File is successfully created
