package com.ar.tbz.controller;

import java.util.Date;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/v2/")
public class IndexControllerJWT {
	@RequestMapping(method = RequestMethod.GET)
	String index() {

		String index = "Autodiagnostico Back-end funcionando correcto. EndPoint existentes: \n { GET [ / ] --> este mensaje } "
				+ " \n { GET [ /legajo/empleado/{id} ] --> Recuperar legajo } "
				+ " \n { POST [ /legajo/autodiagnostico/{Resumen} ] --> Grabar datos autodiagnostico } \n Fecha ejecutado: "
				+ new Date();
		System.out.println("Entra al proceso Index GET V2 " + new Date());

		return index;
	}
}
