package com.packtpub.aop;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.packtpub.dao.TokenDAO;
import com.packtpub.model.Token;
import com.packtpub.service.SecurityServiceImpl;
import com.packtpub.service.SecurityService;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.packtpub.aop.RestaurantTokenRequired;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Aspect
@Component
public class RestaurantTokenRequiredAspect {
	@Autowired
	TokenDAO tokenDAO;
	
	@Autowired
	SecurityService securityService;
	
	@Before("@annotation(restaurantTokenRequired)")
	public void tokenRequiredWithAnnotation(RestaurantTokenRequired restaurantTokenRequired) throws Throwable{
		ServletRequestAttributes reqAttributes = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = reqAttributes.getRequest();
		
		// checks for token in request header
		String tokenInHeader = request.getHeader("token");
		
		if(StringUtils.isEmpty(tokenInHeader)){
			throw new IllegalArgumentException("Empty token");
		}
		
		DecodedJWT verifyToken=securityService.verifyToken(tokenInHeader);
		
		if(verifyToken == null){
			throw new IllegalArgumentException("Token Error : Claim is null");
		}
		
		if(verifyToken.getClaim("username") == null || !verifyToken.getClaim("role").asString().equals("Restaurant")){
			throw new IllegalArgumentException("User token is not authorized");
		}	
	}
}
