package com.revature.views;

import com.revature.services.UserService;

public class CreateUserView implements View {

	public View menuCommands() {
		System.out.println(" ____________________________________________________________________");
		UserService.createUser();
		System.out.println(" ____________________________________________________________________");
		return new LoginView();
	}
}
