package com.packtpub.service;

public interface UserService {
	public void validate(String emailStr);
	public void checkAvailable(String username,String email);
	public String getRoleName(int role);

}
