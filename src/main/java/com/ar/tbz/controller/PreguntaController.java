package com.ar.tbz.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ar.tbz.domain.Pregunta;
import com.ar.tbz.services.PreguntaService;

@RestController

@CrossOrigin(origins = { "http://localhost:4200", "http://34.239.14.244:4200", "http://34.239.14.244",
		"https://autodiagnosticotest.elea.com:2810", "https://autodiagnostico.elea.com:2809",
		"https://autodiagnosticoqas.elea.com:3810" })
public class PreguntaController {
	private static Log log = LogFactory.getLog(PreguntaController.class);
	@Autowired
	PreguntaService preguntaService;

	@PostMapping(value = "/pregunta", produces = "application/json")
	public void insertarPreguntas(@RequestBody Pregunta pregunta) throws Exception {
		log.info("insertar Preguntas");
		preguntaService.insertPregunta(pregunta);
	}

	@PutMapping(value = "/pregunta", produces = "application/json")
	public void modificarPreguntas(@RequestBody Pregunta pregunta) throws Exception {
		log.info("modificar Preguntas");
		preguntaService.updatePregunta(pregunta);
	}

	@DeleteMapping(value = "/pregunta", produces = "application/json")
	public void eliminarPreguntas(@RequestParam int idPregunta) throws Exception {
		log.info("borrar Preguntas");
		preguntaService.deletePregunta(idPregunta);
	}

}