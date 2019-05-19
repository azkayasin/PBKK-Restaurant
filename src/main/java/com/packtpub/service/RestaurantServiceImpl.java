package com.packtpub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.packtpub.service.RestaurantService;
import com.packtpub.dao.RestaurantDAO;

@Service
public class RestaurantServiceImpl implements RestaurantService{
	@Autowired
	RestaurantDAO restaurantDAO;

	@Override
	public void checkAvailable(int resto_id) {
		if(restaurantDAO.checkAvailable(resto_id)==false)
		{
			throw new IllegalArgumentException("Restaurant telah memiliki data");
		}
		
	}

}
