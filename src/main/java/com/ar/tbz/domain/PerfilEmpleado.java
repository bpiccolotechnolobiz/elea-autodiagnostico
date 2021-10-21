package com.ar.tbz.domain;

import java.util.List;

public class PerfilEmpleado {
//	private Integer idPerfil;
	private String nroLegajo;
	private String emailUsuario;
	private List<PreguntaRespuesta> preguntasRespuestas;
	private int estadoLogico;
	
//	public Integer getIdPerfil() {
//		return idPerfil;
//	}
//	
//	public void setIdPerfil(Integer idPerfil) {
//		this.idPerfil = idPerfil;
//	}
	
	public String getNroLegajo() {
		return nroLegajo;
	}
	
	public void setNroLegajo(String nroLegajo) {
		this.nroLegajo = nroLegajo;
	}
	
	public String getEmailUsuario() {
		return emailUsuario;
	}
	
	public void setEmailUsuario(String emailUsuario) {
		this.emailUsuario = emailUsuario;
	}
	
	public List<PreguntaRespuesta> getPreguntasRespuestas() {
		return preguntasRespuestas;
	}

	public void setPreguntasRespuestas(List<PreguntaRespuesta> preguntasRespuestas) {
		this.preguntasRespuestas = preguntasRespuestas;
	}

	public int getEstadoLogico() {
		return estadoLogico;
	}
	
	public void setEstadoLogico(int estadoLogico) {
		this.estadoLogico = estadoLogico;
	}

}
