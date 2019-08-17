package magistrale.tesi.oracle.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

	static private final String jdbcUrlMysql = "jdbc:mysql://localhost/bd?user=root";
	static private final String jdbcUrlErr = "jdbc:mysql://localhost/prova?user=root";
		static private final String jdbcUrlPostgre = "jdbc:postgresql://localhost:5432/bd?user=postgres&password=admin";
	
	static private DBConnect instance = null;

	private DBConnect() {
		instance = this;
	}

	public static DBConnect getInstance() {
		if (instance == null)
			return new DBConnect();
		else {
			return instance;
		}
	}

	public Connection getConnectionMysql() {
		try {
			Connection conn = DriverManager.getConnection(jdbcUrlMysql);
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Cannot get connection " + jdbcUrlMysql, e);
		}
	}
	
	public Connection getConnectionErr() {
		try {
			Connection conn = DriverManager.getConnection(jdbcUrlErr);
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Cannot get connection " + jdbcUrlErr, e);
		}
	}
	
	public Connection getConnectionPostgre() {
		try {
			Connection conn = DriverManager.getConnection(jdbcUrlPostgre);
			return conn;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Cannot get connection " + jdbcUrlPostgre, e);
		}
	}
}
