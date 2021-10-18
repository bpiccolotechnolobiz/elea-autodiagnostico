package com.ar.tbz.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ar.tbz.domain.Pregunta;
import com.ar.tbz.services.PreguntaService;

@RestController

@CrossOrigin(origins = { "http://localhost:4200", "http://34.239.14.244:4200", "http://34.239.14.244",
		"https://autodiagnosticotest.elea.com:2810", "https://autodiagnostico.elea.com:2809",
		"https://autodiagnosticoqas.elea.com:3810", "http://autodiagadminqas.elea.com:8810",
		"https://autodiagadmin.elea.com:8809" })

@RequestMapping("/v1/")
public class PreguntaController {
	private static Log log = LogFactory.getLog(PreguntaController.class);
	@Autowired
	PreguntaService preguntaService;

	@RequestMapping(value = "/pregunta", method = RequestMethod.GET, produces = "application/json")
	public List<Pregunta> obtenerPreguntas() throws Exception {
		log.info("obtener Preguntas");
		return preguntaService.findAll();
	}

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

	@DeleteMapping(value = "/pregunta/{id}", produces = "application/json")
	public void borrarPreguntas(@PathVariable int id) throws Exception {
		log.info("borrarPreguntas");
		preguntaService.deletePregunta(id);
	}

	@PutMapping(value = "/pregunta", produces = "application/json")
	public void updatePregunta(@RequestBody Pregunta pregunta) throws Exception {
		log.info("updatePregunta");
		preguntaService.updatePregunta(pregunta);
	}

}