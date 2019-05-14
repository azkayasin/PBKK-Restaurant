package com.packtpub.restapp;

import java.util.Date;
import java.sql.Time;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.packtpub.aop.AdminTokenRequired;
import com.packtpub.aop.RestaurantTokenRequired;
import com.packtpub.aop.UserTokenRequired;
import com.packtpub.dao.TokenDAO;
import com.packtpub.dao.MenuDAO;
import com.packtpub.dao.RestaurantDAO;
import com.packtpub.model.Menu;
import com.packtpub.model.Restaurant;
import com.packtpub.model.User;
import com.packtpub.service.SecurityService;
import com.packtpub.util.Util;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {
	@Autowired
	RestaurantDAO restaurantDAO;
	
	@Autowired
	TokenDAO tokenDAO;
	
	@Autowired
	MenuDAO menuDAO;
	
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
	
	public void checkMenuName(String name)
	{
		if(menuDAO.checkName(name)==false)
		{
			throw new IllegalArgumentException("Name has been used");
		}
	}
	
	public void checkAvailable(String name,String email)
	{
		if(restaurantDAO.checkName(name)==false)
		{
			throw new IllegalArgumentException("Name has been used");
		}
		if(restaurantDAO.checkEmail(email)==false)
		{
			throw new IllegalArgumentException("Email has been used");
		}
	}
	
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public Map<String, Object> registerRestaurant(
				@RequestParam(value = "name") String name,
				@RequestParam(value = "alamat") String alamat,
				@RequestParam(value = "telefon") String telefon,
				@RequestParam(value = "email") String email,
				@RequestParam(value = "deskripsi") String deskripsi,
				@RequestParam(value = "buka") Time buka,
				@RequestParam(value = "tutup") Time tutup,
				@RequestParam(value = "kategori") String kategori,
				@RequestParam(value = "password") String password
			) 
	{
		checkAvailable(name,email);
		validate(email);
		
		Restaurant res=new Restaurant();
		res.setName(name);
		res.setAlamat(alamat);
		res.setTelefon(telefon);
		res.setEmail(email);
		res.setDeskripsi(deskripsi);
		res.setPassword(password);
		res.setStatus(0);
		res.setBuka(buka);
		res.setTutup(tutup);
		res.setKategori(kategori);
		res.setKondisi(0);
		res.setCreatedAt(new Date());
		restaurantDAO.save(res);
		return Util.getSuccessResult(res);
	}
	
	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Map<String, Object> login(
				@RequestParam(value = "email") String email,
				@RequestParam(value = "password") String password
			) 
	{
		Restaurant resto=restaurantDAO.login(email, password);
		String subject = resto.getId()+"="+2;
		String token = securityService.createToken(subject, (60 * 1000 * 60),resto.getId()); // 60 minutes expiry time
		return Util.getSuccessResult(token);
	}
	
	@ResponseBody
	@RestaurantTokenRequired
	@RequestMapping(value = "/open", method = RequestMethod.POST)
	public Map<String, Object> openRestaurant() 
	{
		String restoIdString=securityService.getUserId();
		Integer restoId=Integer.valueOf(restoIdString);
		
		restaurantDAO.openRestaurant(restoId);
		
		
		
		return Util.getSuccessResult("Restaurant open success");
	}
	
	@ResponseBody
	@RestaurantTokenRequired
	@RequestMapping(value = "/close", method = RequestMethod.POST)
	public Map<String, Object> closeRestaurant() 
	{
		String restoIdString=securityService.getUserId();
		Integer restoId=Integer.valueOf(restoIdString);
		
		restaurantDAO.closeRestaurant(restoId);
		
		
		
		return Util.getSuccessResult("Restaurant close success");
	}
	@ResponseBody
	@RestaurantTokenRequired
	@RequestMapping(value = "/updatedata", method = RequestMethod.POST)
	public Map<String, Object> updatedata(
				@RequestParam(value = "alamat") String alamat,
				@RequestParam(value = "telefon") String telefon,
				@RequestParam(value = "deskripsi") String deskripsi,
				@RequestParam(value = "kategori") String kategori,
				@RequestParam(value = "buka") Time buka,
				@RequestParam(value = "tutup") Time tutup
			) 
	{
		String restoIdString=securityService.getUserId();
		Integer restoId=Integer.valueOf(restoIdString);
		
		Restaurant  resto=restaurantDAO.updateRestaurant(restoId, alamat, telefon, deskripsi, kategori, buka, tutup);
		
		return Util.getSuccessUpdate(resto);
	}
	
	
	@ResponseBody
	@AdminTokenRequired
	@RequestMapping(value = "/verifikasi/{id}", method = RequestMethod.POST)
	public Map<String, Object> VerifikasiRestaurant(
				@PathVariable("id") Integer id
			)
	{
		//Find user id
		Restaurant resto=restaurantDAO.findById(id);
		if(resto==null)
		{
			return Util.getErrorResult("Id tidak ditemukan");
		}
		
		restaurantDAO.verifikasiRestaurant(id);
		
		return Util.getSuccessResult("Verifikasi restaurant sukses");
	}
	
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	@RestaurantTokenRequired
	@RequestMapping(value = "/menu/add", method = RequestMethod.POST)
	public Map<String, Object> addMenumakanan(
			@RequestParam(value = "name") String name,
			@RequestParam(value = "harga") Double harga,
			@RequestParam(value = "kategori") String kategori,
			@RequestParam(value = "deskripsi") String deskripsi
			) 
	{
		String restoIdString=securityService.getUserId();
		Integer restoId=Integer.valueOf(restoIdString);
		
		checkMenuName(name);
		
		Menu menu=new Menu();
		menu.setName(name);
		menu.setHarga(harga);
		menu.setStock(1);
		menu.setDeskripsi(deskripsi);
		menu.setRes_id(restoId);
		menu.setKategori(kategori);
		menu.setStatus(1);
		menu.setCreatedAt(new Date());
		menuDAO.save(menu);
		return Util.getSuccessResult(menu);
	}
	
	@ResponseBody
	@RestaurantTokenRequired
	@RequestMapping(value = "/menu/update", method = RequestMethod.POST)
	public Map<String, Object> updatemenu(
				@RequestParam(value = "id") Integer id,
				@RequestParam(value = "name") String name,
				@RequestParam(value = "harga") Double harga,
				@RequestParam(value = "kategori") String kategori,
				@RequestParam(value = "deskripsi") String deskripsi
			) 
	{
		String restoIdString=securityService.getUserId();
		Integer restoId=Integer.valueOf(restoIdString);
		
		Menu menu =menuDAO.updatemenu(id, name, harga, kategori, deskripsi, restoId);
		
		return Util.getSuccessUpdate(menu);
	}
	
	@ResponseBody
	@RestaurantTokenRequired
	@RequestMapping(value = "/menu/delete", method = RequestMethod.POST)
	public Map<String, Object> deletemenu(
				@RequestParam(value = "id") Integer id
			) 
	{
		String restoIdString=securityService.getUserId();
		Integer restoId=Integer.valueOf(restoIdString);
		menuDAO.deletemenu(id, restoId);
		
		return Util.getSuccessResult("Menu was deleted");
	}
	
	@ResponseBody
	@RestaurantTokenRequired
	@RequestMapping(value = "/menu/outofstock", method = RequestMethod.POST)
	public Map<String, Object> outofstockmenu(
				@RequestParam(value = "id") Integer id
			) 
	{
		String restoIdString=securityService.getUserId();
		Integer restoId=Integer.valueOf(restoIdString);
		menuDAO.outofstockmenu(id, restoId);
		
		return Util.getSuccessResult("Menu updated to out of stock");
	}
	
	@ResponseBody
	@RestaurantTokenRequired
	@RequestMapping("/semuamenu")
	public List<Menu> getAllRestaurantMenu() 
	{
		String restoIdString=securityService.getUserId();
		Integer restoId=Integer.valueOf(restoIdString);
		return menuDAO.findAllRestaurantMenu(restoId);
	}

	
	

}
