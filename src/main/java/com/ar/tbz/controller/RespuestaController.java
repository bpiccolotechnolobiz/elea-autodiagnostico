package com.ar.tbz.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ar.tbz.domain.Respuesta;
import com.ar.tbz.services.RespuestaService;

@RestController
@RequestMapping("/v1/respuesta")
@CrossOrigin(origins = { "http://localhost:4200", "http://34.239.14.244:4200", "http://34.239.14.244",
		"https://autodiagnosticotest.elea.com:2810", "https://autodiagnostico.elea.com:2809",
		"https://autodiagnosticoqas.elea.com:3810", "http://autodiagadminqas.elea.com:8810",
		"https://autodiagadmin.elea.com:8809" })

public class RespuestaController {

	private static Log log = LogFactory.getLog(PreguntaController.class);

	@Autowired
	RespuestaService respuestaService;

	@PostMapping(produces = "application/json")
	public void insert(@RequestBody Respuesta respuesta) throws Exception {
		log.info("insert respuesta");
		respuestaService.insert(respuesta);
	}

	@PutMapping(produces = "application/json")
	public void update(@RequestBody Respuesta respuesta) throws Exception {
		log.info("update respuesta");
		respuestaService.update(respuesta);
	}
}
