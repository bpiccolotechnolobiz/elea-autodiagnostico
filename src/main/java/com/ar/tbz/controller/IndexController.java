package com.ar.tbz.controller;

import java.util.Date;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController

// la llamada desde el cliente a (http://localhost:8080) entra aqui porque no hay nada despues del 8080
//@RequestMapping("/product") este RequestMapping va a ingresar por aqui cuando venga /product desde el cliente

/*
 * @Controller
 * 
 * @RequestMapping("/") class IndexController0 {
 * 
 * @RequestMapping(method = RequestMethod.POST) String updateFoos(@RequestParam
 * Test test) { return "Parameters are ";
 * 
 * } }
 * 
 */

@RequestMapping("/v1/")
public class IndexController {

	@RequestMapping(method = RequestMethod.GET)
	String index() {

		String index = "Autodiagnostico Back-end funcionando correcto. EndPoint existentes: \n { GET [ / ] --> este mensaje } "
				+ " \n { GET [ /legajo/empleado/{id} ] --> Recuperar legajo } "
				+ " \n { POST [ /legajo/autodiagnostico/{Resumen} ] --> Grabar datos autodiagnostico } \n Fecha ejecutado: "
				+ new Date();
		System.out.println("Entra al proceso Index GET " + new Date());

		return index;
	}

	@PostMapping("/")
	String indexPost() {

		String index = "Metodo mal utilizado " + new Date();
		System.out.println("Entra al proceso Index POST " + new Date());

		return index;
	}

	@PutMapping("/")
	String indexPut() {

		String index = "Metodo mal utilizado " + new Date();
		System.out.println("Entra al proceso Index PUT " + new Date());

		return index;
	}

	@DeleteMapping("/")
	String indexDelete() {

		String index = "Metodo mal utilizado " + new Date();
		System.out.println("Entra al proceso Index DELETE " + new Date());

		return index;
	}
}
