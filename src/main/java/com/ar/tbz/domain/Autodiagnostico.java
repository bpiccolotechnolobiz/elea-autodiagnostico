package com.ar.tbz.domain;

import java.util.Date;

public class Autodiagnostico {

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
	private int idLugarAcceso;
	private String descripcionLugarAcceso;

	private boolean estadoSintomas;
	private boolean estadoContactoEstrecho;
	private boolean estadoAntecedentes;
	private boolean resultado;
	private Date fecha_autodiagnostico;
	private Date fecha_hasta_resultado;
	private String comentario;
	private int modificadoPor;
	private Date modificadoEn;

	public Autodiagnostico() {

	}

	public Autodiagnostico(int id, String legajo, String dni, String nombre, String apellido, String telefono,
			int lugarAcceso, boolean sintomas, String emailUsuario, String emailLaboral, String empresa,
			boolean estadoContactoEstrecho, boolean estadoAntecedentes, Date fecha_autodiagnostico,
			Date fecha_hasta_resultado, String comentario, int modificadoPor, Date modificadoEn) {
		idAutodiagnostico = id;
//		version = vers;
		nroLegajo = legajo;
		this.dni = dni;
		this.nombre = nombre;
		this.apellido = apellido;
		this.telefono = telefono;
		this.idLugarAcceso = lugarAcceso;
		this.estadoSintomas = sintomas;
		this.emailUsuario = emailUsuario;
		this.emailLaboral = emailLaboral;
		this.empresa = empresa;
		this.estadoContactoEstrecho = estadoContactoEstrecho;
		this.estadoAntecedentes = estadoAntecedentes;
		this.fecha_autodiagnostico = fecha_autodiagnostico;
		this.fecha_hasta_resultado = fecha_hasta_resultado;
		this.comentario = comentario;
		this.modificadoPor = modificadoPor;
		this.modificadoEn = modificadoEn;

	}

	public Autodiagnostico(int id, String legajo, String dni, String nombre, String apellido, String telefono,
			int lugarAcceso, boolean sintomas) {
		idAutodiagnostico = id;
//		version = vers;
		nroLegajo = legajo;
		this.dni = dni;
		this.nombre = nombre;
		this.apellido = apellido;
		this.telefono = telefono;
		this.idLugarAcceso = lugarAcceso;
		this.estadoSintomas = sintomas;

	}

	// --------------------------------------------
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

	public boolean isEstadoSintomas() {
		return estadoSintomas;
	}

	public void setEstadoSintomas(boolean estadoSintomas) {
		this.estadoSintomas = estadoSintomas;
	}

	public boolean isEstadoContactoEstrecho() {
		return estadoContactoEstrecho;
	}

	public void setEstadoContactoEstrecho(boolean estadoContactoEstrecho) {
		this.estadoContactoEstrecho = estadoContactoEstrecho;
	}

	public boolean isEstadoAntecedentes() {
		return estadoAntecedentes;
	}

	public void setEstadoAntecedentes(boolean estadoAntecedentes) {
		this.estadoAntecedentes = estadoAntecedentes;
	}

	public boolean isResultado() {
		return resultado;
	}

	public void setResultado(boolean resultado) {
		this.resultado = resultado;
	}

	public Date getFecha_autodiagnostico() {
		return fecha_autodiagnostico;
	}

	public void setFecha_autodiagnostico(Date fecha_autodiagnostico) {
		this.fecha_autodiagnostico = fecha_autodiagnostico;
	}

	public Date getFecha_hasta_resultado() {
		return fecha_hasta_resultado;
	}

	public void setFecha_hasta_resultado(Date fecha_hasta_resultado) {
		this.fecha_hasta_resultado = fecha_hasta_resultado;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public int getModificadoPor() {
		return modificadoPor;
	}

	public void setModificadoPor(int modificadoPor) {
		this.modificadoPor = modificadoPor;
	}

	public Date getModificadoEn() {
		return modificadoEn;
	}

	public void setModificadoEn(Date modificadoEn) {
		this.modificadoEn = modificadoEn;
	}

	public String getDescripcionLugarAcceso() {
		return descripcionLugarAcceso;
	}

	public void setDescripcionLugarAcceso(String descripcionLugarAcceso) {
		this.descripcionLugarAcceso = descripcionLugarAcceso;
	}

	@Override
	public String toString() {
		return "Legajo [idAutodiadnostico=" + idAutodiagnostico + ", version=" + version + ", nroLegajo=" + nroLegajo
				+ ", dni=" + dni + ", nombre=" + nombre + ", apellido=" + apellido + ", telefono=" + telefono
				+ ", empresa=" + empresa + ", emailUsuario=" + emailUsuario + ", idLugarAcceso=" + idLugarAcceso
				+ ", estadoSintomas=" + estadoSintomas + ", estadoContactoEstrecho=" + estadoContactoEstrecho
				+ ", estadoAntecedentes=" + estadoAntecedentes + ", resultado=" + resultado + ", fecha_autodiagnostico="
				+ fecha_autodiagnostico + ", fecha_hasta_resultado=" + fecha_hasta_resultado + ", comentario="
				+ comentario + ", modificadoPor=" + modificadoPor + ", modificadoEn=" + modificadoEn + "]";
	}

	/*
	 * 
	 * idAutodiagnostico int nroLegajo String dni String nombre String apellido
	 * String telefono String empresa String mail String idLugarAcceso int
	 * 
	 * estadoSintomas boolean estadoContactoEstrecho boolean estadoAntecedentes
	 * boolean
	 * 
	 * resultado boolean
	 * 
	 * fecha_autodiagnostico DateTime fecha_hasta_resultado DateTime
	 * 
	 * comentario String
	 * 
	 * modificadoPor int modificadoEn DateTime
	 * 
	 * 
	 * 
	 */

	// --------------------------------------------------------

	// ------------------------

}
