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
import com.ar.tbz.domain.Respuesta;
import com.ar.tbz.util.DateUtil;

@Service
public class BusquedaService {

	private static Log log = LogFactory.getLog(BusquedaService.class);

	public List<Autodiagnostico> buscarDiagnostico(Map<String, String> form, String min, String max) throws Exception {
		String query = "SELECT a.*, l.* from ELEA_AUTODIAGNOSTICO.dbo.autodiagnostico a, ELEA_AUTODIAGNOSTICO.dbo.lugarAcceso l "
				+ "  where 1=1 and a.idLugarAcceso = l.idLugarAcceso ";
		// ROWNUM BETWEEN " + min + " AND " + max +
		StringBuilder sb = new StringBuilder(query);
		Connection conn = null;
		List<Autodiagnostico> resultado = new ArrayList<Autodiagnostico>();
		try {
			for (Map.Entry<String, String> entry : form.entrySet()) {
				if (!entry.getKey().equals("pagina")) {
					if (entry.getKey().equals("nroLegajo") && entry.getValue().equals("-1")) {
						sb.append(" and " + entry.getKey() + " > 0 ");
					} else if (entry.getKey().equals("fecha_autodiagnostico")) {
						sb.append(" and " + entry.getKey() + " >= " + entry.getValue());
					} else if (entry.getKey().equals("fecha_autodiagnostico_hasta")) {
						sb.append(" and fecha_autodiagnostico <= " + entry.getValue());
					} else if (entry.getKey().equals("apellido")) {
						sb.append(" and " + entry.getKey() + " LIKE '%" + entry.getValue()
								+ "%' COLLATE Latin1_general_CI_AI");
					} else if (entry.getKey().equals("bloqueado")) {
						String now = DateUtil.currentDateStr();
						sb.append(" and resultado=0 and fecha_hasta_resultado > '" + now + "' ");
					} else {
						sb.append(" and a." + entry.getKey() + " = " + entry.getValue());
					}
				}
			}

			sb.append(" order by fecha_autodiagnostico desc");

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
				nuevoAutoD.setFecha_autodiagnostico(rs.getTimestamp("fecha_autodiagnostico"));
				nuevoAutoD.setFecha_hasta_resultado(rs.getTimestamp("fecha_hasta_resultado"));
				nuevoAutoD.setComentario(rs.getString("comentario"));
				nuevoAutoD.setModificadoPor(rs.getInt("modificadoPor"));
				nuevoAutoD.setModificadoEn(rs.getTimestamp("modificadoEn"));
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

	public List<Respuesta> buscarRespuestas(Integer idAutodiagnostico) throws SQLException {
		String query = "SELECT r.*, p.* from ELEA_AUTODIAGNOSTICO.dbo.respuestas r, ELEA_AUTODIAGNOSTICO.dbo.preguntas p "
				+ "  where r.idPregunta = p.idPregunta and r.idAutodiagnostico = ? order by r.idPregunta";
		List<Respuesta> respuestas = new ArrayList<Respuesta>();
		Connection conn = Conexion.generarConexion();
		PreparedStatement pstm = conn.prepareStatement(query);
		pstm.setInt(1, idAutodiagnostico);
		ResultSet rs = pstm.executeQuery();
		while (rs.next()) {
			Respuesta respuesta = new Respuesta();
			respuesta.setIdAutodiagnostico(rs.getInt("idAutodiagnostico"));
			respuesta.setIdPregunta(rs.getInt("idPregunta"));
			respuesta.setRespuestaPregunta(rs.getString("respuestaPregunta"));
			respuesta.setTextoPregunta(rs.getString("descripcionPregunta"));
			respuestas.add(respuesta);
		}

		conn.close();
		return respuestas;
	}

	public Autodiagnostico buscarAutodiagnostico(int nroLegajo) throws Exception {
		String query = "SELECT * from ELEA_AUTODIAGNOSTICO.dbo.autodiagnostico a "
				+ "  where a.nroLegajo = ? order by a.idAutodiagnostico desc";
		StringBuilder sb = new StringBuilder(query);
		Connection conn = null;
		Autodiagnostico nuevoAutoD = null;
		try {

			conn = Conexion.generarConexion();
			log.info("Query_statement: " + sb.toString());
			PreparedStatement pstm = conn.prepareStatement(sb.toString());
			pstm.setInt(1, nroLegajo);
			ResultSet rs = pstm.executeQuery();
			if (rs.next()) {
				nuevoAutoD = new Autodiagnostico(rs.getInt("idAutodiagnostico"), rs.getString("nroLegajo"),
						rs.getString("dni"), rs.getString("nombre"), rs.getString("apellido"), rs.getString("telefono"),
						rs.getInt("idLugarAcceso"), rs.getInt("resultado"));
				nuevoAutoD.setEmpresa(rs.getString("empresa"));
				nuevoAutoD.setEmailUsuario(rs.getString("emailUsuario"));
				nuevoAutoD.setEmailLaboral(rs.getString("emailLaboral"));
				nuevoAutoD.setEstadoSintomas(rs.getInt("estadoSintomas"));
				nuevoAutoD.setEstadoContactoEstrecho(rs.getInt("estadoContactoEstrecho"));
				nuevoAutoD.setEstadoAntecedentes(rs.getInt("estadoAntecedentes"));
				nuevoAutoD.setFecha_autodiagnostico(rs.getTimestamp("fecha_autodiagnostico"));
				nuevoAutoD.setFecha_hasta_resultado(rs.getTimestamp("fecha_hasta_resultado"));
				nuevoAutoD.setComentario(rs.getString("comentario"));
				nuevoAutoD.setModificadoPor(rs.getInt("modificadoPor"));
				nuevoAutoD.setModificadoEn(rs.getTimestamp("modificadoEn"));
//				nuevoAutoD.setDescripcionLugarAcceso(rs.getString("descripcionLugarAcceso"));
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
		return nuevoAutoD;
	}
	
	
	// buscar autodiagnosticos con respuestas
	public List<Autodiagnostico> buscarDiagnosticoConRespuestas(Map<String, String> form, String min, String max) throws Exception {
		String query = "SELECT a.*, l.* from ELEA_AUTODIAGNOSTICO.dbo.autodiagnostico a, ELEA_AUTODIAGNOSTICO.dbo.lugarAcceso l "
				+ "  where 1=1 and a.idLugarAcceso = l.idLugarAcceso ";
		// ROWNUM BETWEEN " + min + " AND " + max +
		StringBuilder sb = new StringBuilder(query);
		Connection conn = null;
		List<Autodiagnostico> resultado = new ArrayList<Autodiagnostico>();
		try {
			for (Map.Entry<String, String> entry : form.entrySet()) {
				if (!entry.getKey().equals("pagina")) {
					if (entry.getKey().equals("nroLegajo") && entry.getValue().equals("-1")) {
						sb.append(" and " + entry.getKey() + " > 0 ");
					} else if (entry.getKey().equals("fecha_autodiagnostico")) {
						sb.append(" and " + entry.getKey() + " >= " + entry.getValue());
					} else if (entry.getKey().equals("fecha_autodiagnostico_hasta")) {
						sb.append(" and fecha_autodiagnostico <= " + entry.getValue());
					} else if (entry.getKey().equals("apellido")) {
						sb.append(" and " + entry.getKey() + " LIKE '%" + entry.getValue()
								+ "%' COLLATE Latin1_general_CI_AI");
					} else if (entry.getKey().equals("bloqueado")) {
						String now = DateUtil.currentDateStr();
						sb.append(" and resultado=0 and fecha_hasta_resultado > '" + now + "' ");
					} else {
						sb.append(" and a." + entry.getKey() + " = " + entry.getValue());
					}
				}
			}

			sb.append(" order by fecha_autodiagnostico desc");

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
				nuevoAutoD.setFecha_autodiagnostico(rs.getTimestamp("fecha_autodiagnostico"));
				nuevoAutoD.setFecha_hasta_resultado(rs.getTimestamp("fecha_hasta_resultado"));
				nuevoAutoD.setComentario(rs.getString("comentario"));
				nuevoAutoD.setModificadoPor(rs.getInt("modificadoPor"));
				nuevoAutoD.setModificadoEn(rs.getTimestamp("modificadoEn"));
				nuevoAutoD.setDescripcionLugarAcceso(rs.getString("descripcionLugarAcceso"));
				
				
				List<Respuesta> respuestas = new ArrayList<>();
				String queryRtas = "SELECT * FROM ELEA_AUTODIAGNOSTICO.dbo.respuestas WHERE idAutodiagnostico = " + rs.getInt("idAutodiagnostico");
				pstm = conn.prepareStatement(queryRtas);
				ResultSet rsRtas = pstm.executeQuery();
				while(rsRtas.next()) {
					Respuesta rta = new Respuesta();
					rta.setId(rsRtas.getInt("id"));
					rta.setIdAutodiagnostico(rsRtas.getInt("idAutodiagnostico"));
					rta.setIdPregunta(rsRtas.getInt("idPregunta"));
					rta.setRespuestaPregunta(rsRtas.getString("respuestaPregunta"));
					
					respuestas.add(rta);
				}
				
				nuevoAutoD.setRespuestas(respuestas);
				
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
