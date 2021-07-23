package com.ar.tbz.domain;

import java.util.Date;

public class Respuesta {
 
	private Integer idAutodiagnostico;
	private Integer version;
	
	private int 	idPregunta;	
	private String	respuestaPregunta;
	public Integer getIdAutodiagnostico() {
		return idAutodiagnostico;
	}
	public void setIdAutodiagnostico(Integer idAutodiagnostico) {
		this.idAutodiagnostico = idAutodiagnostico;
	}
	public int getIdPregunta() {
		return idPregunta;
	}
	public void setIdPregunta(int idPregunta) {
		this.idPregunta = idPregunta;
	}
	public String getRespuestaPregunta() {
		return respuestaPregunta;
	}
	public void setRespuestaPregunta(String respuestaPregunta) {
		this.respuestaPregunta = respuestaPregunta;
	}
	@Override
	public String toString() {
		return "Respuesta [idAutodiagnostico=" + idAutodiagnostico + ", version=" + version + ", idPregunta="
				+ idPregunta + ", respuestaPregunta=" + respuestaPregunta + "]";
	}	
	
	// ---------------------------------------------   
	
	
	
	
	/*
	    tabla Respuestas
	    
	    idAutodiagnostico		int
		idPregunta				int
		respuestaPregunta		varchar
	  
	 */
	
}
