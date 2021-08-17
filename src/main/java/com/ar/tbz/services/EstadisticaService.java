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
import com.ar.tbz.domain.Estadistica;
import com.ar.tbz.domain.Parametro;

@Service
public class EstadisticaService {

	private static Log log = LogFactory.getLog(EstadisticaService.class);

	public Estadistica obtenerEstadistica() throws Exception {
		Connection conn = null;
		Estadistica estadistica = null;
		try {
			conn = Conexion.generarConexion();

			String queryActivos = "SELECT COUNT(idUsuario) AS cantidad FROM empleadosActivos ";
			int empleadosActivos = getCount(conn, queryActivos);
			String queryAutodiag = "SELECT COUNT(idAutodiagnostico) AS cantidad FROM autodiagnostico WHERE nroLegajo!=0 ";
			int autodiag = getCount(conn, queryAutodiag);
			String queryEmpHabilitados = "SELECT COUNT(idAutodiagnostico) AS cantidad FROM autodiagnostico WHERE nroLegajo!=0 AND resultado=1";
			int empHabilitados = getCount(conn, queryEmpHabilitados);
			String queryEmpNoHabilitados = "SELECT COUNT(idAutodiagnostico) AS cantidad FROM autodiagnostico WHERE nroLegajo!=0 AND resultado=0";
			int empNoHabilitados = getCount(conn, queryEmpNoHabilitados);
			String queryEmpEstrechos = "SELECT COUNT(idAutodiagnostico) AS cantidad FROM autodiagnostico WHERE nroLegajo!=0 AND estadoContactoEstrecho=1";
			int empEstrechos = getCount(conn, queryEmpEstrechos);
			String queryEmpSintomas = "SELECT COUNT(idAutodiagnostico) AS cantidad FROM autodiagnostico WHERE nroLegajo!=0 AND estadoSintomas=1";
			int empSintomas = getCount(conn, queryEmpSintomas);
			String queryPctAutodiagPorEmpAct = "SELECT (CAST((SELECT COUNT (DISTINCT nroLegajo) FROM autodiagnostico WHERE nroLegajo!=0) AS DECIMAL) / CAST((SELECT COUNT (DISTINCT nroLegajo) FROM empleadosActivos) AS DECIMAL)) AS resultado;";
			double pctAutodiagPorEmpAct = getCountDouble(conn, queryPctAutodiagPorEmpAct);

			estadistica = new Estadistica(empleadosActivos, autodiag, empHabilitados, empNoHabilitados, empEstrechos,
					empSintomas, pctAutodiagPorEmpAct);

		} catch (

		SQLException e) {
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
		return estadistica;
	}

	private int getCount(Connection conn, String query) throws SQLException {
		PreparedStatement pstm = conn.prepareStatement(query);
		ResultSet rs = pstm.executeQuery();
		int count = 0;
		while (rs.next()) {
			count = rs.getInt(1);
		}
		return count;
	}

	private double getCountDouble(Connection conn, String query) throws SQLException {
		PreparedStatement pstm = conn.prepareStatement(query);
		ResultSet rs = pstm.executeQuery();
		double count = 0;
		while (rs.next()) {
			count = rs.getDouble(1);
		}
		return count;
	}

	public List<Parametro> obtenerParametros() throws Exception {
		Connection conn = null;
		List<Parametro> parametros = new ArrayList<Parametro>();
		try {
			conn = Conexion.generarConexion();

			String query = "SELECT * FROM Parametria ";

			PreparedStatement pstm = conn.prepareStatement(query);
			ResultSet rs = pstm.executeQuery();
			Parametro param;
			while (rs.next()) {
				param = new Parametro();
				param.setDescripcionParametro(rs.getString("descripcionParametro"));
				param.setValorParametro(rs.getString("valorParametro"));
				param.setIdParametro(rs.getInt("idParametro"));
				parametros.add(param);
			}

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
		return parametros;
	}

	public void insertarParametro(Parametro param) throws Exception {
		Connection conn = null;
		try {
			conn = Conexion.generarConexion();
			String query = "INSERT INTO Parametria (descripcionParametro, valorParametro) values (?, ?)";

			PreparedStatement pstm = conn.prepareStatement(query);
			pstm.setString(1, param.getDescripcionParametro());
			pstm.setString(2, param.getValorParametro());
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

	public void modificarParametro(Parametro param) throws Exception {
		Connection conn = null;
		try {
			conn = Conexion.generarConexion();
			String query = "UPDATE Parametria SET  descripcionParametro = ? , valorParametro ? where idParametro = ?";

			PreparedStatement pstm = conn.prepareStatement(query);
			pstm.setInt(1, param.getIdParametro());
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

	public void borrarParametro(int idParam) throws Exception {
		Connection conn = null;
		try {
			conn = Conexion.generarConexion();
			String query = "DELETE FROM Parametria where idParametro = ?";

			PreparedStatement pstm = conn.prepareStatement(query);
			pstm.setInt(1, idParam);
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
