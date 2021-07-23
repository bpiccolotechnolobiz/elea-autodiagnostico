package com.ar.tbz.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import com.ar.tbz.domain.Test;

@RestController

// la llamada desde el cliente a (http://localhost:8080) entra aqui porque no hay nada despues del 8080
//@RequestMapping("/product") este RequestMapping va a ingresar por aqui cuando venga /product desde el cliente


/*
@Controller
@RequestMapping("/")
class IndexController0 {
	@RequestMapping(method = RequestMethod.POST)
	String updateFoos(@RequestParam Test test) {
		return "Parameters are ";

	}
}

*/

@RequestMapping("/")
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
}

@RequestMapping("/")
class IndexController2 {
	@RequestMapping(method = RequestMethod.POST)
	String index() {

		String index = "Metodo mal utilizado " + new Date();
		System.out.println("Entra al proceso Index POST " + new Date());

		return index;
	}
}

@RequestMapping("/")
class IndexController3 {
	@RequestMapping(method = RequestMethod.PUT)
	String index() {

		String index = "Metodo mal utilizado " + new Date();
		System.out.println("Entra al proceso Index PUT " + new Date());

		return index;
	}
}

@RequestMapping("/")
class IndexController4 {
	@RequestMapping(method = RequestMethod.DELETE)
	String index() {

		String index = "Metodo mal utilizado " + new Date();
		System.out.println("Entra al proceso Index DELETE " + new Date());

		return index;
	}
}
