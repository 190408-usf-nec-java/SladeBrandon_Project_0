package com.revature.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
	
	public static Connection getConnection() throws SQLException {
		
		String url = System.getenv("JDBC_URL");
		String username = System.getenv("JDBC_LOGIN");
		String password = System.getenv("JDBC_PASSWORD");
		Connection connection = DriverManager.getConnection(url, username, password);
		return connection;
	}
}	
