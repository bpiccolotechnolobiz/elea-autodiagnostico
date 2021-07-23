package com.ar.tbz.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ar.tbz.domain.LugarAcceso;
import com.ar.tbz.services.Servicios;

@RestController
@RequestMapping("/lugaracceso")
@CrossOrigin(origins = { "http://localhost:4200", "http://34.239.14.244:4200", "http://34.239.14.244",
		"https://autodiagnosticotest.elea.com:2810", "https://autodiagnostico.elea.com:2809" })

public class LugarAccesoController {
	// ---------------------------------------------------------------------------------------------
	// recuperar lugares acceso
	@RequestMapping(value = "/lista", method = RequestMethod.GET, produces = "application/json")
	public List<LugarAcceso> showLugaresAcceso() {
		List<LugarAcceso> listLugaresAcceso;
		try {
			listLugaresAcceso = Servicios.recuperarLugaresDeAcceso();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		return listLugaresAcceso;
	}

}
