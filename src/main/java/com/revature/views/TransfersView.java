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

public class TransfersView implements View {
	Set<Integer> fromAcctNumbSet = new HashSet<>();
	Set<Integer> toAcctNumbSet = new HashSet<>();
	Map<Integer, Double> fromAcctMap = new HashMap<>();
	Map<Integer, Double> toAcctMap = new HashMap<>();
	public double transfer_amount;
	public int acctNumb;
	public String acctName;

	public View menuCommands() {
		System.out.println(" ____________________________________________________________________");
		System.out.println("  Thank you for chosing to transfer funds from one of your accounts. ");
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
				fromAcctNumbSet.add(rs.getInt(2));
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
				fromAcctMap.put(rs.getInt(2), rs.getDouble(5));
				fromAcctNumbSet.add(rs.getInt(2));
			}
			System.out.println(" ____________________________________________________________________");
			System.out.println("  Which account would you like to transfer from?");
			try {
				int account_from_number = Integer.parseInt(ScannerUtil.getLine());
				System.out.println(" ____________________________________________________________________");
				if (fromAcctNumbSet.contains(account_from_number)) {
					System.out.println("  How much would you like to transfer from account #" + account_from_number + "?");
					try {
						transfer_amount = Double.parseDouble(ScannerUtil.getLine());
					} catch (NumberFormatException e) {
						AppLauncher.log.catching(e);
						System.out.println("  Only numbers are excepted here.");
						return new WithdrawView();
					}
					double fromAcctBal = fromAcctMap.get(account_from_number);
					if(transfer_amount <= fromAcctBal) {
						try(Connection connection1 = ConnectionUtil.getConnection()){
							String sql1 = "select * from bank_account_table;";
							PreparedStatement ps1 = connection1.prepareStatement(sql1);
							ResultSet rs1 = ps1.executeQuery();

							while (rs1.next()) {
								toAcctNumbSet.add(rs1.getInt(1));
							}
							System.out.println(" ____________________________________________________________________");
							System.out.println("  Which account would you like to transfer to?");
							try{
								int account_to_number = Integer.parseInt(ScannerUtil.getLine());
								System.out.println(" ____________________________________________________________________");
								if (toAcctNumbSet.contains(account_to_number)) {
									try (Connection connection2 = ConnectionUtil.getConnection()) {
										String sql2 = "select * from user_bank_account_table join bank_account_table on user_bank_account_table.account_number = "
												+ "bank_account_table.account_number where bank_account_table.account_number = '" + account_to_number + "';";
										PreparedStatement ps2 = connection2.prepareStatement(sql2);
										ResultSet rs2 = ps2.executeQuery();

										while (rs2.next()) {
											toAcctMap.put(rs2.getInt(2), rs2.getDouble(5));
										}
										if(!toAcctMap.containsKey(account_to_number)) {
											System.out.println("  This account either does not exist or it has no\n banking accounts associated with it. Please try a\n different account number.");
										}
									}catch (SQLException e) {
										AppLauncher.log.catching(e);
									}
									double toAcctBal = toAcctMap.get(account_to_number);
									fromAcctBal -= transfer_amount;
								    toAcctBal += transfer_amount;
									try (Connection connection3 = ConnectionUtil.getConnection()) {
										String sql3 = "UPDATE bank_account_table SET balance = '" + fromAcctBal
												+ "'  where account_number = '" + account_from_number + "';";
										String sql4 = "UPDATE bank_account_table SET balance = '" + toAcctBal
												+ "'  where account_number = '" + account_to_number + "';";
										PreparedStatement ps3 = connection1.prepareStatement(sql3);
										PreparedStatement ps4 = connection1.prepareStatement(sql4);
										ps3.executeUpdate();
										ps4.executeUpdate();
										System.out.println("  Your transfer has been completed and both accounts have been updated.");
										System.out.println(" ____________________________________________________________________");
									} catch (SQLException e) {
										AppLauncher.log.catching(e);
									}
								}else {
									System.out.println(
											"  The account you are trying to transfer to doesn't exist.\n Please try again.");
									return new TransfersView();
								}
							}catch(NumberFormatException e) {
								AppLauncher.log.catching(e);
								System.out.println("  You entered something other than whole number, please try again");
								return new TransfersView();
							}
							
						}catch (SQLException e) {
							AppLauncher.log.catching(e);
						}
					}else {
						System.out.println("  You are trying to transfer more money than what you\n have in account #" + 
								account_from_number + ". Please try a lower amount or a different\n account to transfer from.");
					}
				} else {
					System.out.println(
							"  The account you entered either doesn't exist or doesn't belong\n to you. Please try again.");
					return new TransfersView();
				}
			}catch(NumberFormatException e) {
				AppLauncher.log.catching(e);
				System.out.println("  You entered something other than whole number, please try again");
				return new TransfersView();
			}
			boolean answerValid = false;
			while (!answerValid) {
				System.out.println("  Would you like to make another transfer? Y/N");
				String answer = ScannerUtil.getLine();
				if (answer.contains("Y") || answer.contains("y")) {
					return new TransfersView();
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
