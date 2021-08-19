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

import com.ar.tbz.domain.LugarAcceso;
import com.ar.tbz.services.LugarAccesoService;

@RestController
@RequestMapping("/lugaracceso")
@CrossOrigin(origins = { "http://localhost:4200", "http://34.239.14.244:4200", "http://34.239.14.244",
		"https://autodiagnosticotest.elea.com:2810", "https://autodiagnostico.elea.com:2809" })

public class LugarAccesoController {

	private static Log log = LogFactory.getLog(LugarAccesoController.class);

	@Autowired
	LugarAccesoService lugarAccesoService;

	// ---------------------------------------------------------------------------------------------
	// recuperar lugares acceso
	@RequestMapping(value = "/lista", method = RequestMethod.GET, produces = "application/json")
	public List<LugarAcceso> showLugaresAcceso() {
		List<LugarAcceso> listLugaresAcceso;
		try {
			listLugaresAcceso = lugarAccesoService.recuperarLugaresDeAcceso();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return listLugaresAcceso;
	}

	@GetMapping(value = "/accesos", produces = "application/json")
	public List<LugarAcceso> buscarAccesos() throws Exception {
		log.info("Obtener accesos");
		return lugarAccesoService.recuperarLugaresDeAcceso();
	}

	@GetMapping(value = "/accesos/{idAcceso}", produces = "application/json")
	public LugarAcceso buscarAcceso(@PathVariable int idAcceso) throws Exception {
		log.info("Obtener accesos");
		return lugarAccesoService.recuperarLugarDeAcceso(idAcceso);
	}

	@PostMapping(value = "/accesos", produces = "application/json")
	public void insertarAaccesos(@RequestBody LugarAcceso lugar) throws Exception {
		log.info("Insertar accesos");
		lugarAccesoService.insertar(lugar);
	}

	@PutMapping(value = "/accesos", produces = "application/json")
	public void modificarAccesos(@RequestBody LugarAcceso lugar) throws Exception {
		log.info("Modificar accesos");
		lugarAccesoService.modificar(lugar);
	}

	@DeleteMapping(value = "/accesos", produces = "application/json")
	public void borrarAccesos(@RequestParam int idAcceso) throws Exception {
		log.info("Borrar accesos");
		lugarAccesoService.borrar(idAcceso);
	}

}
