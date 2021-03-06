package com.flabser.solutions.oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleJDBC {

	public static void main(String[] argv) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;

		}
		Connection connection = null;
		try {
			// String url = "jdbc:oracle:thin:@192.168.1.97:1521:XE";
			// connection = DriverManager.getConnection(url, "SYSTEM",
			// "aida22");
			String url = "jdbc:oracle:thin:@172.16.251.12:1521:dbserver";
			connection = DriverManager.getConnection(url, "SYSTEM", "Pa$$w0rd");
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}

		if (connection != null) {
			System.out.println("Connected..");
		}
	}

}
