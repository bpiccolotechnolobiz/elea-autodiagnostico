package com.ar.tbz.domain;

import java.util.Date;

public class Resultado {
	
	
	private Legajo legajo;
	private String temperaturaLabel ;
	private String sintomasLabel ;
	private String contactosEstrechoLabel ;
	private String antecedentesLabel ;
	private String temperatura ;
	private String sintomas ;  // @@ idSintomaLabel, 0/1 @@............@@
	private String contactoEstrecho ;  // @@ idContactoEstrechoLabel, 0/1 @@............@@
	private String antecedentes ;  // @@ idAntecedenteLabel, 0/1 @@............@@
	private String vacunas; // @@ idPregunta, ninguna/nombre de la vacuna @@..........@@
	
	// ---------------------------------------------   nuevo modelo 
	
	private boolean estadoSintomas;
	private boolean estadoContactoEstrecho;
	private boolean estadoAntecedentes;
	
	private boolean resultado;
	private String 	fecha_autodiagnostico;
	private String 	fecha_hasta_resultado;
	private String  comentario;
	private int		modificadoPor;
	private Date 	modificadoEn;
	// -------------------------------------------------
	public Legajo getLegajo() {
		return legajo;
	}
	public void setLegajo(Legajo legajo) {
		this.legajo = legajo;
	}
	public String getTemperaturaLabel() {
		return temperaturaLabel;
	}
	public void setTemperaturaLabel(String temperaturaLabel) {
		this.temperaturaLabel = temperaturaLabel;
	}
	public String getSintomasLabel() {
		return sintomasLabel;
	}
	public void setSintomasLabel(String sintomasLabel) {
		this.sintomasLabel = sintomasLabel;
	}
	public String getContactosEstrechoLabel() {
		return contactosEstrechoLabel;
	}
	public void setContactosEstrechoLabel(String contactosEstrechoLabel) {
		this.contactosEstrechoLabel = contactosEstrechoLabel;
	}
	public String getAntecedentesLabel() {
		return antecedentesLabel;
	}
	public void setAntecedentesLabel(String antecedentesLabel) {
		this.antecedentesLabel = antecedentesLabel;
	}
	public String getTemperatura() {
		return temperatura;
	}
	public void setTemperatura(String temperatura) {
		this.temperatura = temperatura;
	}
	public String getContactoEstrecho() {
		return contactoEstrecho;
	}
	public void setContactoEstrecho(String contactoEstrecho) {
		this.contactoEstrecho = contactoEstrecho;
	}
	public String getSintomas() {
		return sintomas;
	}
	public void setSintomas(String sintomas) {
		this.sintomas = sintomas;
	}
	public String getAntecedentes() {
		return antecedentes;
	}
	public void setAntecedentes(String antecedentes) {
		this.antecedentes = antecedentes;
	}
	public String getVacunas() {
		return vacunas;
	}
	public void setVacunas(String vacunas) {
		this.vacunas = vacunas;
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
	public String getFecha_autodiagnostico() {
		return fecha_autodiagnostico;
	}
	public void setFecha_autodiagnostico(String fecha_autodiagnostico) {
		this.fecha_autodiagnostico = fecha_autodiagnostico;
	}
	public String getFecha_hasta_resultado() {
		return fecha_hasta_resultado;
	}
	public void setFecha_hasta_resultado(String fecha_hasta_resultado) {
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
	@Override
	public String toString() {
		return "Resultado [legajo=" + legajo + ", temperaturaLabel=" + temperaturaLabel + ", sintemasLabel="
				+ sintomasLabel + ", contactosEstrechoLabel=" + contactosEstrechoLabel + ", antecedentesLabel="
				+ antecedentesLabel + ", temperatura=" + temperatura + ", sintomas=" + sintomas + ", antecedentes="
				+ antecedentes + ", estadoSintomas=" + estadoSintomas + ", estadoContactoEstrecho="
				+ estadoContactoEstrecho + ", estadoAntecedentes=" + estadoAntecedentes + ", resultado=" + resultado
				+ ", fecha_autodiagnostico=" + fecha_autodiagnostico + ", fecha_hasta_resultado="
				+ fecha_hasta_resultado + ", comentario=" + comentario + ", modificadoPor=" + modificadoPor
				+ ", modificadoEn=" + modificadoEn + "]";
	}
	
	
	// --------------------------------------------------
	
	
}
