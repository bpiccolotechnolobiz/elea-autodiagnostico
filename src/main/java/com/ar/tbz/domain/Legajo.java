package com.ar.tbz.domain;

import java.util.Date;

public class Legajo {
 
	private Integer idAutodiagnostico;
	private Integer version;
	
	private String nroLegajo;	
	private String dni;	
	private String nombre;
	private String apellido;
	private String telefono;
	private String empresa;
	private String emailLaboral;
	private String emailUsuario;
	private int    idLugarAcceso;
	
	// ---------------------------------------------   
	
	public Integer getIdAutodiagnostico() {
		return idAutodiagnostico;
	}
	public void setIdAutodiagnostico(Integer idAutodiagnostico) {
		this.idAutodiagnostico = idAutodiagnostico;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getNroLegajo() {
		return nroLegajo;
	}
	public void setNroLegajo(String nroLegajo) {
		this.nroLegajo = nroLegajo;
	}
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getEmpresa() {
		return empresa;
	}
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	public String getEmailLaboral() {
		return emailLaboral;
	}
	public void setEmailLaboral(String emailLaboral) {
		this.emailLaboral = emailLaboral;
	}
	public String getEmailUsuario() {
		return emailUsuario;
	}
	public void setEmailUsuario(String emailUsuario) {
		this.emailUsuario = emailUsuario;
	}
	public int getIdLugarAcceso() {
		return idLugarAcceso;
	}
	public void setIdLugarAcceso(int idLugarAcceso) {
		this.idLugarAcceso = idLugarAcceso;
	}
	@Override
	public String toString() {
		return "Legajo [idAutodiagnostico=" + idAutodiagnostico + ", version=" + version + ", nroLegajo=" + nroLegajo
				+ ", dni=" + dni + ", nombre=" + nombre + ", apellido=" + apellido + ", telefono=" + telefono
				+ ", empresa=" + empresa + ", mail=" + emailLaboral + ", idLugarAcceso=" + idLugarAcceso + "]";
	}
	 	
	
	// --------------------------------------------------------
	
	// capitaliza nombre y apellido, emails en lowercase, dni en uppercase
	public void formatearLegajo() {
		this.setNombre(this.capitalizar(this.getNombre()));
		this.setApellido(this.capitalizar(this.getApellido()));
		
		if (this.getDni() != null || !this.getDni().isEmpty()) {
			this.setDni(this.getDni().toUpperCase());
		}
		
		if (this.getEmailLaboral() != null || !this.getEmailLaboral().isEmpty()) {
			this.setEmailLaboral(this.getEmailLaboral().toLowerCase());
		}
		
		if (this.getEmailUsuario() != null || !this.getEmailUsuario().isEmpty()) {
			this.setEmailUsuario(this.getEmailUsuario().toLowerCase());
		}
	}

	private String capitalizar(String str) {
		String capitalizado = "";
		
		if(str == null || str.isEmpty()) {
	        return str;
	    }
		
		String[] strSplit = str.split(" ");
		
		if(strSplit.length == 1) {
			capitalizado = str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
		} else {
			for (String strDelSplit : strSplit) {
				capitalizado += strDelSplit.substring(0, 1).toUpperCase() + strDelSplit.substring(1).toLowerCase() + " ";
			}
			capitalizado = capitalizado.substring(0, capitalizado.length()-1);
		}		
		
		return capitalizado;
	}
	// ------------------------

}
