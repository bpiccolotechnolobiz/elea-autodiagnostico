package com.ar.tbz.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.ar.tbz.conexion.Conexion;
import com.ar.tbz.domain.Autodiagnostico;

@Service
public class BusquedaService {

	private static Log log = LogFactory.getLog(BusquedaService.class);

	public List<Autodiagnostico> buscarDiagnostico(Map<String, String> form) throws Exception {
		String query = "SELECT * from ELEA_AUTODIAGNOSTICO.dbo.autodiagnostico where 1 = 1 "
//				+ " fecha_autodiagnostico = ? and nroLegajo = ?"
		;
		StringBuilder sb = new StringBuilder(query);
		Connection conn = null;
		List<Autodiagnostico> resultado = new ArrayList<Autodiagnostico>();
		try {
			for (Map.Entry<String, String> entry : form.entrySet()) {
				if (entry.getKey().equals("nroLegajo") && entry.getValue().equals("-1")) {
					sb.append(" and " + entry.getKey() + " > 0 ");
				} else {
					sb.append(" and " + entry.getKey() + " = " + entry.getValue());
				}
			}

			conn = Conexion.generarConexion();
			log.info("Query_statement: " + query);
			PreparedStatement pstm = conn.prepareStatement(query);
			ResultSet rs = pstm.executeQuery(sb.toString());
			while (rs.next()) {
				Autodiagnostico nuevoAutoD = new Autodiagnostico(rs.getInt(1), rs.getString(2), rs.getString(3),
						rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(9), rs.getBoolean((10)));
				resultado.add(nuevoAutoD);
			}

		} catch (SQLException e) {
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
		return resultado;
	}
}
