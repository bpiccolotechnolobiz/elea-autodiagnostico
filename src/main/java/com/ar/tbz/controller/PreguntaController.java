package com.ar.tbz.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ar.tbz.domain.Pregunta;
import com.ar.tbz.services.PreguntaService;

@RestController

@CrossOrigin(origins = { "http://localhost:4200", "http://34.239.14.244:4200", "http://34.239.14.244",
		"https://autodiagnosticotest.elea.com:2810", "https://autodiagnostico.elea.com:2809",
		"https://autodiagnosticotestqas.elea.com:2810", "https://autodiagnosticotestqas.elea.com:2809", })
public class PreguntaController {
	private static Log log = LogFactory.getLog(PreguntaController.class);
	@Autowired
	PreguntaService preguntaService;

	@GetMapping(value = "/pregunta", produces = "application/json")
	public List<Pregunta> buscarPreguntas() throws Exception {
		log.info("buscarPreguntas");
		return preguntaService.findAll();
	}

}