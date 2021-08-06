package com.ar.tbz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ar.tbz.domain.Estadistica;
import com.ar.tbz.domain.Respuesta;
import com.ar.tbz.services.EstadisticaService;

@RestController

@CrossOrigin(origins = { "http://localhost:4200", "http://34.239.14.244:4200", "http://34.239.14.244",
		"https://autodiagnosticotest.elea.com:2810", "https://autodiagnostico.elea.com:2809" })

public class EstadisticaController {

	@Autowired
	EstadisticaService estadisticaService;

	@GetMapping(value = "/estadistica", produces = "application/json")
	public Estadistica buscarEstadisticas() throws Exception {
		return estadisticaService.obtenerEstadistica();
	}

	@GetMapping(value = "/respuestas", produces = "application/json")
	public List<Respuesta> getRespuestas(@RequestParam Integer idAutodiagnostico) throws Exception {
		return busquedaService.buscarRespuestas(idAutodiagnostico);
	}

}