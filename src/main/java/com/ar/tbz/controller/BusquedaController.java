package com.ar.tbz.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ar.tbz.domain.Autodiagnostico;
import com.ar.tbz.domain.Respuesta;
import com.ar.tbz.services.BusquedaService;

@RestController

@CrossOrigin(origins = { "http://localhost:4200", "http://34.239.14.244:4200", "http://34.239.14.244",
		"https://autodiagnosticotest.elea.com:2810", "https://autodiagnostico.elea.com:2809",
		"https://autodiagnosticoqas.elea.com:3810", "http://autodiagadminqas.elea.com:8810",
		"http://autodiagadmin.elea.com:8809" })

@RequestMapping("/v1/")
public class BusquedaController {
	private static Log log = LogFactory.getLog(BusquedaController.class);
	private static final int ROWS_PER_PAGE = 3500;
	@Autowired
	BusquedaService busquedaService;

	@GetMapping(value = "/buscar", produces = "application/json")
	public List<Autodiagnostico> buscarDiagnostico(@RequestParam Map<String, String> form, @RequestParam Integer pagina)
			throws Exception {
		int comienzo = Integer.valueOf(pagina);
		String min = String.valueOf((comienzo - 1) * ROWS_PER_PAGE);
		String max = String.valueOf(comienzo * ROWS_PER_PAGE);
		return busquedaService.buscarDiagnostico(form, min, max);
	}

	@GetMapping(value = "/respuestas", produces = "application/json")
	public List<Respuesta> getRespuestas(@RequestParam Integer idAutodiagnostico) throws Exception {
		return busquedaService.buscarRespuestas(idAutodiagnostico);
	}

}