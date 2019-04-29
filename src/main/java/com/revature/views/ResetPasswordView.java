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

public class ResetPasswordView implements View {
	
	public static boolean userVerified = false;
	static Map<String, String> userMap = new HashMap<>();

	public View menuCommands() {
		System.out.println(" ____________________________________________________________________");
		System.out.println("  Are you sure you would like to reset your password? Once you\n start"
				+ "the process you will have to complete it to regain access\n to your account. "
				+ "Press 1 to proceed and 2 if you would like\n to go back to the account view.");
		System.out.println(" ____________________________________________________________________");
		System.out.println("  1. Proceed");
		System.out.println("  2. Back to account view");
		System.out.println(" ____________________________________________________________________");
		
		int selection = ScannerUtil.getNumericChoice(2);
		
		switch(selection) {
		case 1:	
			System.out.println(" ____________________________________________________________________");
			System.out.println("  Reset your password by providing the following information:");
			while(!userVerified) {
				System.out.println(" ____________________________________________________________________");
				System.out.println("  Please enter your username.");
				String username = ScannerUtil.getLine();
				try (Connection connection = ConnectionUtil.getConnection()) {
					String sql = "select id, user_name from user_account_table;";
					PreparedStatement ps = connection.prepareStatement(sql);
					ResultSet rs = ps.executeQuery();
					while (rs.next()) {
						userMap.put(rs.getString(1), rs.getString(2));
					}
	
				} catch (SQLException e) {
					AppLauncher.log.catching(e);
				}
				if(userMap.containsValue(username)) { 
					System.out.println(" ____________________________________________________________________");
					System.out.println("  Please enter your user account number.");
					String userAcctNumber = ScannerUtil.getLine();
					if(userMap.containsKey(userAcctNumber)) {
						if(userMap.get(userAcctNumber).equals(username)) {
							System.out.println(" ____________________________________________________________________");
							System.out.println("  What would you like your new password to be?");
							String password1 = ScannerUtil.getLine();
							System.out.println("  Please confirm your new password.");
							String password2 = ScannerUtil.getLine();
							if(password1.equals(password2)){
								try (Connection connection = ConnectionUtil.getConnection()) {
									String sql = "UPDATE user_account_table SET password = '" + password1 + "' where user_name = '" + username + "';";
									PreparedStatement ps = connection.prepareStatement(sql);
									ps.executeUpdate();
									System.out.println(" ____________________________________________________________________");
									System.out.println("  Your password has been reset.");
									System.out.println(" ____________________________________________________________________");
									return new LoginView();	
								} catch (SQLException e) {
									AppLauncher.log.catching(e);
								}
							}else {
								System.out.println("  The two passwords you entered do not match. Please try again");
							}
							
						}else {
							System.out.println("  The username and account number you provided do not match. Please try again.");
						}
					}else {
						System.out.println("  This account number doesn't exist please try again.");
					}				
				}else {
					System.out.println("  This username doesn't exist please try again.");
				}
			}
		return null;
		case 2: return new AccountView();
		default: return null;
		}
	}
}
