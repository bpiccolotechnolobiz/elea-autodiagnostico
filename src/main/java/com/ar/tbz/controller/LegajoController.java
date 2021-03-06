package com.ar.tbz.controller;

import java.util.Date;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ar.tbz.domain.Legajo;
import com.ar.tbz.domain.Resultado;
import com.ar.tbz.domain.Test;
import com.ar.tbz.services.Servicios;

@RestController
@RequestMapping("/legajo")
@CrossOrigin(origins = { "http://localhost:4200", "http://34.239.14.244:4200", "http://34.239.14.244",
		"https://autodiagnosticotest.elea.com:2810", "https://autodiagnostico.elea.com:2809" })

public class LegajoController {

	// si es true se hace un test local si es false se hace una conexion a la base
	// de datos SQLServer

	boolean testLocalhost = false;
//	boolean testLocalhost = true;

	// Test de datos prueba desde el Front End
	// -----------------------------------------------------------------------------------------
	// Generar Autodiagnostico
	@RequestMapping(value = "/autodiagnosticoTest", method = RequestMethod.POST, produces = "application/json")
	public String crearAutodiagnostico(@RequestBody Test test) throws Exception {

		System.out.println("Entra al proceso autodiagnostico " + new Date());
		String datos = test.getDatos();

		// cambiar este mensaje por el de error si corresponde
		return datos;
	}

	@RequestMapping(value = "/test", method = RequestMethod.POST, produces = "application/json")
	public String test(@RequestBody Test test) throws Exception {

		String datos = test.getDatos();

		// cambiar este mensaje por el de error si corresponde
		return datos;
	}

	// ---------------------------------------------------------------------------------------------
	// recuperar legajo

	@RequestMapping(value = "/empleado/{id}", method = RequestMethod.GET, produces = "application/json")

	public Legajo showLegajo(@PathVariable Integer id, Model model) throws Exception {

		System.out.println("Entra al proceso Legajo " + new Date());

		Legajo legajo = new Legajo();

		if (testLocalhost) {
			legajo.setNroLegajo("12345678");
			legajo.setDni("12345678");
			legajo.setNombre("Juan empleado legajo 12345678");
			legajo.setApellido("Perez empleado DNI 123678");
			legajo.setTelefono("011-2345-6789");
			legajo.setEmailLaboral("Juan.Perez@Elea.com.ar");
			legajo.setEmailUsuario("Juan.Perez@gmail.com.ar");
		} else {
			System.out.println("Entra al read base de datos " + new Date());

			legajo = Servicios.recuperarDniLegajo("" + id);

			System.out.println("Sale del  read base de datos " + new Date());

		}
//		Servicios.crearMail(new Resultado());

		System.out.println("Sale  del proceso legajo " + new Date() + "   " + legajo);

		return legajo;

	}

	// -----------------------------------------------------------------------------------------
	// Generar Autodiagnostico
	@RequestMapping(value = "/autodiagnostico", method = RequestMethod.POST, produces = "application/json")
	public String crearAutodiagnostico(@RequestBody Resultado resultado) throws Exception {
		int idAutodiagnostico = 0;

		try {
			System.out.println("Entra al proceso autodiagnostico " + new Date());

			resultado.getLegajo().formatearLegajo();
			Legajo legajo = resultado.getLegajo();

			Servicios.grabarAutoDiagnostico(resultado);
			idAutodiagnostico = resultado.getLegajo().getIdAutodiagnostico();

//			Servicios.crearTransacciones(resultado);

			Servicios.crearMail(resultado);

//			Servicios.crearPDF(resultado);
//			Servicios.crearQRCode(resultado);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return e.getMessage();
		}

		// cambiar este mensaje por el de error si corresponde
		return "" + idAutodiagnostico;
	}

	// ---------------------------------------------------------------------------------------------
	// Metodo usado para testear desde el Postman con ID

	// -----------------------------------------------------------------------------------------
	// Generar Autodiagnostico
	@RequestMapping(value = "/autodiagnostico/{id}", method = RequestMethod.POST, produces = "application/json")
	public void crearAutodiagnostico(@PathVariable Integer id) throws Exception {

		Resultado resultado = new Resultado();

		// test create results......
		boolean createResults = true;

		// ------------------------------------ sacar se usa solo testing

		System.out.println("Entra al proceso autodiagnostico/{id} " + new Date() + "ID " + id);

		// Resultado resultado = new Resultado();

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

		// boolean createResults = false ;
		Resultado resultTest = null;
		if (createResults) {
			resultado = createResultsToText();
		}

		Legajo legajo = resultado.getLegajo();
		// legajo

		Servicios.grabarAutoDiagnostico(resultado);

//		Servicios.crearTransacciones(resultado);

		Servicios.crearMail(resultado);
//		Servicios.crearPDF(resultado);
//		Servicios.crearQRCode(resultado);

		// Servicios.envioMail(resultado);

	}

	/*
	 * private Legajo legajo; private String temperaturaLabel ; private String
	 * sintomasLabel ; private String contactosEstrechoLabel ; private String
	 * antecedentesLabel ; private String temperatura ; private String sintomas ;
	 * // @@ idSintomaLabel, 0/1 @@............@@ private String antecedentes ;
	 * // @@ idAntecedenteLabel, 0/1 @@............@@ ;
	 * 
	 * 
	 * private boolean estadoSintomas; private boolean estadoContactoEstrecho;
	 * private boolean estadoAntecedentes;
	 * 
	 * private boolean resultado; private String fecha_autodiagnostico; private
	 * String fecha_hasta_resultado; private String comentario; private int
	 * modificadoPor; private Date modificadoEn;
	 * 
	 * 
	 */

	/*
	 * private Resultado createResultsToText() {
	 * 
	 * Resultado rt = new Resultado(); Legajo lg = new Legajo();
	 * lg.setNroLegajo("87654321"); lg.setDni("87654321");
	 * lg.setNombre("test-01-nombre"); lg.setApellido("test-01-apellido");
	 * lg.setTelefono("876-543-21"); lg.setEmpresa("Empresa-test-01");
	 * lg.setMail("test-01@gmail.com"); lg.setIdLugarAcceso(1);
	 * 
	 * rt.setLegajo(lg); rt.setTemperatura("Sin Temperatura");
	 * rt.setSintomasLabel("Sin sintomas");
	 * rt.setContactosEstrechoLabel("Sin contacto strecho");
	 * rt.setAntecedentesLabel("Sin antecedentes"); rt.setTemperatura("37.00");
	 * String sintomas =
	 * "@@ 0,0 @@ 1,0 @@ 2,0 @@ 3,0 @@ 4,0 @@ 5,0 @@ 6,0 @@ 7,0 @@";
	 * rt.setSintomas(sintomas); String antecedentes =
	 * "@@ 0,0 @@ 1,0 @@ 2,0 @@ 3,0 @@ 4,0 @@ 5,0 @@ 6,0 @@ 7,0 @@";
	 * rt.setAntecedentes(antecedentes); rt.setEstadoSintomas(false);
	 * rt.setEstadoContactoEstrecho(false); rt.setEstadoAntecedentes(false);
	 * rt.setResultado(false);
	 * 
	 * rt.setFecha_autodiagnostico("2021-06-12T12:34:09");
	 * rt.setFecha_hasta_resultado("2021-07-12T12:34:09");
	 * 
	 * 
	 * 
	 * return rt; }
	 * 
	 */

	// ---------------------------------------------------------------------------------------------

	// -------------------------------------------- createResultsText

	private Resultado createResultsToText() {

		Resultado rt = new Resultado();
		Legajo lg = new Legajo();
		lg.setNroLegajo("87654321");
		lg.setDni("87654321");
		lg.setNombre("test-01-nombre");
		lg.setApellido("test-01-apellido");
		lg.setTelefono("876-543-21");
		lg.setEmpresa("Empresa-test-01");
		lg.setEmailLaboral("test-01@gmail.com");
		lg.setIdLugarAcceso(1);

		rt.setLegajo(lg);
		rt.setTemperatura("Sin Temperatura");
		rt.setSintomasLabel("Sin sintomas");
		rt.setContactosEstrechoLabel("Sin contacto strecho");
		rt.setAntecedentesLabel("Sin antecedentes");
		rt.setTemperatura("37.00");
		String sintomas = "@@ 2,0 @@ 3,0 @@ 4,0 @@ 5,0 @@ 6,0 @@ 7,0 @@ 8 ,0 @@ 9, 0 @@ 10,0 @@";
		rt.setSintomas(sintomas);
		String antecedentes = "@@ 11,0 @@ 12,0 @@ 13,0 @@ 14,0 @@ 15,0 @@ 16,0 @@ 17,0 @@ 18 ,0 @@ 19,0 @@";
		rt.setAntecedentes(antecedentes);
		rt.setEstadoSintomas(false);
		rt.setEstadoContactoEstrecho(false);
		rt.setEstadoAntecedentes(false);
		rt.setResultado(false);

		rt.setFecha_autodiagnostico("2021-06-12T12:34:09");
		rt.setFecha_hasta_resultado("2021-07-12T12:34:09");

		return rt;
	}

	// fin de createResult para usar como testeo mock de datos

}
