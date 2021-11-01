package com.ar.tbz.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Service;

import com.ar.tbz.conexion.Conexion;
import com.ar.tbz.domain.Respuesta;

@Service
public class RespuestaService {

	public void insert(Respuesta respuesta) throws SQLException {

		Connection conn = null;
		PreparedStatement pstm = null;

		conn = Conexion.generarConexion();
		String insert = "INSERT INTO respuesta(nroLegajo, idPregunta, respuestaPregunta, version, textoPregunta) values"
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

	public void insertRespuestaPerfil(Respuesta respuesta, String nroLegajo) throws SQLException {

		Connection conn = null;
		PreparedStatement pstm = null;

		conn = Conexion.generarConexion();
		String insert = "INSERT INTO respuestasPerfilEmpleado(nroLegajo, idPregunta, respuestaPregunta) values"
				+ "(?, ?, ?)";
		pstm = conn.prepareStatement(insert);
		pstm.setString(1, nroLegajo);
		pstm.setInt(2, respuesta.getIdPregunta());
		pstm.setString(3, respuesta.getRespuestaPregunta());
		pstm.executeUpdate();
		conn.close();
	}

	public void updateRespuestaPerfil(Respuesta respuesta, String nroLegajo) throws SQLException {

		Connection conn = null;
		PreparedStatement pstm = null;

		conn = Conexion.generarConexion();

		Respuesta resDB = findRespuestaPerfilEmpleadoByNroLegajoIdPregunta(nroLegajo, respuesta.getIdPregunta());
		
		if (resDB == null) {

			String insert = "INSERT INTO respuestasPerfilEmpleado(nroLegajo, idPregunta, respuestaPregunta) values"
					+ "(?, ?, ?)";
			pstm = conn.prepareStatement(insert);
			pstm.setString(1, nroLegajo);
			pstm.setInt(2, respuesta.getIdPregunta());
			pstm.setString(3, respuesta.getRespuestaPregunta());

		} else {

			String update = "UPDATE respuestasPerfilEmpleado SET respuestaPregunta = ? where nroLegajo = ? and idPregunta = ?";
			pstm = conn.prepareStatement(update);
			pstm.setString(1, respuesta.getRespuestaPregunta());
			pstm.setString(2, nroLegajo);
			pstm.setInt(3, respuesta.getIdPregunta());
		}

		pstm.executeUpdate();
		conn.close();
	}

	public Respuesta findRespuestaPerfilEmpleadoById(int id) throws SQLException {
		Connection conn = null;
		PreparedStatement pstm = null;

		conn = Conexion.generarConexion();
		String findById = "select * from respuestasPerfilEmpleado where idRespuestaPerfil = ?";
		pstm = conn.prepareStatement(findById);
		pstm.setInt(1, id);
		ResultSet rs = pstm.executeQuery();
		Respuesta res = null;
		if (rs.next()) {
			res = new Respuesta();
			res.setId(rs.getInt("idRespuestaPerfil"));
			res.setIdPregunta(rs.getInt("idPregunta"));
			res.setRespuestaPregunta(rs.getString("respuestaPregunta"));
		}
		conn.close();
		return res;
	}
	
	public Respuesta findRespuestaPerfilEmpleadoByNroLegajoIdPregunta(String nroLegajo, int idPregunta) throws SQLException {
		Connection conn = null;
		PreparedStatement pstm = null;

		conn = Conexion.generarConexion();
		String findById = "select * from respuestasPerfilEmpleado where nroLegajo = ? and idPregunta = ?";
		pstm = conn.prepareStatement(findById);
		pstm.setString(1, nroLegajo);
		pstm.setInt(2, idPregunta);
		ResultSet rs = pstm.executeQuery();
		Respuesta res = null;
		if (rs.next()) {
			res = new Respuesta();
			res.setId(rs.getInt("idRespuestaPerfil"));
			res.setIdPregunta(rs.getInt("idPregunta"));
			res.setRespuestaPregunta(rs.getString("respuestaPregunta"));
		}
		conn.close();
		return res;
	}

}