package com.packtpub.service;

import java.io.IOException;

import com.auth0.jwt.interfaces.DecodedJWT;

public interface SecurityService {
	String createToken(String subject,int roleUser,String name,String username, String email,String phone,Integer userId,long ttlMillis) throws IOException;
	DecodedJWT verifyToken(String token) throws IOException;
	int getUserId() throws IOException;
}
