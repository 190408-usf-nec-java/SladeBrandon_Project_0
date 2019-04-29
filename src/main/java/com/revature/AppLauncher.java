package com.revature;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.views.MainMenuView;
import com.revature.views.View;

public class AppLauncher {
	public static Logger log = LogManager.getRootLogger(); 
	public static void main(String[] args) {
		View view = new MainMenuView();
		
		while(view != null) {
			view = view.menuCommands();
		}
		
		System.out.println("  Thanks for using our app, please come again soon.");
	}
	

}
