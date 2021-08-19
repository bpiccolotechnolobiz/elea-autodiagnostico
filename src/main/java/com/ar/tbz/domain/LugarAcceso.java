package com.ar.tbz.domain;

public class LugarAcceso {
	private Integer idLugarAcceso;
	private String descripcionLugarAcceso;
	private int estado;

	public LugarAcceso(String descripcionLugarAcceso, int estado) {
		this.descripcionLugarAcceso = descripcionLugarAcceso;
		this.estado = estado;
	}

	public LugarAcceso() {
		// TODO Auto-generated constructor stub
	}

	public LugarAcceso(int id, String descripcionLugarAcceso, int estado) {
		this.idLugarAcceso = id;
		this.descripcionLugarAcceso = descripcionLugarAcceso;
		this.estado = estado;
	}

	public Integer getIdLugarAcceso() {
		return idLugarAcceso;
	}

	public void setIdLugarAcceso(Integer idLugarAcceso) {
		this.idLugarAcceso = idLugarAcceso;
	}

	public String getDescripcionLugarAcceso() {
		return descripcionLugarAcceso;
	}

	public void setDescripcionLugarAcceso(String descripcionLugarAcceso) {
		this.descripcionLugarAcceso = descripcionLugarAcceso;
	}

	public int getEstado() {
		return estado;
	}

	public void setEstado(int estado) {
		this.estado = estado;
	}

}
