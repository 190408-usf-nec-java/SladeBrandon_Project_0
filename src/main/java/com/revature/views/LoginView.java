package com.revature.views;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.revature.AppLauncher;
import com.revature.util.ConnectionUtil;
import com.revature.util.ScannerUtil;

public class LoginView implements View {	
	
	public static boolean userVerified = false;
	static Map<String, String> userMap = new HashMap<>();
	public static int userNumb;
		
	public View menuCommands() {
		System.out.println(" ____________________________________________________________________");
		System.out.println("  Please provide the following information to log into your account.");
		while(!userVerified) {
			System.out.println(" ____________________________________________________________________");
			System.out.println("  What is your username? ");
			try {
				String username = ScannerUtil.getLine();
				try (Connection connection = ConnectionUtil.getConnection()) {
					String sql = "select id, user_name, password from user_account_table where user_name = '" + username + "';";
					PreparedStatement ps = connection.prepareStatement(sql);
					ResultSet rs = ps.executeQuery();
					while (rs.next()) {
						userMap.put(rs.getString(2), rs.getString(3));
						userNumb = rs.getInt(1);
					}
				} catch (SQLException e) {
					AppLauncher.log.catching(e);
				}
				if(userMap.containsKey(username)) {
					System.out.println("  What is your password? ");
					String password = ScannerUtil.getLine();
					System.out.println(" ____________________________________________________________________");
					try {
						if(userMap.get(username).equals(password)) {
							userVerified = true;
							return new AccountView();
						}else {
							System.out.println("  The username and password do not match what is in the database\n please try again.");
						}
					}catch(NullPointerException e) {
						AppLauncher.log.catching(e);
						System.out.println("  The username and password do not match what is in the database\n please try again.");
					}
				}else {
					System.out.println("  The username you entered does not exist. Please try again.");
				}
			}catch (NullPointerException e) {
				AppLauncher.log.catching(e);
			}
		}
		return null;
	}
}
