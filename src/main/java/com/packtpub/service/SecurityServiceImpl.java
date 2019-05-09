package com.packtpub.service;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.packtpub.dao.TokenDAO;
import com.packtpub.model.Token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class SecurityServiceImpl implements SecurityService{
	public static final String secretKey= "4C8okt4LxyKWYLM78sKdXrzbBjDCFyfX";
	
	@Autowired
	TokenDAO tokenDAO;
	
	@Override
	public String createToken(String subject, long ttlMillis,Integer userId) {
		if (ttlMillis <= 0) {
			throw new RuntimeException("Expiry time must be greater than Zero :["+ttlMillis+"] ");
		}
		
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		// The JWT signature algorithm we will be using to sign the token
		long nowMillis = System.currentTimeMillis();

		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		JwtBuilder builder = Jwts.builder()
				.setSubject(subject)				
				.signWith(signatureAlgorithm, signingKey);
		
		builder.setExpiration(new Date(nowMillis + ttlMillis));
		
		String token=builder.compact();
		
		//Save to DB
		Token tokenSave=new Token();
		tokenSave.setIdUser(userId);
		tokenSave.setStringToken(token);
		tokenSave.setCreatedAt(new Date());
		tokenSave.setExpiredAt(new Date(nowMillis+ttlMillis));
		tokenDAO.save(tokenSave);

		return token;
	}
	
	@Override
	public String getSubject(String token) {
		Claims claims = Jwts.parser() .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
				.parseClaimsJws(token).getBody();
		return claims.getSubject();
	}
	
	@Override
	public String getUserId()
	{
		ServletRequestAttributes reqAttributes = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = reqAttributes.getRequest();

		// checks for token in request header
		String tokenInHeader = request.getHeader("token");
		
		Claims claims = Jwts.parser() .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
				.parseClaimsJws(tokenInHeader).getBody();
		String subject=claims.getSubject();
		
		return subject.split("=")[0];
	}
	
	@Override
	public String getRole()
	{
		ServletRequestAttributes reqAttributes = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = reqAttributes.getRequest();

		// checks for token in request header
		String tokenInHeader = request.getHeader("token");
		
		Claims claims = Jwts.parser() .setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
				.parseClaimsJws(tokenInHeader).getBody();
		String subject=claims.getSubject();
		
		return subject.split("=")[1];
	}
}
