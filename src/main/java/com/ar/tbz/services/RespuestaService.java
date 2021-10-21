package com.ar.tbz.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ar.tbz.conexion.Conexion;
import com.ar.tbz.domain.Respuesta;

public class RespuestaService {

	public void insert(Respuesta respuesta) throws SQLException {

		Connection conn = null;
		PreparedStatement pstm = null;

		conn = Conexion.generarConexion();
		String insert = "INSERT INTO respuesta(idAutodiagnostico, idPregunta, respuestaPregunta, version, textoPregunta) values"
				+ "(?, ?, ?, ?, ?)";
		pstm = conn.prepareStatement(insert);
		pstm.setInt(1, respuesta.getIdAutodiagnostico());
		pstm.setInt(2, respuesta.getIdPregunta());
		pstm.setString(3, respuesta.getRespuestaPregunta());
		pstm.setInt(4, respuesta.getVersion());
		pstm.setString(5, respuesta.getTextoPregunta());
		pstm.executeUpdate();
		conn.close();
	}

	public void update(Respuesta respuesta) throws SQLException {

		Connection conn = null;
		PreparedStatement pstm = null;

		conn = Conexion.generarConexion();
		String update = "UPDATE respuestas SET idAutodiagnostico = ?, idPregunta = ? , respuestaPregunta = ? , version = ?, textoPregunta = ? where id = ?";
		pstm = conn.prepareStatement(update);
		pstm.setInt(1, respuesta.getIdAutodiagnostico());
		pstm.setInt(2, respuesta.getIdPregunta());
		pstm.setString(3, respuesta.getRespuestaPregunta());
		pstm.setInt(4, respuesta.getVersion());
		pstm.setString(5, respuesta.getTextoPregunta());
		pstm.setInt(6, respuesta.getId());
		pstm.executeUpdate();
		conn.close();
	}

}
