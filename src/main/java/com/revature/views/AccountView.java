package com.revature.views;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.revature.AppLauncher;
import com.revature.util.ConnectionUtil;
import com.revature.util.ScannerUtil;

public class AccountView implements View {
	public static int acctNumb;
	public static String acctName;
	public static BigDecimal acctBal;
	public static int userAcctNumb;

	public View menuCommands() {
		System.out.println(" ____________________________________________________________________");
		System.out.println("  This is your account view where you may access all the other\n functions "
				+ "of your account. The list below are all the valid\n commands available to you.");
		System.out.println(" ____________________________________________________________________");
		System.out.println("  1. Logout");
		System.out.println("  2. Reset Password");
		System.out.println("  3. Create new checking or savings account");
		System.out.println("  4. Make a deposit");
		System.out.println("  5. Make a withdrawl");
		System.out.println("  6. Make a transfer");
		System.out.println("  0. Quit");
		System.out.println(" ____________________________________________________________________");
		System.out.println(" ");
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
				acctBal = rs.getBigDecimal(5);
				userAcctNumb = rs.getInt(1);
				System.out.println("  Acct #" + acctNumb + " |" + " Acct type " + acctName + " |" + " Your balance is: " + acctBal);
				System.out.println("|____________________________________________________________________|");
				
			}
			System.out.println(" ");
			System.out.println(" ____________________________________________________________________");
			System.out.println("  Your user account number is: #" + userAcctNumb + " Please make sure to remember\n this number "
					+ "as you will need it to reset your password should\n you forget it.");
			System.out.println(" ____________________________________________________________________");
			System.out.println("  What would you like to do from here?");
		} catch (SQLException e) {
			AppLauncher.log.catching(e);
		}
		
		int selection = ScannerUtil.getNumericChoice(6);
		
		switch(selection) {
		case 1:	return new MainMenuView();
		case 2: return new ResetPasswordView();
		case 3: return new CreateBankAccountView();
		case 4: return new DepositView();
		case 5: return new WithdrawView();
		case 6: return new TransfersView();
		default: return null;
		}
	}

}
