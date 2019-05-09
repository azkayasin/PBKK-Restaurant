package com.packtpub.restapp;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.packtpub.aop.AdminTokenRequired;
import com.packtpub.aop.UserTokenRequired;
import com.packtpub.dao.TokenDAO;
import com.packtpub.dao.UserDAO;
import com.packtpub.model.User;
import com.packtpub.service.SecurityService;
import com.packtpub.util.Util;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	TokenDAO tokenDAO;
	
	@Autowired
	SecurityService securityService;
	
	public  Pattern VALID_EMAIL_ADDRESS_REGEX = 
		    Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

	public void validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        if(matcher.find()==false)
        {
        	throw new IllegalArgumentException("Email doesn't match");
        }
	}
	
	public void checkAvailable(String username,String email)
	{
		if(userDAO.checkUsername(username)==false)
		{
			throw new IllegalArgumentException("Username has been used");
		}
		if(userDAO.checkEmail(email)==false)
		{
			throw new IllegalArgumentException("Email has been used");
		}
	}
	
	@ResponseBody
	@AdminTokenRequired
	@RequestMapping("")
	public List<User> getAllUsers() {
		return userDAO.findAll();
	}
	
	@ResponseBody
	@RequestMapping(value = "/register/dummy", method = RequestMethod.POST)
	public Map<String, Object> registerCustomer(
				@RequestParam(value = "username") String username,
				@RequestParam(value = "name") String name,
				@RequestParam(value = "email") String email,
				@RequestParam(value = "password") String password
			) 
	{
		checkAvailable(username,email);
		validate(email);
		
		User usr=new User();
		usr.setName(name);
		usr.setUserName(username);
		usr.setPassword(password);
		usr.setEmail(email);
		usr.setStatus(1);
		usr.setRole(3);
		usr.setCreatedAt(new Date());
		userDAO.save(usr);
		return Util.getSuccessResult();
	}
	
	@ResponseBody
	@AdminTokenRequired
	@RequestMapping(value = "/register/restaurant", method = RequestMethod.POST)
	public Map<String, Object> registerRestaurant(
				@RequestParam(value = "username") String username,
				@RequestParam(value = "name") String name,
				@RequestParam(value = "email") String email,
				@RequestParam(value = "password") String password
			) 
	{
		checkAvailable(username,email);
		validate(email);
		
		User usr=new User();
		usr.setName(name);
		usr.setUserName(username);
		usr.setPassword(password);
		usr.setEmail(email);
		usr.setStatus(1);
		usr.setRole(2);
		usr.setCreatedAt(new Date());
		userDAO.save(usr);
		return Util.getSuccessResult();
	}
	
	@ResponseBody
	@AdminTokenRequired
	@RequestMapping(value = "/register/admin", method = RequestMethod.POST)
	public Map<String, Object> registerAdmin(
				@RequestParam(value = "username") String username,
				@RequestParam(value = "name") String name,
				@RequestParam(value = "email") String email,
				@RequestParam(value = "password") String password
			) 
	{
		checkAvailable(username,email);
		validate(email);
		
		User usr=new User();
		usr.setName(name);
		usr.setUserName(username);
		usr.setPassword(password);
		usr.setEmail(email);
		usr.setStatus(1);
		usr.setRole(3);
		usr.setCreatedAt(new Date());
		userDAO.save(usr);
		return Util.getSuccessResult();
	}
	
	
	@ResponseBody
	@AdminTokenRequired
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public Map<String, Object> deleteUser(
			@PathVariable("id") Integer userid) {
		
		//Find user id
		User user=userDAO.findById(userid);
		if(user==null)
		{
			return Util.getErrorResult("Username not Found");
		}
				
		Map<String, Object> map = new LinkedHashMap<>();   
		userDAO.deleteUser(userid);    
	    map.put("result", "deleted");
	    return map;
	}
	
	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Map<String, Object> login(
				@RequestParam(value = "email") String email,
				@RequestParam(value = "password") String password
			) 
	{
		User user=userDAO.login(email, password);
		String subject = user.getId()+"="+user.getRole();
		String token = securityService.createToken(subject, (60 * 1000 * 60),user.getId()); // 60 minutes expiry time
		return Util.getSuccessResult(token);
	}
	
	@ResponseBody
	@UserTokenRequired
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public Map<String, Object> logout(
			@RequestParam(value = "email") String email,
			@RequestParam(value = "password") String password
		) 
	{
		ServletRequestAttributes reqAttributes = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = reqAttributes.getRequest();
		
		// checks for token in request header
		String tokenInHeader = request.getHeader("token");
		
		if(StringUtils.isEmpty(tokenInHeader)){
			throw new IllegalArgumentException("Empty token");
		}
		
		//System.out.println(tokenInHeader);
		tokenDAO.delete(tokenInHeader);
		return Util.getSuccessResult();
	}
	
	@ResponseBody
	@UserTokenRequired
	@RequestMapping("/{id}")
	public User getUser(@PathVariable("id") Integer id) {
		User get=userDAO.findById(id);
		return get;
	}
	
	@ResponseBody
	@UserTokenRequired
	@RequestMapping("/customer")
	public List<User> getCustomer() {
		List<User> get=userDAO.findCustomer(null);
		return get;
	}
	
	@ResponseBody
	@UserTokenRequired
	@RequestMapping("/customer/{id}")
	public List<User> getCustomerById(@PathVariable("id") String idS) {
		Integer id=null;
		if(!idS.equals(""))
		{
			id=Integer.valueOf(idS);
		}
		List<User> get=userDAO.findCustomer(id);
		return get;
	}
	
	@ResponseBody
	@UserTokenRequired
	@RequestMapping("/restaurant")
	public List<User> getRestaurant() {
		List<User> get=userDAO.findRestaurant(null);
		return get;
	}
	
	@ResponseBody
	@UserTokenRequired
	@RequestMapping("/restaurant/{id}")
	public List<User> getRestaurantById(@PathVariable("id") String idS) {
		Integer id=null;
		if(!idS.equals(""))
		{
			id=Integer.valueOf(idS);
		}
		List<User> get=userDAO.findRestaurant(id);
		return get;
	}
	
	@ResponseBody
	@UserTokenRequired
	@RequestMapping(value = "/changepassword", method = RequestMethod.POST)
	public Map<String, Object> changePassword(
				@RequestParam(value = "oldpassword") String oldPassword,
				@RequestParam(value = "newpassword") String newPassword,
				@RequestParam(value = "confirmpassword") String confirmPassword
			) 
	{
		String userIdString=securityService.getUserId();
		Integer userId=Integer.valueOf(userIdString);
		
		User user=userDAO.login(userId, oldPassword);
		if(user==null)
		{
			return Util.getErrorResult("Username or Password is Not Match");
		}
		if(oldPassword.equals("")||newPassword.equals("")||confirmPassword.equals(""))
		{
			return Util.getErrorResult("All fields must be filled in");
		}
		if(!newPassword.equals(confirmPassword))
		{
			return Util.getErrorResult("New password not equal with confirm password");
		}
		
		userDAO.updatePasswordUser(userId,newPassword);
		
		
		return Util.getSuccessResult("Successfully updated user password");
	}
	
	@ResponseBody
	@AdminTokenRequired
	@RequestMapping(value = "/changepassword/{id}", method = RequestMethod.POST)
	public Map<String, Object> changePasswordAdmin(
				@PathVariable("id") Integer id,
				@RequestParam(value = "newpassword") String newPassword
			) 
	{
		User user=userDAO.findById(id);//find by id
		if(user==null)
		{
			return Util.getErrorResult("Username not Found");
		}
		
		userDAO.updatePasswordUser(id,newPassword);
		
		return Util.getSuccessResult("Successfully updated user password");
		
	}
	
	@ResponseBody
	@UserTokenRequired
	@RequestMapping(value = "/checkusername", method = RequestMethod.POST)
	public Map<String, Object> checkUsername(
				@RequestParam(value = "username") String username
			) 
	{
		if(userDAO.checkUsername(username)==false)
		{
			return Util.getErrorResult("Username available");
		}
		return Util.getSuccessResult("Username not available");
	}
	
	@ResponseBody
	@UserTokenRequired
	@RequestMapping(value = "/checkemail", method = RequestMethod.POST)
	public Map<String, Object> checkEmail(
				@RequestParam(value = "email") String email
			) 
	{
		if(userDAO.checkEmail(email)==false)
		{
			return Util.getErrorResult("Username available");
		}
		return Util.getSuccessResult("Username not available");
	}
	
	@ResponseBody
	@UserTokenRequired
	@RequestMapping(value = "/changebiodata", method = RequestMethod.POST)
	public Map<String, Object> changeBiodata(
				@RequestParam(value = "username") String userName,
				@RequestParam(value = "name") String name,
				@RequestParam(value = "email") String email
			) 
	{
		String userIdString=securityService.getUserId();
		Integer userId=Integer.valueOf(userIdString);
		
		userDAO.updateUser(userId, name, userName, email);
		
		return Util.getSuccessResult("Biodata was updated");
	}
	
	@ResponseBody
	@AdminTokenRequired
	@RequestMapping(value = "/changebiodata/{id}", method = RequestMethod.POST)
	public Map<String, Object> changeBiodataAdmin(
				@PathVariable("id") Integer id,
				@RequestParam(value = "username") String userName,
				@RequestParam(value = "name") String name,
				@RequestParam(value = "email") String email
			)
	{
		//Find user id
		User user=userDAO.findById(id);
		if(user==null)
		{
			return Util.getErrorResult("Username not Found");
		}
		
		userDAO.updateUser(id, name, userName, email);
		
		return Util.getSuccessResult("Biodata was updated");
	}
}
