package com.revature.views;

import com.revature.util.ScannerUtil;

public class MainMenuView implements View {
	
	public View menuCommands() {
		System.out.println(" ____________________________________________________________________");
		System.out.println("  Welcome to Slade Banking Inc. \n  What would you like to do?");
		System.out.println(" ____________________________________________________________________");
		System.out.println("  1. Login");
		System.out.println("  2. Create New User");
		System.out.println("  3. Reset Password");
		System.out.println("  0. Quit");
		System.out.println(" ____________________________________________________________________");
	
		int selection = ScannerUtil.getNumericChoice(3);
		
		switch(selection) {
		case 1: return new LoginView();
		case 2: return new CreateUserView();
		case 3: return new ResetPasswordEXTView();
		default: return null;
		}
		
	}
}
