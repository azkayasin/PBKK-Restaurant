package com.packtpub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.packtpub.model.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer>{
	@Query("select t from Token t where t.stringToken = :stringToken")
	Token findByStringToken(@Param("stringToken") String stringToken);
}