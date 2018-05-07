package com.dixonscarphone.webserver.shared;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

/* DB Singleton */
public class DB {

	private static DB db = null;
	private static Connection connection = null;

	private static final String LOGGER_NAME = "DB";
	private static final Logger LOGGER = Logger.getLogger(LOGGER_NAME);

	private DB() {
		try {
			InitialContext ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/mydb");
			connection = ds.getConnection();
		} catch (SQLException se) {
			LOGGER.error("DB ERROR: " + se.toString());
		} catch (NamingException ne) {
			LOGGER.error("DB ERROR: " + ne.toString());
		}
	}

	public static DB getInstance() {
		if (db == null) {
			db = new DB();
		}
		return db;
	}

	public static Connection getConnection() {
		getInstance();
		return connection;
	}

	public static void insertIntoTableMessage(String wsName, String wsResult) {
		String date = "datetime('now', 'localtime')";
		String sql = "INSERT OR REPLACE INTO message (date, ws_name, ws_result) VALUES (" + date + ", '" + wsName + "', '" + wsResult + "')";
		insert(sql);
	}

	public static void insertIntoTableMessage(String wsName, String wsResult, String text) {
		String date = "datetime('now', 'localtime')";
		String sql = "INSERT OR REPLACE INTO message (date, ws_name, ws_result, text) VALUES (" + date + ", '" + wsName + "', '" + wsResult + "', '"
				+ text.replace("'", "''") + "')";
		insert(sql);
	}

	public static void insert(String sql) {
		try {
			try {
				Connection connection = getConnection();
				Statement statement = connection.createStatement();
				statement.executeUpdate(sql);
				statement.close();
				// connection.close();
			} catch (SQLException e) {
				LOGGER.error("DB ERROR: " + e.getMessage());
			}
		} catch (Exception e) {
			LOGGER.error("FATAL DB ERROR: " + e.getMessage());
		}
	}
	
	public static List<String[]> selectAndReturnAsList(String sql) {
		
		List<String[]> result = new ArrayList<String[]>();
		
		try {
			
			Connection connection = getConnection();
			Statement statement	= connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			
			int columnCount = rs.getMetaData().getColumnCount();
			
			while(rs.next()) {
			    String[] row = new String[columnCount];
			    for (int i = 0; i < columnCount; i++)
			    {
			       row[i] = rs.getString(i + 1);
			    }
			    result.add(row);
			}
			
			statement.close();
			//connection.close();		
			
		} catch (SQLException e) {
			LOGGER.error("DB ERROR: " + e.getMessage());
			result = null;
		}
		
		return result;
		
	}

}
