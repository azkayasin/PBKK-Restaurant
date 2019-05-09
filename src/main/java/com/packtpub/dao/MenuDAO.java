package com.packtpub.dao;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
			return new Menu(menu.getId(),menu.getName(),menu.getHarga(),menu.getStock(),menu.getDeskripsi(),menu.getRes_id(),menu.getkKategori(),menu.getStatus());
		}
		return null;
	}

	public boolean checkName(String name) {
		Menu menu=menuRepository.findByName(name);
		if(menu == null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public void updatemenu(Integer id, String name, Double harga, String kategori, String deskripsi, Integer restoId) {
		Menu menu=menuRepository.findByIdmenu(id);
		if(menu!=null)
		{
			if(menu.getRes_id() == restoId)
			{
				if(name != null && name != "") {
					menu.setName(name);
				}
				if(harga != null) {
					menu.setHarga(harga);
				}
				if(kategori != null &&  kategori != "") {
					menu.setKategori(kategori);
				}
				if( deskripsi != null &&  deskripsi != "") {
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
		
	}

	public void deletemenu(Integer id, Integer restoId) {
		Menu menu=menuRepository.menuaktif(id);
		if(menu!=null)
		{
			if(menu.getRes_id() == restoId)
			{
				menu.setStatus(0);
				menuRepository.save(menu);
			}
			else
			{
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
			if(menu.getRes_id() == restoId)
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
