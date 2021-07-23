package com.ar.tbz.conexion;

import java.sql.DriverManager;
import java.util.Date;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

public class Conexion {
	private static final String SERVER_NAME = "172.16.0.50";
//	private static final String SERVER_NAME = "172.16.0.53";
	private static final int PORT_NUMBER = 0;

	private static final String USER_NAME = "";
	private static final String PASSWORD = "";

	public static final String DB_NAME = "";

	public static Connection generarConexion() {

		Connection connect = null;

		try {

			System.out.println("Entra conexion" + new Date());

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			System.out.println("Sale Class.forName " + new Date());

			// connect = DriverManager.getConnection("jdbc:mysql://"+ SERVER_NAME +":" +
			// PORT_NUMBER + "/" + DB_NAME + "?user=" + USER_NAME + "&password=" +
			// PASSWORD);

			String dbURL = "jdbc:sqlserver://" + SERVER_NAME + ":" + PORT_NUMBER + ";databaseName=" + DB_NAME;

			System.out.println("Entra al connect to base de datos " + new Date());

			connect = DriverManager.getConnection(dbURL, USER_NAME, PASSWORD);

			System.out.println("Sale al connect to base de datos " + new Date());

			if (connect == null) {

				System.out.println("ERROR  al connect to base de datos " + new Date());

				throw new Exception("Error de conexion a la base de datos");
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return connect;
	}

}
