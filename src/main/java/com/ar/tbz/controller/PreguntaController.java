package com.ar.tbz.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ar.tbz.domain.Pregunta;
import com.ar.tbz.services.PreguntaService;

@RestController

@CrossOrigin(origins = { "http://localhost:4200", "http://34.239.14.244:4200", "http://34.239.14.244",
		"https://autodiagnosticotest.elea.com:2810", "https://autodiagnostico.elea.com:2809",
		"https://autodiagnosticoqas.elea.com:3810", "http://autodiagadminqas.elea.com:8810",
		"http://autodiagadmin.elea.com:8809" })

@RequestMapping("/v1/")
public class PreguntaController {
	private static Log log = LogFactory.getLog(PreguntaController.class);
	@Autowired
	PreguntaService preguntaService;

	@GetMapping(value = "/pregunta", produces = "application/json")
	public List<Pregunta> buscarPreguntas() throws Exception {
		log.info("buscarPreguntas");
		return preguntaService.findAll();
	}

	@DeleteMapping(value = "/pregunta/{id}", produces = "application/json")
	public void borrarPreguntas(@PathVariable int id) throws Exception {
		log.info("borrarPreguntas");
		preguntaService.deleteById(id);
	}

	@PutMapping(value = "/pregunta", produces = "application/json")
	public void updatePregunta(@RequestBody Pregunta pregunta) throws Exception {
		log.info("updatePregunta");
		preguntaService.update(pregunta);
	}

}