package com.openclassrooms.mddapi.dto;

public class UserLoginRequest {


	private String email;
    private String password;
    
	public String getLogin() {
		return email;
	}
	public void setLogin(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
    
    

}
