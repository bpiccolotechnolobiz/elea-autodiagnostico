package com.ar.tbz.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ar.tbz.domain.Estadistica;
import com.ar.tbz.services.EstadisticaService;

@RestController

@CrossOrigin(origins = { "http://localhost:4200", "http://34.239.14.244:4200", "http://34.239.14.244",
		"https://autodiagnosticotest.elea.com:2810", "https://autodiagnostico.elea.com:2809" })

public class EstadisticaController {
	private static Log log = LogFactory.getLog(EstadisticaController.class);
	@Autowired
	EstadisticaService estadisticaService;

	@GetMapping(value = "/estadistica", produces = "application/json")
	public Estadistica buscarEstadisticas() throws Exception {
		log.info("Obtener estadistica");
		return estadisticaService.obtenerEstadistica();
	}

}