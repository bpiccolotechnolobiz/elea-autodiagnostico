package com.ar.tbz.conexion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Date;
import java.util.Properties;

public class Conexion {
	private static final String SERVER_NAME = "172.16.0.50";

	public static Connection generarConexion() {

		Connection connect = null;

		try {

			System.out.println("Entra conexion" + new Date());

			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

			System.out.println("Sale Class.forName " + new Date());

			// connect = DriverManager.getConnection("jdbc:mysql://"+ SERVER_NAME +":" +
			// PORT_NUMBER + "/" + DB_NAME + "?user=" + USER_NAME + "&password=" +
			// PASSWORD);

			Properties properties = new Properties();
			properties.load(
					new FileInputStream(new File("/aplicaciones/autodiagnostico/autodiagnostico-back.properties")));

			String serverName = properties.getProperty("database.url");
			String user = properties.getProperty("database.user");
			String pass = properties.getProperty("database.pass");
			String database = properties.getProperty("database.database");

			String dbURL = "jdbc:sqlserver://" + serverName + ";databaseName=" + database;

			System.out.println("Entra al connect to base de datos " + new Date());

			connect = DriverManager.getConnection(dbURL, user, pass);

			System.out.println("Sale al connect to base de datos " + new Date());

			if (connect == null) {

				System.out.println("ERROR  al connect to base de datos " + new Date());

				throw new Exception("Error de conexion a la base de datos");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return connect;
	}

}
