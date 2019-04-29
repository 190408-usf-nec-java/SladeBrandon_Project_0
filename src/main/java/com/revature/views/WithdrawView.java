package com.revature.views;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.revature.AppLauncher;
import com.revature.util.ConnectionUtil;
import com.revature.util.ScannerUtil;

public class WithdrawView implements View{
	Set<Integer> acctNumbSet = new HashSet<>();
	Map<Integer, Double> acctMap = new HashMap<>();
	public double withdraw_amount;
	public int acctNumb;
	public String acctName;

	public View menuCommands() {
		System.out.println(" ____________________________________________________________________");
		System.out.println("  Thank you for chosing to withdraw funds from one of your accounts. ");
		System.out.println(" ____________________________________________________________________");
		System.out.println("  Here are a list of your accounts: ");
		System.out.println(" ____________________________________________________________________");
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "select * from user_bank_account_table join bank_account_table on user_bank_account_table.account_number = "
					+ "bank_account_table.account_number where id = " + LoginView.userNumb + ";";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				acctNumb = rs.getInt(2);
				acctName = rs.getString(4);
				double acctBal = rs.getDouble(5);
				acctNumbSet.add(rs.getInt(2));
				System.out.println("  Acct #" + acctNumb + " |" + " Acct type " + acctName + " |" + " Your balance is: "
						+ acctBal);
				System.out.println("|____________________________________________________________________|");
			}
		} catch (SQLException e) {
			AppLauncher.log.catching(e);
		}
		try (Connection connection = ConnectionUtil.getConnection()) {
			String sql = "select * from user_bank_account_table join bank_account_table on user_bank_account_table.account_number = "
					+ "bank_account_table.account_number where id = " + LoginView.userNumb + ";";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				acctMap.put(rs.getInt(2), rs.getDouble(5));
				acctNumbSet.add(rs.getInt(2));
			}
				System.out.println(" ");
				System.out.println(" ____________________________________________________________________");
				System.out.println("  Which account would you like to withdraw from?");
				try {
					int account_number = Integer.parseInt(ScannerUtil.getLine());
					System.out.println(" ____________________________________________________________________");
					if (acctNumbSet.contains(account_number)) {
						System.out.println("  How much would you like to withdraw from account #" + account_number + "?");
						try {
							withdraw_amount = Double.parseDouble(ScannerUtil.getLine());
						} catch (NumberFormatException e) {
							AppLauncher.log.catching(e);
							System.out.println("  Only numbers are excepted here.");
							return new WithdrawView();
						}
						double acctBal = acctMap.get(account_number);
						if (withdraw_amount <= acctMap.get(account_number)) {
						    acctBal -= withdraw_amount;
							try (Connection connection1 = ConnectionUtil.getConnection()) {
								String sql1 = "UPDATE bank_account_table SET balance = '" + acctBal
										+ "'  where account_number = '" + account_number + "';";
								PreparedStatement ps1 = connection1.prepareStatement(sql1);
								ps1.executeUpdate();
								System.out.println("  Your balance has been updated");
								System.out.println(" ____________________________________________________________________");
							} catch (SQLException e) {
								AppLauncher.log.catching(e);
							}
						} else {
							System.out.println("  You entered a withdraw amount that exceeds your account balance, please try again.");
							return new WithdrawView();
						}
					} else {
						System.out.println(
								"  The account you entered either doesn't exist or doesn't belong\n to you. Please try again.");
						return new WithdrawView();
					}
				}catch(NumberFormatException e) {
					AppLauncher.log.catching(e);
					System.out.println("  You entered something other than whole number please try again");
					return new WithdrawView();
				}
				boolean answerValid = false;
				while (!answerValid) {
					System.out.println("  Would you like to make another withdraw? Y/N");
					String answer = ScannerUtil.getLine();
					if (answer.contains("Y") || answer.contains("y")) {
						return new WithdrawView();
					} else if (answer.contains("N") || answer.contains("n")) {
						return new AccountView();
					} else {
						System.out.println("  You entered an invalid command. Please try again.");
					}
				}
			
		} catch (SQLException e) {
			AppLauncher.log.catching(e);
		}
		return null;
	}
}
