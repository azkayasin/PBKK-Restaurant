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

import com.auth0.jwt.interfaces.DecodedJWT;
import com.packtpub.aop.UserTokenRequired;
import com.packtpub.dao.TokenDAO;
import com.packtpub.model.Token;
import com.packtpub.service.SecurityServiceImpl;
import com.packtpub.service.SecurityService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Aspect
@Component
public class UserTokenRequiredAspect {
	@Autowired
	TokenDAO tokenDAO;
	
	@Autowired
	SecurityService securityService;
	
	@Before("@annotation(userTokenRequired)")
	public void tokenRequiredWithAnnotation(UserTokenRequired userTokenRequired) throws Throwable{
		
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
		
		/*Map<String, Claim> claims = verifyToken.getClaims();    //Key is the Claim name
		Claim claim = claims.get("username");
		Claim claimT=verifyToken.getClaim("username");*/
		
		/*Claim role=verifyToken.getClaim("role");
		String roleUser=role.asString();*/
		
		if(verifyToken.getClaim("username") == null){
			throw new IllegalArgumentException("User token is not authorized");
		}		
	}
}
