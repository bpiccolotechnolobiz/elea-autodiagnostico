package com.ar.tbz.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ar.tbz.domain.Autodiagnostico;
import com.ar.tbz.services.Servicios;

@RestController
@RequestMapping("/busqueda")
@CrossOrigin(origins = { "http://localhost:4200", "http://34.239.14.244:4200", "http://34.239.14.244",
		"https://autodiagnosticotest.elea.com:2810", "https://autodiagnostico.elea.com:2809" })

public class BusquedaController {

	@RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json")
	public synchronized static List<Autodiagnostico> buscarDiagnostico(@RequestBody Map<String, String> form)
			throws Exception {
		return Servicios.buscarDiagnostico(form);
	}

}