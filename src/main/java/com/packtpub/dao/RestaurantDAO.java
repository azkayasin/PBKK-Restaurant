package com.packtpub.dao;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.packtpub.model.Restaurant;
import com.packtpub.model.User;
import com.packtpub.repository.RestaurantRepository;
import com.packtpub.util.Util;

@Service
public class RestaurantDAO {
	
	@Autowired
	RestaurantRepository restaurantRepository;
	
	public Restaurant save(Restaurant res) {
		return restaurantRepository.save(res);
	}
	
	public List<Restaurant> findAll()
	{
		return restaurantRepository.findAll();
	}
	
	public Restaurant findById(Integer id)
	{
		Restaurant res=restaurantRepository.findByIdrestaurant(id);
		if(res!=null)
		{
			return new Restaurant(res.getId(),res.getName(),res.getAlamat(),res.getTelefon(),res.getEmail(),res.getDeskripsi(),res.getBuka(),res.getTutup(),res.getKondisi(),res.getStatus());
		}
		return null;
	}
	
	public Restaurant login(String email,String password)
	{
		Restaurant resto=restaurantRepository.findByEmailandPassword(email, DigestUtils.sha256Hex(password));
		if(resto==null)
		{
			throw new IllegalArgumentException("Email or Password is Not Match");
		}
		return resto;
	}
	
	public boolean checkEmail(String email)
	{
		Restaurant checkResto=restaurantRepository.findByEmail(email);
		if(checkResto==null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean checkName(String name)
	{
		Restaurant checkResto=restaurantRepository.findByName(name);
		if(checkResto == null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public void verifikasiRestaurant(Integer Id)
	{
		Restaurant resto=restaurantRepository.findByIdrestaurant(Id);
		if(resto!=null)
		{
			resto.setStatus(1);
			restaurantRepository.save(resto);
		}
	}
	
	public void openRestaurant(Integer Id)
	{
		Restaurant resto=restaurantRepository.findByIdrestaurant(Id);
		Restaurant buka=restaurantRepository.open(Id);
		if (buka != null)
		{
			throw new IllegalArgumentException("Restaurant is already open");
		}
		if(resto!=null)
		{
			resto.setKondisi(1);
			restaurantRepository.save(resto);
		}
	}
	
	public void closeRestaurant(Integer Id)
	{
		Restaurant resto=restaurantRepository.findByIdrestaurant(Id);
		Restaurant tutup=restaurantRepository.close(Id);
		if (tutup != null)
		{
			throw new IllegalArgumentException("Restaurant is already closed");
		}
		if(resto!=null)
		{
			resto.setKondisi(0);
			restaurantRepository.save(resto);
		}
	}

	public void updateRestaurant(Integer restoId, String alamat, String telefon, String deskripsi, String kategori,
			Time buka, Time tutup) {
		Restaurant resto=restaurantRepository.findByIdrestaurant(restoId);
		if(resto!=null)
		{
			if(alamat != null && alamat != "") {
				resto.setAlamat(alamat);
			}
			if(telefon != null && telefon != "") {
				resto.setTelefon(telefon);
			}
			if(deskripsi != null &&  deskripsi != "") {
				resto.setDeskripsi(deskripsi);
			}
			if( kategori != null &&  kategori != "") {
				resto.setKategori(kategori);
			}
			if( buka != null) {
				resto.setBuka(buka);
			}
			if( tutup != null) {
				resto.setTutup(tutup);
			}
			
			
			restaurantRepository.save(resto);
		}
		
	}
}
