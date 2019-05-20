package com.packtpub.dao;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.packtpub.model.Menu;
import com.packtpub.model.Restaurant;
import com.packtpub.model.User;
import com.packtpub.repository.MenuRepository;
import com.packtpub.util.Util;
@Service
public class MenuDAO {
	
	@Autowired
	MenuRepository menuRepository;
	
	public Menu save(Menu menu) {
		return menuRepository.save(menu);
	}
	
	public List<Menu> findAll()
	{
		return menuRepository.findAll();
	}
	
	public Menu findById(Integer id)
	{
		Menu menu=menuRepository.findByIdmenu(id);
		if(menu!=null)
		{
			return new Menu(menu.getId(),menu.getName(),menu.getHarga(),menu.getStock(),menu.getDeskripsi(),menu.getRes_id(),menu.getKategori(),menu.getStatus());
		}
		return null;
	}

	public boolean checkName(String name, Integer Id) {
		Menu menu=menuRepository.findByName(name, Id);
		if(menu == null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public Menu updatemenu(Integer id, String name, Double harga, String kategori, String deskripsi, Integer restoId) {
		Menu menu=menuRepository.findByIdmenu(id);
		if(menu!=null)
		{
			if(menu.getRes_id() == restoId)
			{
				if(!StringUtils.isEmpty(name)) {
					menu.setName(name);
				}
				if(harga != null) {
					menu.setHarga(harga);
				}
				if(!StringUtils.isEmpty(kategori)) {
					menu.setKategori(kategori);
				}
				if(!StringUtils.isEmpty(deskripsi)) {
					menu.setDeskripsi(deskripsi);
				}
				menuRepository.save(menu);
			}
			else
			{
				throw new IllegalArgumentException("you dont have access for this menu");
			}
				
			
		}
		else
		{
			throw new IllegalArgumentException("Menu is not found");
		}
		return menu;
		
	}

	public void deletemenu(Integer id, Integer restoId) {
		Menu menu=menuRepository.menuaktif(id); 
		if(menu!=null)
		{
			if(menu.getRes_id().equals(restoId))
			{
				menu.setStatus(0);
				menuRepository.save(menu);
			}
			else
			{
//				String resto = Integer.toString(restoId);
//				String tampung2 = Integer.toString(menu.getRes_id());
//				String myString = resto +"batas" +tampung2;
				throw new IllegalArgumentException("you dont have access for this menu");
			}
			
		}
		else
		{
			throw new IllegalArgumentException("Menu Is not Found");
		}
		
		
	}

	public void outofstockmenu(Integer id, Integer restoId) {
		Menu menu=menuRepository.menuoutofstock(id);
		if(menu!=null)
		{
			if(menu.getRes_id().equals(restoId))
			{
				menu.setStock(0);
				menuRepository.save(menu);
			}
			else
			{
				throw new IllegalArgumentException("you dont have access for this menu");
			}
			
		}
		else
		{
			throw new IllegalArgumentException("Menu Is already out ofstock");
		}
		
	}

	public List<Menu> findAllRestaurantMenu(Integer restoId) {
		
		return menuRepository.findAll(restoId);
	}

}
