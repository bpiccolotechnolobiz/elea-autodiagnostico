package com.ar.tbz.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.ar.tbz.domain.Legajo;
import com.ar.tbz.domain.Resultado;
import com.ar.tbz.services.Servicios;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfCreateFile {
	
//	public static void main(String args[]) throws Exception {
//		
//		Resultado resultado = new Resultado();
//		new PdfCreateFile().execute(resultado);
//		
//
//	}// end main

	public void buildPDFDocument(Resultado resultado) throws Exception {
		// TODO Auto-generated method stub
		
		boolean recuperarNroLegajoDeTabla = true ;
		//boolean recuperarNroLegajoDeTabla = false ;
		
		OutputStream file = null;
		Legajo legajo = resultado.getLegajo();
		String nroLegajo ="" ;
		if (legajo != null) {
			nroLegajo = resultado.getLegajo().getNroLegajo();
		}
		
		String dni = "" ; 
		int nroLegajoRecuperaDNI =  0 ;
		// usado para test de funcionamiento
		// recupera datos de la tabla real
		if (recuperarNroLegajoDeTabla) {
		   dni = "34567891";
		  legajo = Servicios.findByLegajo(""+nroLegajoRecuperaDNI); // ESTO FUNCIONA SOLO CON EMPLEADOS, VISITANTES NO
		 
		}
		
		String archivoNombre = "" ;
		if(nroLegajo == null) {
			archivoNombre = "TestPDF.pdf" ;
		}else {
 			String fecha = resultado.getFecha_autodiagnostico() ;
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
			
			// file = new FileOutputStream(new File("c://Users//JulioMOliveira//tmp//"+archivoNombre+ ".pdf")); 
			 file = new FileOutputStream(new File("c://Users//asman//tmp//pdf//"+archivoNombre+ ".pdf")); 
//			   file = new FileOutputStream(new File("c://tmp//pdf//"+archivoNombre+ ".pdf"));
			// file = new FileOutputStream(new File("c://Users//elea//tmp//pdf//"+archivoNombre+ ".pdf")); 
			
				
		
			// Create a new Document object
			Document document = new Document();
			
			// You need PdfWriter to generate PDF document
			PdfWriter.getInstance(document, file);
			
			// Opening document for writing PDF
			document.open();
			
			// si es true levanta los datos del cargaDatosMail
			boolean testearPDF = true ;
		//	boolean testearPDF = false ;
				
			int xdebug = 0 ; 
		if (testearPDF) {
			
//			String datos = new Mail().cargarDatosMail(resultado);
			
//			document.add(new Paragraph(datos));
	
		}else {
			// Writing content
			document.add(new Paragraph("Hello World, Creating PDF document in Java is easy"));
						
		
			document.add(new Paragraph("You are customer # 2345433")); 
		}
			
			
			
			//document.add(new Paragraph(new Date(new java.util.Date().getTime()).toString()));
			
			// Add meta data information to PDF file document.addCreationDate();
			document.addAuthor("Elea-Autodiagnostico");
			
			document.addTitle("Autodiagnostico COVID-19");
			document.addCreator("Elea Laboratorio"); 
			
			
			// close the document
			document.close();
			
			System.out.println("Your PDF File is succesfully created 2");
		
		} catch (Exception e){
			e.printStackTrace(); 
		} finally { 
			// closing FileOutputStream
			try { if (file != null) { file.close();
		}
		} catch (IOException io) {
			/*Failed to close*/
			} 
		}
	}
}
// Output: Your PDF File is successfully created
