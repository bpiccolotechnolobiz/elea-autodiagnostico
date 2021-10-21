package com.ar.tbz.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ar.tbz.domain.PerfilEmpleado;
import com.ar.tbz.services.PerfilEmpleadoService;

@RestController
@RequestMapping("/v1/perfilEmpleado")
@CrossOrigin(origins = { "http://localhost:4200", "http://34.239.14.244:4200", "http://34.239.14.244",
		"https://autodiagnosticotest.elea.com:2810", "https://autodiagnostico.elea.com:2809",
		"https://autodiagnosticoqas.elea.com:3810", "http://autodiagadminqas.elea.com:8810",
		"https://autodiagadmin.elea.com:8809" })

public class PerfilEmpleadoController {
	private static Log log = LogFactory.getLog(PreguntaController.class);
	@Autowired
	PerfilEmpleadoService perfilEmpleadoService;
	
	@GetMapping(value = "/{nroLegajo}", produces = "application/json")
	public PerfilEmpleado getPerfilEmpleadoByNroLegajo(@PathVariable String nroLegajo) throws Exception {
		log.info("Obtener perfil de empleado");
		return perfilEmpleadoService.getPerfilEmpleadoByNroLegajo(nroLegajo);
	}
}
