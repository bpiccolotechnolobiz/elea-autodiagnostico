package com.ar.tbz.domain;

public class Vacuna {
	private Integer idVacuna;
	private String descripcionVacuna;
	private int estadoLogico;

	public Vacuna(String descripcionVacuna, int estado) {
		this.descripcionVacuna = descripcionVacuna;
		this.estadoLogico = estado;
	}

	public Vacuna() {
		// TODO Auto-generated constructor stub
	}

	public Vacuna(int id, String descripcionVacuna, int estado) {
		this.idVacuna = id;
		this.descripcionVacuna = descripcionVacuna;
		this.estadoLogico = estado;
	}

	public Integer getIdVacuna() {
		return idVacuna;
	}

	public void setIdVacuna(Integer idVacuna) {
		this.idVacuna = idVacuna;
	}

	public String getDescripcionVacuna() {
		return descripcionVacuna;
	}

	public void setDescripcionVacuna(String descripcionVacuna) {
		this.descripcionVacuna = descripcionVacuna;
	}

	public int getEstadoLogico() {
		return estadoLogico;
	}

	public void setEstadoLogico(int estadoLogico) {
		this.estadoLogico = estadoLogico;
	}

}
