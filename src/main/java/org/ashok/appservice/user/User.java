package org.ashok.appservice.user;

import java.util.List;

public record User(
		String userName,
		String firstName,
		String lastName,
		List<String> roles) 
{}
