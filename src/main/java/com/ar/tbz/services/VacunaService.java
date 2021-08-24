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
import com.ar.tbz.domain.Vacuna;

@Service
public class VacunaService {

	private static Log log = LogFactory.getLog(VacunaService.class);

	// ------------------------------------------- lugares de acceso
	public List<Vacuna> findAll() throws Exception {

		log.info("Entra recupera Vacunas ");

		List<Vacuna> vacunas = new ArrayList<Vacuna>();
		Vacuna vacuna = null;

		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {

			conn = Conexion.generarConexion();

			String query = "SELECT * from ELEA_AUTODIAGNOSTICO.dbo.vacunas";
			pstm = conn.prepareStatement(query);
			rs = pstm.executeQuery();
			while (rs.next()) {
				vacuna = new Vacuna();
				vacuna.setIdVacuna(rs.getInt("idVacuna"));
				vacuna.setDescripcionVacuna(rs.getString("descripcionVacuna"));
				vacuna.setEstadoLogico(rs.getInt("estadoLogico"));

				vacunas.add(vacuna);
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
		return vacunas;

	} // end method recuperarLugaresDeAcceso

	public Vacuna findById(Integer idVacuna) throws Exception {

		log.info("Entra recupera Vacuna ");

		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			conn = Conexion.generarConexion();

			String query = "SELECT * from ELEA_AUTODIAGNOSTICO.dbo.vacunas el  where  el.idVacuna = ?";
			pstm = conn.prepareStatement(query);
			pstm.setInt(1, idVacuna);
			rs = pstm.executeQuery();
			Vacuna vacuna = null;
			if (rs.next()) {
				vacuna = new Vacuna(rs.getInt("idVacuna"), rs.getString("descripcionVacuna"),
						rs.getInt("estadoLogico"));
			}
			return vacuna;

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

	} // end method recuperarDniLegadjo

	public void insertar(Vacuna vacuna) throws Exception {
		Connection conn = null;
		try {
			conn = Conexion.generarConexion();
			String query = "INSERT INTO ELEA_AUTODIAGNOSTICO.dbo.vacunas (descripcionVacuna, estadoLogico) values ( ?, 1)";

			PreparedStatement pstm = conn.prepareStatement(query);
			pstm.setString(1, vacuna.getDescripcionVacuna());
			pstm.setInt(2, vacuna.getEstadoLogico());
			pstm.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					throw new Exception(e2);
				}
			}
		}
	}

	public void modificar(Vacuna lugar) throws Exception {
		Connection conn = null;
		try {
			conn = Conexion.generarConexion();
			String query = "UPDATE ELEA_AUTODIAGNOSTICO.dbo.vacunas SET  descripcionVacuna = ?, estadoLogico = ?  where idVacuna = ?";

			PreparedStatement pstm = conn.prepareStatement(query);
			pstm.setString(1, lugar.getDescripcionVacuna());
			pstm.setInt(2, lugar.getEstadoLogico());
			pstm.setInt(3, lugar.getIdVacuna());
			pstm.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					throw new Exception(e2);
				}
			}
		}
	}

	public void borrar(int idVacuna) throws Exception {
		Connection conn = null;
		try {
			conn = Conexion.generarConexion();
			String query = "UPDATE ELEA_AUTODIAGNOSTICO.dbo.vacunas SET estadoLogico = 0 where idVacuna = ?";

			PreparedStatement pstm = conn.prepareStatement(query);
			pstm.setInt(1, idVacuna);
			pstm.executeUpdate();

		} catch (SQLException e) {
			throw new Exception(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e2) {
					throw new Exception(e2);
				}
			}
		}
	}
}