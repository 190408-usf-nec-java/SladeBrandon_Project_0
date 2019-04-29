package com.revature.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.revature.AppLauncher;
import com.revature.beans.User;
import com.revature.daos.UserDao;
import com.revature.util.ConnectionUtil;
import com.revature.util.ScannerUtil;

public class UserService {
	static UserDao userDao = new UserDao();
	public static boolean userExists = false;
	public static boolean passwordMatches = false;
	public static String username = "";
	public static String password = "";
	static Set<String> userSet = new HashSet<>();

	public static void createUser() {
		System.out.println(" ____________________________________________________________________");
		System.out.println("  Thank you for choosing to create a new user account.");
		while (!userExists) {
			System.out.println(" ____________________________________________________________________");
			System.out.println("  Please enter a username: ");
			String username_Entry = ScannerUtil.getLine();
			try (Connection connection = ConnectionUtil.getConnection()) {
				String sql = "select user_name from user_account_table;";
				PreparedStatement ps = connection.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					userSet.add(rs.getString(1));
				}

			} catch (SQLException e) {
				AppLauncher.log.catching(e);
			}
			if (userSet.contains(username_Entry)) {
				System.out.println("  This user already exists in our system please choose another username:");

			} else {
				username += username_Entry;
				userExists = true;
			}
		}
		while (!passwordMatches) {
			System.out.println(" ____________________________________________________________________");
			System.out.println("  Please enter a password: ");
			String password1 = ScannerUtil.getLine();
			System.out.println(" ____________________________________________________________________");
			System.out.println("  Please re-enter the same password: ");
			String password2 = ScannerUtil.getLine();
			
			if(password1.equals(password2)) {
				password += password1;
				passwordMatches = true;
			}else {
				System.out.println(" ____________________________________________________________________");
				System.out.println("  Your passwords do not match please try again.");
			}
		}
		User user = new User(username, password);
		user = userDao.saveUser(user);
	}
}
