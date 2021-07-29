package com.ar.tbz.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ar.tbz.domain.Autodiagnostico;
import com.ar.tbz.services.BusquedaService;

@RestController

@CrossOrigin(origins = { "http://localhost:4200", "http://34.239.14.244:4200", "http://34.239.14.244",
		"https://autodiagnosticotest.elea.com:2810", "https://autodiagnostico.elea.com:2809" })

public class BusquedaController {

	@Autowired
	BusquedaService busquedaService;

	@GetMapping(value = "/buscar", produces = "application/json")
	public List<Autodiagnostico> buscarDiagnostico(@RequestParam Map<String, String> form, @RequestParam Integer pagina)
			throws Exception {
		System.out.println(pagina);
		return busquedaService.buscarDiagnostico(form);
	}

}