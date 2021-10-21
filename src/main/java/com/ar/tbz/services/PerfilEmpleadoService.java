package com.ar.tbz.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ar.tbz.conexion.Conexion;
import com.ar.tbz.domain.PerfilEmpleado;
import com.ar.tbz.domain.Pregunta;
import com.ar.tbz.domain.PreguntaRespuesta;
import com.ar.tbz.domain.Respuesta;

@Service
public class PerfilEmpleadoService {
	private static Log log = LogFactory.getLog(PreguntaService.class);
	@Autowired PreguntaService preguntaService;

	public PerfilEmpleado getPerfilEmpleadoByNroLegajo(String nroLegajo) throws Exception {

		log.info("Entra recupera perfil de empleado ");
		
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		try {
			conn = Conexion.generarConexion();

			String query = "SELECT * from ELEA_AUTODIAGNOSTICO.dbo.perfilEmpleados pe  where  pe.nroLegajo = ?";
			pstm = conn.prepareStatement(query);
			pstm.setString(1, nroLegajo);
			rs = pstm.executeQuery();
			
			PerfilEmpleado perfil = null;
			
			if(rs.next()) {
				perfil = new PerfilEmpleado();
//				perfil.setIdPerfil(rs.getInt("idPerfil"));
				perfil.setNroLegajo(rs.getString("nroLegajo"));
				perfil.setEmailUsuario(rs.getString("emailUsuario"));
				perfil.setEstadoLogico(rs.getInt("estadoLogico"));
			}
			
			if (perfil != null) {
				List<Pregunta> preguntas = preguntaService.findAll();
				List<Pregunta> preguntasActivas = preguntas.stream()
						.filter(x -> x.getEstadoLogico() == 1 && (x.getIdPantalla() == 4 || x.getIdPantalla() == 5)).collect(Collectors.toList());
				
				List<Respuesta> respuestasPerfilEmpleado = preguntaService.getRespuestasPerfilEmpleado(nroLegajo);
				
				List<PreguntaRespuesta> list = new ArrayList<PreguntaRespuesta>();
				for (Pregunta preg : preguntasActivas) {
					PreguntaRespuesta pr = new PreguntaRespuesta();
					Optional<Respuesta> respuestaOpt = respuestasPerfilEmpleado.stream()
							.filter(x -> x.getIdPregunta() == preg.getIdPregunta()).findAny();
					if (respuestaOpt.isPresent()) {
						pr.setRespuesta(respuestaOpt.get());
					}
					pr.setPregunta(preg);
					list.add(pr);
				}
				perfil.setPreguntasRespuestas(list);
			}
			
			
			return perfil;
		} catch (Exception e) {
			throw new Exception(e);
		}finally {
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
