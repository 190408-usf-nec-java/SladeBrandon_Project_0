package com.revature.views;

import com.revature.util.ScannerUtil;

public class CreateBankAccountView implements View {

	public View menuCommands() {
		System.out.println(" ____________________________________________________________________");
		System.out.println("  What type of account do you want to create?");
		System.out.println(" ____________________________________________________________________");
		System.out.println("  1. Checking");
		System.out.println("  2. Savings");
		System.out.println("  3. Back to account view");
		System.out.println("  4. Logout");
		System.out.println("  0. Quit");
		System.out.println(" ____________________________________________________________________");
		

		int selection = ScannerUtil.getNumericChoice(4);
		
		switch(selection) {
		case 1: return new CheckingView();
		case 2: return new SavingsView();
		case 3: return new AccountView();
		case 4: return new MainMenuView();
		default: return null;
		}
	}

}
