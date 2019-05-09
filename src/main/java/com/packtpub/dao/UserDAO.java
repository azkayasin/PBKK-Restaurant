package com.packtpub.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.packtpub.model.User;
import com.packtpub.repository.UserRepository;

@Service
public class UserDAO {
	
	@Autowired
	UserRepository userRepository;
	
	public User save(User usr) {
		return userRepository.save(usr);
	}
	
	public List<User> findAll()
	{
		return userRepository.findAll();
	}
	
	public User findById(Integer id)
	{
		User usr=userRepository.findByIduser(id);
		if(usr!=null)
		{
			return new User(usr.getId(),usr.getName(),usr.getUserName(),usr.getRole(),usr.getStatus(),usr.getEmail());
		}
		return null;
	}
	
	public List<User> findCustomer(Integer id)
	{
		List <User> customers=new ArrayList<User>(); 
		if(id!=null)
		{
			User usr=userRepository.findByIduser(id);
			if(usr!=null&&usr.getRole()==1)
			{
				customers.add(new User(usr.getId(),usr.getName(),usr.getUserName(),usr.getRole(),usr.getStatus(),usr.getEmail()));
			}
		}
		else
		{
			List<User> getCustomers=userRepository.findCustomer();
			if(getCustomers!=null)
			{
				for(User usr:getCustomers)
				{
					customers.add(new User(usr.getId(),usr.getName(),usr.getUserName(),usr.getRole(),usr.getStatus(),usr.getEmail()));
				}
			}
		}
		return customers;
	}
	
	public List<User> findRestaurant(Integer id)
	{
		List <User> restaurants=new ArrayList<User>(); 
		if(id!=null)
		{
			User usr=userRepository.findByIduser(id);
			if(usr!=null&&usr.getRole()==2)
			{
				restaurants.add(new User(usr.getId(),usr.getName(),usr.getUserName(),usr.getRole(),usr.getStatus(),usr.getEmail()));
			}
		}
		else
		{
			List<User> getCustomers=userRepository.findRestaurant();
			if(getCustomers!=null)
			{
				for(User usr:getCustomers)
				{
					restaurants.add(new User(usr.getId(),usr.getName(),usr.getUserName(),usr.getRole(),usr.getStatus(),usr.getEmail()));
				}
			}
		}
		return restaurants;
	}
	
	public User login(String email,String password)
	{
		User user=userRepository.findByEmailandPassword(email, DigestUtils.sha256Hex(password));
		if(user==null)
		{
			throw new IllegalArgumentException("Username or Password is Not Match");
		}
		return user;
	}
	
	public User login(Integer userId,String password)
	{
		User user=userRepository.findByUserIdandPassword(userId, DigestUtils.sha256Hex(password));
		return user;
	}
	
	public boolean checkUsername(String userName)
	{
		User checkUser=userRepository.findByUsername(userName);
		if(checkUser == null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean checkEmail(String email)
	{
		User checkUser=userRepository.findByEmail(email);
		if(checkUser==null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void deleteUser(Integer userId)
	{
		User usr=userRepository.findByIduser(userId);
		if(usr!=null)
		{
			usr.setStatus(0);
			userRepository.save(usr);
		}
	}
	
	public void updatePasswordUser(Integer userId,String password)
	{
		User usr=userRepository.findByIduser(userId);
		if(usr!=null)
		{
			usr.setPassword(password);
			userRepository.save(usr);
		}
	}
	
	public void updateUser(Integer userId, String name, String userName, String email)
	{
		User usr=userRepository.findByIduser(userId);
		if(usr!=null)
		{
			if(name != null && name != "") {
				usr.setName(name);
			}
			if(userName != null && userName != "") {
				usr.setUserName(userName);
			}
			if(email != null && email != "") {
				usr.setEmail(email);
			}
			
			userRepository.save(usr);
		}
	}
}
