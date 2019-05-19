package com.packtpub.dao;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
			return new Restaurant(res.getId(),res.getName(),res.getAlamat(),res.getResto_id(),res.getDeskripsi(),res.getBuka(),res.getTutup(),res.getKondisi(),res.getStatus());
		}
		return null;
	}
	
	
	
	
	public boolean checkAvailable(Integer resto_id)
	{
		Restaurant checkResto=restaurantRepository.findByResto_id(resto_id);
		if(checkResto == null)
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
	public void deactiveRestaurant(Integer Id)
	{
		Restaurant resto=restaurantRepository.findByIdrestaurant(Id);
		if(resto!=null)
		{
			resto.setStatus(0);
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
		else if(resto!=null)
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

	public void updateRestaurant(Integer restoId, String name, String alamat, String deskripsi, String kategori,
			Time buka, Time tutup) {
		Restaurant resto=restaurantRepository.findByIdrestaurant(restoId);
		if(resto!=null)
		{
			if(!StringUtils.isEmpty(name)) {
				resto.setName(name);
			}
			if(!StringUtils.isEmpty(alamat)) {
				resto.setAlamat(alamat);
			}
			if(!StringUtils.isEmpty(deskripsi)) {
				resto.setDeskripsi(deskripsi);
			}
			if(!StringUtils.isEmpty(kategori)) {
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
//		return resto;
		
	}
}
