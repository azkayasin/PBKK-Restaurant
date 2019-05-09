package com.packtpub.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.packtpub.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	@Query("select u from User u where u.id = ?1")
	User findByIduser(Integer id);
	
	@Query("select u from User u where u.userName = :userName and u.status = 1")
	User findByUsername(@Param("userName") String userName);
	
	@Query("select u from User u where u.id = :userId and u.password=:password and u.status = 1")
	User findByUserIdandPassword (@Param("userId") Integer userId,
									@Param("password") String password);
	
	@Query("select u from User u where u.email = :email and u.status = 1")
	User findByEmail(@Param("email") String email);
	
	@Query("select u from User u where u.email = :email and u.password=:password and u.status = 1")
	User findByEmailandPassword(@Param("email") String email,
								@Param("password") String password);
	
	@Query("select u from User u where u.role = 1 and u.status = 1")
	List<User> findCustomer();
	
	@Query("select u from User u where u.role = 2 and u.status = 1")
	List<User> findRestaurant();
}
