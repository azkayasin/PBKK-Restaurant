package com.packtpub.repository;

import java.util.List;
//import java.sql.Time;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.packtpub.model.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
	@Query("select u from Restaurant u where u.name = :name")
	Restaurant findByName(@Param("name") String name);
	
	@Query("select u from Restaurant u where u.resto_id = ?1")
	Restaurant findByIdrestaurant(Integer id);
	
	@Query("select u from Restaurant u where u.resto_id = ?1 and u.kondisi = 1")
	Restaurant open(Integer id);
	
	@Query("select u from Restaurant u where u.resto_id = ?1 and u.kondisi = 0")
	Restaurant close(Integer id);
	
	@Query("select u from Restaurant u where u.resto_id= ?1 and u.status = 1")
	Restaurant findByResto_id(Integer resto_id);
	
	//kepake atas
	
	
	

	
}