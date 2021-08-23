package com.ar.tbz.domain;

public class Pregunta {

	private Integer idPregunta;
	private Integer idOrdenEnPantalla;

	private int idPantalla;
	private String descripcionPregunta;
	private int estadoLogico;

	public Integer getIdPregunta() {
		return idPregunta;
	}

	public void setIdPregunta(Integer idPregunta) {
		this.idPregunta = idPregunta;
	}

	public Integer getIdOrdenEnPantalla() {
		return idOrdenEnPantalla;
	}

	public void setIdOrdenEnPantalla(Integer idOrdenEnPantalla) {
		this.idOrdenEnPantalla = idOrdenEnPantalla;
	}

	public int getIdPantalla() {
		return idPantalla;
	}

	public void setIdPantalla(int idPantalla) {
		this.idPantalla = idPantalla;
	}

	public String getDescripcionPregunta() {
		return descripcionPregunta;
	}

	public void setDescripcionPregunta(String descripcionPregunta) {
		this.descripcionPregunta = descripcionPregunta;
	}

	public int getEstadoLogico() {
		return estadoLogico;
	}

	public void setEstadoLogico(int estadoLogico) {
		this.estadoLogico = estadoLogico;
	}

}
