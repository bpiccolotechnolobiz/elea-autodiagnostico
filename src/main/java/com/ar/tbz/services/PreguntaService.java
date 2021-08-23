package com.ar.tbz.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.ar.tbz.conexion.Conexion;
import com.ar.tbz.domain.Pregunta;

@Service
public class PreguntaService {

	private static Log log = LogFactory.getLog(PreguntaService.class);

	public List<Pregunta> findAll() throws SQLException {
		String query = "SELECT  p.*  ELEA_AUTODIAGNOSTICO.dbo.preguntas p order by p.idPregunta";
		log.info("PreguntasService.findAll");
		List<Pregunta> preguntas = new ArrayList<Pregunta>();
		Connection conn = Conexion.generarConexion();
		PreparedStatement pstm = conn.prepareStatement(query);
		ResultSet rs = pstm.executeQuery();
		while (rs.next()) {
			Pregunta pregunta = new Pregunta();
			pregunta.setIdPregunta(rs.getInt("idPregunta"));
			pregunta.setDescripcionPregunta(rs.getString("descripcionPregunta"));
			pregunta.setEstadoLogico(rs.getInt("estadoLogico"));
			pregunta.setIdOrdenEnPantalla(rs.getInt("idOrdenEnPantalla"));
			pregunta.setIdPantalla(rs.getInt("idPantalla"));
			preguntas.add(pregunta);
		}

		conn.close();
		return preguntas;
	}
}
