package com.packtpub.service;

public interface SecurityService {
	String createToken(String subject, long ttlMillis,Integer userId);
	String getSubject(String token);
	String getUserId();
	String getRole();
}
