package com.ar.tbz.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.ar.tbz.conexion.Conexion;
import com.ar.tbz.domain.Legajo;

@Service
public class EmpleadoService {

	private static Log log = LogFactory.getLog(EmpleadoService.class);

	public Legajo findByLegajo(String nroLegajo) throws Exception {
		String query = "SELECT * from ELEA_AUTODIAGNOSTICO.dbo.empleadosActivos  lg  where  lg.nroLegajo = ?";
		return findBy(nroLegajo, query);
	} // end method recuperarDniLegadjo

	public Legajo findByDni(String dni) throws Exception {
		String query = "SELECT * from ELEA_AUTODIAGNOSTICO.dbo.empleadosActivos  lg  where  lg.dni = ?";
		return findBy(dni, query);
	}

	private Legajo findBy(String numero, String query) throws Exception {
		System.out.println("Entra recuperarDniLegajo ");
		List<Legajo> legajos = new ArrayList<Legajo>();
		Legajo legajo = null;
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			conn = Conexion.generarConexion();
			System.out.println("Sale -->  recuperarDniLegajo :: generarConexion ");
			pstm = conn.prepareStatement(query);
			pstm.setString(1, numero);
			rs = pstm.executeQuery();
			System.out.println("Sale -->  recuperarDniLegajo :: ejecutaQuery ");
			while (rs.next()) {
				legajo = new Legajo();
				legajo.setNroLegajo(rs.getString("nrolegajo"));
				legajo.setDni(rs.getString("dni"));
				legajo.setNombre(rs.getString("nombre"));
				legajo.setApellido(rs.getString("apellido"));
				legajo.setTelefono((String) rs.getString("telefono"));
				legajo.setEmailLaboral((String) rs.getString("emailLaboral"));
				legajos.add(legajo);
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
		System.out.println("\n\n\n --------------------------------------------->  Retorna legajo recuperado  -->  "
				+ " -- " + legajo);
		return legajo;

	}

}
