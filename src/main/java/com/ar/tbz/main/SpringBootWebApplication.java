package com.ar.tbz.main;

import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer; // New
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ar.tbz.conexion.JWTAuthorizationFilter;

@SpringBootApplication(scanBasePackages = { "com.ar.tbz" })
// New
public class SpringBootWebApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebApplication.class, args);
		System.out.println("Comienzo del proceso Autodiagnostico " + new Date());
	}
	// Esta es una prueba para Git

	@EnableWebSecurity
	@Configuration
	class WebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable()
					.addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
					.authorizeRequests().antMatchers("/v1/**").permitAll().antMatchers(HttpMethod.POST, "/v2/user")
					.permitAll().anyRequest().authenticated();
		}
	}

}