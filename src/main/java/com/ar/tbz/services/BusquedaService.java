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
		String query = "SELECT a.*, l.* from ELEA_AUTODIAGNOSTICO.dbo.autodiagnostico a, ELEA_AUTODIAGNOSTICO.dbo.lugarAcceso l "
				+ "  where 1 = 1 " + " and a.idLugarAcceso = l.idLugarAcceso ";
		StringBuilder sb = new StringBuilder(query);
		Connection conn = null;
		List<Autodiagnostico> resultado = new ArrayList<Autodiagnostico>();
		try {
			for (Map.Entry<String, String> entry : form.entrySet()) {
				if (!entry.getKey().equals("pagina")) {
					if (entry.getKey().equals("nroLegajo") && entry.getValue().equals("-1")) {
						sb.append(" and " + entry.getKey() + " > 0 ");
					} else if (entry.getKey().equals("fecha_autodiagnostico")) {
						sb.append(" and " + entry.getKey() + " between " + entry.getValue());
					} else if (entry.getKey().equals("fecha_autodiagnostico_hasta")) {
						sb.append(" and " + entry.getValue());
					} else {
						sb.append(" and a." + entry.getKey() + " = " + entry.getValue());
					}
				}
			}

			conn = Conexion.generarConexion();
			log.info("Query_statement: " + sb.toString());
			PreparedStatement pstm = conn.prepareStatement(sb.toString());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				Autodiagnostico nuevoAutoD = new Autodiagnostico(rs.getInt("idAutodiagnostico"),
						rs.getString("nroLegajo"), rs.getString("dni"), rs.getString("nombre"),
						rs.getString("apellido"), rs.getString("telefono"), rs.getInt("idLugarAcceso"),
						rs.getInt("resultado"));
				nuevoAutoD.setEmpresa(rs.getString("empresa"));
				nuevoAutoD.setEmailUsuario(rs.getString("emailUsuario"));
				nuevoAutoD.setEmailLaboral(rs.getString("emailLaboral"));
				nuevoAutoD.setEstadoSintomas(rs.getInt("estadoSintomas"));
				nuevoAutoD.setEstadoContactoEstrecho(rs.getInt("estadoContactoEstrecho"));
				nuevoAutoD.setEstadoAntecedentes(rs.getInt("estadoAntecedentes"));
				nuevoAutoD.setFecha_autodiagnostico(rs.getDate("fecha_autodiagnostico"));
				nuevoAutoD.setFecha_hasta_resultado(rs.getDate("fecha_hasta_resultado"));
				nuevoAutoD.setComentario(rs.getString("comentario"));
				nuevoAutoD.setModificadoPor(rs.getInt("modificadoPor"));
				nuevoAutoD.setModificadoEn(rs.getDate("modificadoEn"));
				nuevoAutoD.setDescripcionLugarAcceso(rs.getString("descripcionLugarAcceso"));
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
