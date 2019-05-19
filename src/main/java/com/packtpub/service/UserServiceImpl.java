package com.packtpub.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.packtpub.service.UserService;
import com.packtpub.dao.UserDAO;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	UserDAO userDAO;
	
	public  Pattern VALID_EMAIL_ADDRESS_REGEX = 
		    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	
	@Override
	public void validate(String emailStr) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        if(matcher.find()==false)
        {
        	throw new IllegalArgumentException("Email doesn't match");
        }
	}

	@Override
	public void checkAvailable(String username, String email) {
		if(userDAO.checkUsername(username)==false)
		{
			throw new IllegalArgumentException("Username has been used");
		}
		if(userDAO.checkEmail(email)==false)
		{
			throw new IllegalArgumentException("Email has been used");
		}
	}

	@Override
	public String getRoleName(int role) {
		if(role==2)
		{
			return "Restaurant";
		}
		else if(role==3)
		{
			return "Admin";
		}
		else if(role==4)
		{
			return "Driver";
		}
		else
		{
			return "Customer";
		}
	}
	

}
