package com.ar.tbz.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.ar.tbz.conexion.Conexion;
import com.ar.tbz.domain.Pregunta;
import com.ar.tbz.domain.Respuesta;

@Service
public class PreguntaService {

	private static Log log = LogFactory.getLog(PreguntaService.class);

	public List<Pregunta> findAll() throws SQLException {
		String query = "SELECT  p.* FROM ELEA_AUTODIAGNOSTICO.dbo.preguntas p order by p.idPantalla, p.idOrdenEnPantalla, p.idPregunta";
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
	public List<String> recuperarPreguntasRespuestas(Integer idAutodiagnostico, boolean sinVacunacion)
			throws Exception {

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

			if (sinVacunacion) {
				query += "AND A.idPantalla<>5";
			} else { // para que retorne solo las de Vacunacion
				query += "AND A.idPantalla=5";
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

//		String query = "Delete from pregunta where id = " + id;
		String query = "UPDATE ELEA_AUTODIAGNOSTICO.dbo.preguntas SET estadoLogico = 0 where idPregunta = " + id;
		pstm = conn.prepareStatement(query);
		pstm.executeUpdate();

		if (conn != null) {
			conn.close();
		}
	}

	public void insertPregunta(Pregunta pregunta) throws SQLException {
		Connection conn = null;
		PreparedStatement pstm = null;

		conn = Conexion.generarConexion();

		// obtengo el ultimo idOrdenEnPantalla que posee la pantalla a la cual pertenece
		// la pregunta
		String query = "SELECT TOP 1 idOrdenEnPantalla FROM preguntas WHERE idPantalla = ? ORDER BY idOrdenEnPantalla DESC;";
		pstm = conn.prepareStatement(query);
		pstm.setInt(1, pregunta.getIdPantalla());
		ResultSet rs = pstm.executeQuery();
//		int ultIdOrdenEnPantalla = 0;
		while (rs.next()) {
//			ultIdOrdenEnPantalla = rs.getInt("idOrdenEnPantalla");
			// setteo el idOrdenEnPantalla de la pregunta a insertar
			pregunta.setIdOrdenEnPantalla(rs.getInt("idOrdenEnPantalla") + 1);
		}
//		pregunta.setIdOrdenEnPantalla(ultIdOrdenEnPantalla+1);

		query = "Insert into preguntas (idOrdenEnPantalla, idPantalla, descripcionPregunta, estadoLogico) values "
				+ "(? , ? , ? , ? )";
		pstm = conn.prepareStatement(query);
		pstm.setInt(1, pregunta.getIdOrdenEnPantalla());
		pstm.setInt(2, pregunta.getIdPantalla());
		pstm.setString(3, pregunta.getDescripcionPregunta());
		pstm.setInt(4, pregunta.getEstadoLogico());
		pstm.executeUpdate();

		if (conn != null) {
			conn.close();
		}
	}

	public void updatePregunta(Pregunta pregunta) throws SQLException {
		Connection conn = null;
		PreparedStatement pstm = null;

		conn = Conexion.generarConexion();

		String query = "UPDATE preguntas SET idOrdenEnPantalla = ?, idPantalla= ?, descripcionPregunta =?, estadoLogico = ? "
				+ " WHERE idPregunta = ? ";
		pstm = conn.prepareStatement(query);
		pstm.setInt(1, pregunta.getIdOrdenEnPantalla());
		pstm.setInt(2, pregunta.getIdPantalla());
		pstm.setString(3, pregunta.getDescripcionPregunta());
		pstm.setInt(4, pregunta.getEstadoLogico());
		pstm.setInt(5, pregunta.getIdPregunta());
		pstm.executeUpdate();

		if (conn != null) {
			conn.close();
		}
	}

	public List<Respuesta> getRespuestas(String legajo, int idPantalla) throws SQLException {
		String query = "select * from pregunta p, respuesta r, autodiagnostico a   where p.idPantalla = ? "
				+ "and p.idPregunta = r.idPregunta  and r.idAutodiagnostico = a.idAutodiagnostico "
				+ "and a.legajo = ? order by a.fecha_autodiagnostico desc";
		Connection conn = null;
		PreparedStatement pstm = null;

		conn = Conexion.generarConexion();
		pstm = conn.prepareStatement(query);
		pstm.setInt(1, idPantalla);
		pstm.setString(2, legajo);

		ResultSet rs = pstm.executeQuery();
		List<Respuesta> respuestas = new ArrayList<>();
		Map<Integer, Respuesta> respuestasMap = new HashMap<>();
		int idPregunta = 0;
		while (rs.next()) {
			idPregunta = rs.getInt("idPregunta");
			if (respuestasMap.get(idPregunta) == null) {

				Respuesta respuesta = new Respuesta();
				respuesta.setVersion(rs.getInt("version"));
				respuesta.setTextoPregunta(rs.getString("textoPregunta"));
				respuesta.setIdAutodiagnostico(rs.getInt("idAutodiagnostico"));
				respuesta.setRespuestaPregunta(rs.getString("respuestaPregunta"));
				respuesta.setIdPregunta(rs.getInt("idPregunta"));
				respuestasMap.put(idPregunta, respuesta);
			}
		}
		respuestas = respuestasMap.values().stream().collect(Collectors.toList());

		if (conn != null) {
			conn.close();
		}
		return respuestas;

	}

}
