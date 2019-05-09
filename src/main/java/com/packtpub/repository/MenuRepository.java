package com.packtpub.repository;
import java.util.List;
//import java.sql.Time;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.packtpub.model.Menu;
import com.packtpub.model.Restaurant;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
	
	@Query("select u from Menu u where u.res_id = ?1")
	Menu findByRestaurant(Integer id);
	
	@Query("select u from Menu u where u.id = ?1")
	Menu findByIdmenu(Integer id);
	
	@Query("select u from Menu u where u.name = :name")
	Menu findByName(String name);
	
	@Query("select u from Menu u where u.id = ?1 and u.status = 1")
	Menu menuaktif(Integer id);
	
	@Query("select u from Menu u where u.id = ?1 and u.stock = 1")
	Menu menuoutofstock(Integer id);
	
	@Query("select u from Menu u where u.res_id = ?1")
	List<Menu> findAll(Integer restoId);

}
