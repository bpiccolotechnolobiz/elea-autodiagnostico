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
		String query = "SELECT  p.* FROM ELEA_AUTODIAGNOSTICO.dbo.preguntas p order by p.idPregunta";
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

	// RECUPERAR LISTA DE PREGUNTAS+RESPUESTAS -> pregunta@@respuesta
	public List<String> recuperarPreguntasRespuestas(Integer idAutodiagnostico, boolean sinVacunacion) throws Exception {

		System.out.println("Entra recuperar preguntas y respuestas según idAutodiagnostico");

		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		List<String> listPregRtas = new ArrayList<String>();
		String preguntasRespuestas = "";
		try {
			conn = Conexion.generarConexion();

			String query = "SELECT A.descripcionPregunta, B.respuestaPregunta FROM preguntas A, respuestas B WHERE B.idPregunta=A.idPregunta AND B.idAutodiagnostico="
					+ idAutodiagnostico + " ";
			
			if(sinVacunacion) {
				query += "AND A.idPantalla<>5";
			} else { // para que retorne solo las de Vacunacion
				query +="AND A.idPantalla=5";
			}
			
			query += " ORDER BY A.idPregunta ASC;";
			
			pstm = conn.prepareStatement(query);
			rs = pstm.executeQuery();

			while (rs.next()) {
				preguntasRespuestas = rs.getString("descripcionPregunta") + "@@" + rs.getString("respuestaPregunta");
				listPregRtas.add(preguntasRespuestas);
			}

		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					// TODO: handle exception
					throw new Exception(e2);
				}
			}
		}

		return listPregRtas;

	}

	public void deletePregunta(int id) throws SQLException {
		Connection conn = null;
		PreparedStatement pstm = null;

		conn = Conexion.generarConexion();

		String query = "Delete from pregunta where id = " + id;
		pstm = conn.prepareStatement(query);
		pstm.executeUpdate();
	}

	public void insertPregunta(Pregunta pregunta) throws SQLException {
		Connection conn = null;
		PreparedStatement pstm = null;

		conn = Conexion.generarConexion();

		String query = "Insert into pregunta values (idOrdenEnPantalla, idPantalla, descripcionPregunta, estadoLogico) values "
				+ "(? , ? , ? , ? )";
		pstm = conn.prepareStatement(query);
		pstm.setInt(1, pregunta.getIdOrdenEnPantalla());
		pstm.setInt(2, pregunta.getIdPantalla());
		pstm.setString(3, pregunta.getDescripcionPregunta());
		pstm.setInt(4, pregunta.getEstadoLogico());
		pstm.executeUpdate();
	}

	public void updatePregunta(Pregunta pregunta) throws SQLException {
		Connection conn = null;
		PreparedStatement pstm = null;

		conn = Conexion.generarConexion();

		String query = "UPDATE pregunta SET idOrdenEnPantalla = ?, idPantalla= ?, descripcionPregunta =?, estadoLogico = ? "
				+ " WHERE idPregunta = ? ";
		pstm = conn.prepareStatement(query);
		pstm.setInt(1, pregunta.getIdOrdenEnPantalla());
		pstm.setInt(2, pregunta.getIdPantalla());
		pstm.setString(3, pregunta.getDescripcionPregunta());
		pstm.setInt(4, pregunta.getEstadoLogico());
		pstm.setInt(5, pregunta.getIdPregunta());
		pstm.executeUpdate();
	}
}
