package com.flabser.dataengine;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import com.flabser.server.Server;

public class DatabaseUtil{

	public static void errorPrint(Throwable e) {
		if (e instanceof SQLException) {
			SQLException sqle = (SQLException) e;
			SQLExceptionPrint(sqle);
		} else {
			Server.logger.errorLogEntry(e.toString());
			e.printStackTrace();
		}
	}

	public static void debugErrorPrint(Throwable e) {
		if (e instanceof SQLException) {
			SQLException sqle = (SQLException) e;
			SQLExceptionPrintDebug(sqle);
		} else {
			Server.logger.errorLogEntry(e.toString());
			e.printStackTrace();
		}
	}


	public static void errorPrint(String DbID, Throwable e) {
		Server.logger.errorLogEntry(DbID);
		if (e instanceof SQLException) {
			SQLException sqle = (SQLException) e;
			SQLExceptionPrintDebug(sqle);
		} else {
			Server.logger.errorLogEntry(e.toString());
			e.printStackTrace();
		}
	}

	public static void errorPrint(Throwable e, String sql) {
		Server.logger.errorLogEntry(sql);
		if (e instanceof SQLException) {
			SQLException sqle = (SQLException) e;
			SQLExceptionPrintDebug(sqle);
		} else {
			Server.logger.errorLogEntry(e.toString());
			e.printStackTrace();
		}
	}

	public static void SQLExceptionPrint(SQLException sqle) {
		while (sqle != null) {
			Server.logger.errorLogEntry("SQLState:   " + sqle.getSQLState());
			Server.logger.errorLogEntry("Severity: " + sqle.getErrorCode());
			Server.logger.errorLogEntry("Message:  " + sqle.getMessage());
			//Server.logger.errorLogEntry(sqle);
			sqle = sqle.getNextException();
		}
	}

	public static void SQLExceptionPrintDebug(SQLException sqle) {
		while (sqle != null) {
			Server.logger.errorLogEntry("SQLState:   " + sqle.getSQLState());
			Server.logger.errorLogEntry("Severity: " + sqle.getErrorCode());
			Server.logger.errorLogEntry("Message:  " + sqle.getMessage());
			Server.logger.errorLogEntry(sqle);
			sqle.printStackTrace();
			sqle = sqle.getNextException();
		}
	}

	public static boolean hasTable(String tableName, Connection conn) throws SQLException {
		try {
			DatabaseMetaData metaData = null;
			metaData = conn.getMetaData();
			String[] tables = {"TABLE"};
			ResultSet rs = metaData.getTables(null, null, null, tables);
			while (rs.next()) {
				String table = rs.getString("TABLE_NAME");
				if (tableName.equalsIgnoreCase(table)) {
					return true;
				}
			}
			return false;
		} catch (Throwable e) {
			return false;
		}
	}

	public static boolean hasView(String viewName, Connection conn) throws SQLException {
		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM INFORMATION_SCHEMA.VIEWS where upper(table_name) = '" + viewName.toUpperCase() + "'");
			if (rs.next()) {
				return true;
			}
			return false;
		} catch (Throwable e) {
			e.printStackTrace();
			return false;

		}
	}

	public static boolean hasProcedureAndTriger(String name, Connection conn) throws SQLException {
		try {
			DatabaseMetaData metaData = null;
			metaData = conn.getMetaData();
			ResultSet rs = metaData.getProcedures(null, null, null);
			while (rs.next()) {
				String procedure = rs.getString("PROCEDURE_NAME");
				if (name.equalsIgnoreCase(procedure)) {
					return true;
				}
			}
			return false;
		} catch (Throwable e) {
			return false;
		}
	}

	public static boolean hasTrigger(String name, Connection conn) throws SQLException {
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery("select * from sys.triggers where name = '" + name + "'");
			if (rs.next()) {
				return true;
			}
			return false;
		} catch (Throwable e) {
			return false;
		}
	}


	public static String prepareListToQuery(Collection<String> elements) {
		StringBuffer result = new StringBuffer(1000);
		if (elements != null) {
			for (String element : elements) {
				result.append("'" + element + "',");
			}
			if (result.length() != 0) {
				result = result.deleteCharAt(result.length() - 1);
			}
		}
		return result.toString();
	}

	public static boolean hasFTIndex(Connection conn, String tableName) {
		String sql = "SELECT COUNT(*) FROM sys.fulltext_indexes where object_id = object_id('" + tableName + "')";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count > 0) {
					return true;
				} else {
					return false;
				}
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			DatabaseUtil.errorPrint(e, sql);
		}
		return false;
	}

	public static boolean hasFTCatalog(Connection conn, String catalogName) {
		String sql = "SELECT COUNT(*) FROM sys.fulltext_catalogs where name = '" + catalogName + "'";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count > 0) {
					return true;
				} else {
					return false;
				}
			}
			rs.close();
			st.close();
		} catch (SQLException e) {
			DatabaseUtil.errorPrint(e, sql);
		}
		return false;
	}

}
