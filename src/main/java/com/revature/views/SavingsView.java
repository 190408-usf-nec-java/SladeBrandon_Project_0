package com.revature.views;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import com.revature.AppLauncher;
import com.revature.util.ConnectionUtil;
import com.revature.util.ScannerUtil;

public class SavingsView implements View {
	public static boolean acctCreated = false;
	public static int acctNumb = 0;
	public static int userId = 0;
	public static int userId2 = 0;
	static Set<String> userSet = new HashSet<>();
	
	public View menuCommands() {
		System.out.println(" ____________________________________________________________________");
		System.out.println("  Thank you for choosing to create a savings account.");
		System.out.println("  Will this be a joint account? Yes/No");
		System.out.println(" ____________________________________________________________________");
		System.out.println("  1. Logout");
		System.out.println("  2. Back to account view");
		System.out.println("  3. Yes");
		System.out.println("  4. No");
		System.out.println("  0. Quit");
		System.out.println(" ____________________________________________________________________");
	
		int selection = ScannerUtil.getNumericChoice(4);
		
		switch(selection) {
		case 1: return new MainMenuView();
		case 2: return new AccountView();
		case 3: {
				System.out.println("  What is the username of the other account that will co-own this account?");
				String username_Entry = ScannerUtil.getLine();
				 int userid2 = 0;
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
					try (Connection connection = ConnectionUtil.getConnection()) {
						String sql3 = "select id from user_account_table where user_name = '" + username_Entry + "';";
						PreparedStatement ps3 = connection.prepareStatement(sql3);
						ResultSet rs3 = ps3.executeQuery();
						while(rs3.next()) {
							userid2 += rs3.getInt(1);
						}
						String sql = "insert into bank_account_table (account_name, balance) values ('Savings', 0.00) RETURNING account_number";
						PreparedStatement ps = connection.prepareStatement(sql);
						ResultSet rs = ps.executeQuery();
						while (rs.next()) {
							acctNumb += rs.getInt(1);
						}
						String sql2 = "insert into user_bank_account_table (id, account_number) values ('" + LoginView.userNumb + "', '" + acctNumb + "'), ('" + userid2 + "', '" + acctNumb + "');";
						PreparedStatement ps2 = connection.prepareStatement(sql2);
						ps2.executeUpdate();
						System.out.println("  Your account has been created.");
					} catch (SQLException e) {
						AppLauncher.log.catching(e);
						}
					}
			}
			return new AccountView();
		case 4: {
				try (Connection connection = ConnectionUtil.getConnection()) {
					String sql = "insert into bank_account_table (account_name, balance) values ('Savings', 0.00) RETURNING account_number";
					PreparedStatement ps = connection.prepareStatement(sql);
					ResultSet rs = ps.executeQuery();
					while (rs.next()) {
						acctNumb += rs.getInt(1);
					}
					String sql2 = "insert into user_bank_account_table (id, account_number) values ('" + LoginView.userNumb + "', '" + acctNumb + "');";
					PreparedStatement ps2 = connection.prepareStatement(sql2);
					ps2.executeUpdate();
					System.out.println("  Your account has been created.");
				} catch (SQLException e) {
					AppLauncher.log.catching(e);
					}
				}	
				return new AccountView();
		default: return null;
		}
	}
}
