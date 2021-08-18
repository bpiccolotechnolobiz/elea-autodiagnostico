package com.ar.tbz.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ar.tbz.domain.Estadistica;
import com.ar.tbz.domain.Parametro;
import com.ar.tbz.services.EstadisticaService;

@RestController

@CrossOrigin(origins = { "http://localhost:4200", "http://34.239.14.244:4200", "http://34.239.14.244",
		"https://autodiagnosticotest.elea.com:2810", "https://autodiagnostico.elea.com:2809" })

public class EstadisticaController {
	private static Log log = LogFactory.getLog(EstadisticaController.class);
	@Autowired
	EstadisticaService estadisticaService;

	@GetMapping(value = "/estadistica", produces = "application/json")
	public Estadistica buscarEstadisticas(@RequestParam String fechaDesde, @RequestParam String fechaHasta)
			throws Exception {
		log.info("Obtener estadistica");
		return estadisticaService.obtenerEstadistica(fechaDesde, fechaHasta);
	}

	@GetMapping(value = "/parametros", produces = "application/json")
	public List<Parametro> buscarParametros() throws Exception {
		log.info("Obtener parametros");
		return estadisticaService.obtenerParametros();
	}

	@PostMapping(value = "/parametros", produces = "application/json")
	public void insertarParametros(@RequestBody Parametro param) throws Exception {
		log.info("Insertar parametros");
		estadisticaService.insertarParametro(param);
	}

	@PutMapping(value = "/parametros", produces = "application/json")
	public void modificarParametros(@RequestBody Parametro param) throws Exception {
		log.info("Modificar parametros");
		estadisticaService.modificarParametro(param);
	}

	@DeleteMapping(value = "/parametros", produces = "application/json")
	public void borrarParametros(@RequestParam int idParam) throws Exception {
		log.info("Borrar parametros");
		estadisticaService.borrarParametro(idParam);
	}

}