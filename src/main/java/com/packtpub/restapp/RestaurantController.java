package com.packtpub.restapp;

import java.util.Date;
import java.io.IOException;
import java.sql.Time;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.packtpub.aop.AdminTokenRequired;
import com.packtpub.aop.RestaurantTokenRequired;
import com.packtpub.aop.UserTokenRequired;
import com.packtpub.dao.TokenDAO;
import com.packtpub.dao.MenuDAO;
import com.packtpub.dao.RestaurantDAO;
import com.packtpub.model.Menu;
import com.packtpub.model.Restaurant;
import com.packtpub.model.User;
import com.packtpub.property.FileStorageProperties;
import com.packtpub.service.FileStorageService;
import com.packtpub.service.RestaurantService;
import com.packtpub.service.SecurityService;
import com.packtpub.util.Util;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
@RestController
@RequestMapping("/restaurant")
public class RestaurantController {
	
	@Autowired
	RestaurantDAO restaurantDAO;
	
	@Autowired
	FileStorageService fstorage;
	
	@Autowired
	TokenDAO tokenDAO;
	
	@Autowired
	MenuDAO menuDAO;
	
	@Autowired
	RestaurantService restaurantService;

	
	@Autowired
	SecurityService securityService;
	
	public void checkMenuName(String name, Integer Id)
	{
		if(menuDAO.checkName(name, Id)==false)
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
	}
	
	@ResponseBody
	@RestaurantTokenRequired
	@ResponseStatus(value = HttpStatus.CREATED)
	@RequestMapping(value = "/data", method = RequestMethod.POST)
	public Map<String, Object> registerRestaurant(
				@RequestParam(value = "name") String name,
				@RequestParam(value = "alamat") String alamat,
				@RequestParam(value = "deskripsi") String deskripsi,
				@RequestParam(value = "jam_buka") Time buka,
				@RequestParam(value = "jam_tutup") Time tutup,
				@RequestParam(value = "kategori") String kategori
			) throws IOException 
	{	
		int restoId=securityService.getUserId();
		restaurantService.checkAvailable(restoId);
		Restaurant res=new Restaurant();
		res.setName(name);
		res.setAlamat(alamat);
		res.setDeskripsi(deskripsi);
		res.setStatus(1);
		res.setBuka(buka);
		res.setTutup(tutup);
		res.setKategori(kategori);
		res.setResto_id(restoId);
		res.setKondisi(0);
		res.setCreatedAt(new Date());
		restaurantDAO.save(res);
		return Util.getSuccessResult(res);
	}
	
	@ResponseBody
	@UserTokenRequired
	@RequestMapping("/lists")
	public List<Restaurant> getAllRestaurant() 
	{
		return restaurantDAO.findAllRestaurant();
	}
	
	@ResponseBody
	@RestaurantTokenRequired
	@RequestMapping(value = "/deal", method = RequestMethod.POST)
	public Map<String, Object> setDeals(
			@RequestParam(value = "deal") String deal) throws IOException 
	{
		int restoId=securityService.getUserId();
		restaurantDAO.dealRestaurant(restoId, deal);
		
		return Util.getSuccessResult("Restaurant deal success");
	}
	
	
	@ResponseBody
	@RestaurantTokenRequired
	@RequestMapping(value = "/open", method = RequestMethod.POST)
	public Map<String, Object> openRestaurant() throws IOException 
	{
		int restoId=securityService.getUserId();
		restaurantDAO.openRestaurant(restoId);
		
		
		
		return Util.getSuccessResult("Restaurant open success");
	}
	
	@ResponseBody
	@RestaurantTokenRequired
	@RequestMapping(value = "/close", method = RequestMethod.POST)
	public Map<String, Object> closeRestaurant() throws IOException 
	{
		int restoId=securityService.getUserId();
		
		restaurantDAO.closeRestaurant(restoId);
		
		
		
		return Util.getSuccessResult("Restaurant close success");
	}
	@ResponseBody
	@RestaurantTokenRequired
	@RequestMapping(value = "/updatedata", method = RequestMethod.PUT)
	public Map<String, Object> updatedata(
				@RequestParam(value = "name") String name,
				@RequestParam(value = "alamat") String alamat,
				@RequestParam(value = "deskripsi") String deskripsi,
				@RequestParam(value = "jam_buka") Time buka,
				@RequestParam(value = "jam_tutup") Time tutup,
				@RequestParam(value = "kategori") String kategori
			) throws IOException 
	{
		int restoId=securityService.getUserId();
		
		restaurantDAO.updateRestaurant(restoId, name, alamat, deskripsi, kategori, buka, tutup);
		
		return Util.getSuccessResult("Data was updated");
	}
	
	
	@ResponseBody
	@AdminTokenRequired
	@RequestMapping(value = "/deactive/{id}", method = RequestMethod.POST)
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
		
		restaurantDAO.deactiveRestaurant(id);
		
		return Util.getSuccessResult("Deactive restaurant success");
	}
	
	@ResponseBody
	@ResponseStatus(value = HttpStatus.CREATED)
	@RestaurantTokenRequired
	@RequestMapping(value = "/menu/add", method = RequestMethod.POST)
	public Map<String, Object> addMenumakanan(
			@RequestParam(value = "name") String name,
			@RequestParam(value = "harga") Double harga,
			@RequestParam(value = "kategori") String kategori,
			@RequestParam(value = "deskripsi") String deskripsi,
			@RequestParam(value = "gambar") MultipartFile file
			) throws IOException 
	{
		int restoId=securityService.getUserId();
		
		checkMenuName(name,restoId);

		String fileName = fstorage.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("restaurant/upload/images/")
                .path(fileName)
                .toUriString();

//        return new UploadFileResponse(fileName, fileDownloadUri,
//                file.getContentType(), file.getSize());
		
		Menu menu=new Menu();
		menu.setName(name);
		menu.setHarga(harga);
		menu.setStock(1);
		menu.setGambar(fileDownloadUri);
		menu.setDeskripsi(deskripsi);
		menu.setRes_id(restoId);
		menu.setKategori(kategori);
		menu.setStatus(1);
		menu.setCreatedAt(new Date());
		menuDAO.save(menu);
		return Util.getSuccessResult(menu);
	}
	
	@RequestMapping(value = "/upload/images/{filename:.+}", method = RequestMethod.GET)
	public ResponseEntity<Resource> getImage(@PathVariable String filename) {
		Resource resource = fstorage.loadFileAsResource(filename);
		return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
	}
	
	@ResponseBody
	@RestaurantTokenRequired
	@RequestMapping(value = "/menu/update", method = RequestMethod.PUT)
	public Map<String, Object> updatemenu(
				@RequestParam(value = "id") Integer id,
				@RequestParam(value = "name") String name,
				@RequestParam(value = "harga") Double harga,
				@RequestParam(value = "kategori") String kategori,
				@RequestParam(value = "deskripsi") String deskripsi
			) throws IOException 
	{
		int restoId=securityService.getUserId();
		
		menuDAO.updatemenu(id, name, harga, kategori, deskripsi, restoId);
		
		return Util.getSuccessResult("Menu Was Updated");
	}
	
	@ResponseBody
	@RestaurantTokenRequired
	@RequestMapping(value = "/menu/delete/{id}", method = RequestMethod.POST)
	public Map<String, Object> deletemenu(
			@PathVariable("id") Integer id
			) throws IOException 
	{
		int restoId=securityService.getUserId();
		menuDAO.deletemenu(id, restoId);
		
		return Util.getSuccessResult("Menu was deleted");
	}
	
	@ResponseBody
	@RestaurantTokenRequired
	@RequestMapping(value = "/menu/outofstock/{id}", method = RequestMethod.POST)
	public Map<String, Object> outofstockmenu(
			@PathVariable("id") Integer id
			) throws IOException 
	{
		int restoId=securityService.getUserId();
		menuDAO.outofstockmenu(id, restoId);
		
		return Util.getSuccessResult("Menu updated to out of stock");
	}
	
	@ResponseBody
	@RestaurantTokenRequired
	@RequestMapping("/menu/allmenu")
	public List<Menu> getAllRestaurantMenu() throws IOException 
	{
		int restoId=securityService.getUserId();
		return menuDAO.findAllRestaurantMenu(restoId);
	}
	
	@ResponseBody
	@UserTokenRequired
	@RequestMapping("/menu/{id}")
	public List<Menu> getAllUserMenu(
			@PathVariable("id") Integer id
			) throws IOException 
	{
		return menuDAO.findAllRestaurantMenu(id);
	}

	
	

}
