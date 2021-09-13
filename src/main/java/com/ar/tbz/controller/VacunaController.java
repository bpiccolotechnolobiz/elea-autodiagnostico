package com.ar.tbz.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ar.tbz.domain.Vacuna;
import com.ar.tbz.services.VacunaService;

@RestController
@RequestMapping("/vacuna")
@CrossOrigin(origins = { "http://localhost:4200", "http://34.239.14.244:4200", "http://34.239.14.244",
		"https://autodiagnosticotest.elea.com:2810", "https://autodiagnostico.elea.com:2809",
		"https://autodiagnosticoqas.elea.com:3810",
		"http://autodiagadminqas.elea.com:8810", "http://autodiagadmin.elea.com:8810" })
public class VacunaController {

	private static Log log = LogFactory.getLog(VacunaController.class);

	@Autowired
	VacunaService vacunaService;

	// ---------------------------------------------------------------------------------------------
	// recuperar vacunas
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
	public List<Vacuna> obtenerVacunas() {
		List<Vacuna> vacunas;
		try {
			vacunas = vacunaService.findAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return vacunas;
	}

	@GetMapping(value = "/{idVacuna}", produces = "application/json")
	public Vacuna buscarVacuna(@PathVariable int idVacuna) throws Exception {
		log.info("Obtener vacuna");
		return vacunaService.findById(idVacuna);
	}

	@PostMapping(value = "/", produces = "application/json")
	public void insertar(@RequestBody Vacuna vacuna) throws Exception {
		log.info("Insertar vacuna");
		vacunaService.insertar(vacuna);
	}

	@PutMapping(value = "/", produces = "application/json")
	public void modificarVacuna(@RequestBody Vacuna vacuna) throws Exception {
		log.info("Modificar vacuna");
		vacunaService.modificar(vacuna);
	}

	@DeleteMapping(value = "/", produces = "application/json")
	public void borrarAccesos(@RequestParam int idVacuna) throws Exception {
		log.info("Borrar vacuna");
		vacunaService.borrar(idVacuna);
	}
}
