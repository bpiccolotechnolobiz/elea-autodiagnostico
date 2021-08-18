package com.ar.tbz.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.ar.tbz.conexion.Conexion;
import com.ar.tbz.domain.LugarAcceso;

@Service
public class LugarAccesoService {

	private static Log log = LogFactory.getLog(LugarAccesoService.class);

	// ------------------------------------------- lugares de acceso
	public List<LugarAcceso> recuperarLugaresDeAcceso() throws Exception {

		System.out.println("Entra recupera Lugares de Acceso ");

		List<LugarAcceso> listLugaresAcceso = new ArrayList<LugarAcceso>();
		LugarAcceso lugarAcceso = null;

		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
		boolean status = false;
		try {

			conn = Conexion.generarConexion();

			String query = "SELECT * from ELEA_AUTODIAGNOSTICO.dbo.lugarAcceso";
			pstm = conn.prepareStatement(query);
			rs = pstm.executeQuery();
			while (rs.next()) {
				lugarAcceso = new LugarAcceso();
				lugarAcceso.setIdLugarAcceso(rs.getInt("idLugarAcceso"));
				lugarAcceso.setDescripcionLugarAcceso(rs.getString("descripcionLugarAcceso"));

				listLugaresAcceso.add(lugarAcceso);
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
		return listLugaresAcceso;

	} // end method recuperarLugaresDeAcceso

	public String recuperarLugarDeAcceso(Integer idAcceso) throws Exception {

		System.out.println("Entra recupera Lugar de Acceso ");

		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
		boolean status = false;
		try {
			conn = Conexion.generarConexion();

			String query = "SELECT * from ELEA_AUTODIAGNOSTICO.dbo.lugarAcceso  el  where  el.idLugarAcceso = ?";
			pstm = conn.prepareStatement(query);
			pstm.setInt(1, idAcceso);
			rs = pstm.executeQuery();
			while (rs.next()) {
				return rs.getString("descripcionLugarAcceso");
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
		return null;

	} // end method recuperarDniLegadjo

	public void insertar(LugarAcceso lugar) throws Exception {
		Connection conn = null;
		try {
			conn = Conexion.generarConexion();
			String query = "INSERT INTO ELEA_AUTODIAGNOSTICO.dbo.lugarAcceso (descripcionLugarAcceso) values ( ?)";

			PreparedStatement pstm = conn.prepareStatement(query);
			pstm.setString(1, lugar.getDescripcionLugarAcceso());
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

	public void modificar(LugarAcceso lugar) throws Exception {
		Connection conn = null;
		try {
			conn = Conexion.generarConexion();
			String query = "UPDATE ELEA_AUTODIAGNOSTICO.dbo.lugarAcceso SET  descripcionLugarAcceso = ?  where idLugarAcceso = ?";

			PreparedStatement pstm = conn.prepareStatement(query);
			pstm.setString(1, lugar.getDescripcionLugarAcceso());
			pstm.setInt(2, lugar.getIdLugarAcceso());
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

	public void borrar(int idAcceso) throws Exception {
		Connection conn = null;
		try {
			conn = Conexion.generarConexion();
			String query = "DELETE FROM LEA_AUTODIAGNOSTICO.dbo.lugarAcceso where idLugarAcceso = ?";

			PreparedStatement pstm = conn.prepareStatement(query);
			pstm.setInt(1, idAcceso);
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