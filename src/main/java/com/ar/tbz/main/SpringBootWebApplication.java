package com.ar.tbz.main;

import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer; // New

@SpringBootApplication (scanBasePackages = { "com.ar.tbz" })
// New
public class SpringBootWebApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebApplication.class, args);
        System.out.println("Comienzo del proceso Autodiagnostico "+ new Date());
    }
    //Esta es una prueba para Git
}