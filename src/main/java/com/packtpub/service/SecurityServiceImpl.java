package com.packtpub.service;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.packtpub.dao.TokenDAO;
import com.packtpub.model.Token;
import com.packtpub.service.RSAKeysLoader;
import com.packtpub.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class SecurityServiceImpl implements SecurityService{
	public static final String secretKey= "4C8okt4LxyKWYLM78sKdXrzbBjDCFyfXUm9sZSI6IkN1c3RvbWVyIiwiVXNlcm5hbWUiOiJ3";
	private static final String ISSUER = "tcdelivery";
	
	
	@Autowired
	TokenDAO tokenDAO;
	
	@Autowired
	UserService userService;
	
	@Override
	public String createToken(String subject,int roleUser,String name,String username, String email,String phone,Integer userId,long ttlMillis) throws IOException {
		if (ttlMillis <= 0) {
			throw new RuntimeException("Expiry time must be greater than Zero :["+ttlMillis+"] ");
		}
		
		String role=userService.getRoleName(roleUser);
		
		RSAPrivateKey privateKey=RSAKeysLoader.createPrivateKeyPKCS1Format(readFile("key/jwtRS256.key")); //Get the key instance
		
		try {
			Algorithm algorithmRS = Algorithm.RSA256(null, privateKey);
			
			long nowMillis = System.currentTimeMillis();
			
			String token=JWT.create()
						.withIssuer(ISSUER)
						.withClaim("name", name)
						.withClaim("role", role)
						.withClaim("username", username)
						.withClaim("userid", userId)
						.withClaim("email", email)
						.withClaim("phone", phone)
						.withExpiresAt(new Date(nowMillis + ttlMillis))
						.sign(algorithmRS);		
			
			//Save to DB
			Token tokenSave=new Token();
			tokenSave.setIdUser(userId);
			tokenSave.setStringToken(token);
			tokenSave.setCreatedAt(new Date());
			tokenSave.setExpiredAt(new Date(nowMillis+ttlMillis));
			tokenDAO.save(tokenSave);

			return token;
        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
            return null;
        }
	}
	
	private String readFile(String fileName) throws IOException{
		
		InputStream input = new ClassPathResource(fileName).getInputStream();
        byte[] bytes = IOUtils.toByteArray(input);
        return new String(bytes);
	}
	
	@Override
	public int getUserId() throws IOException
	{
		ServletRequestAttributes reqAttributes = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = reqAttributes.getRequest();

		// checks for token in request header
		String token = request.getHeader("token");
		
		try
		{
			RSAPublicKey publicKey=RSAKeysLoader.createPublicKeyPKCS1Format(readFile("key/jwtRS256.key"));//Get the key instance
			Algorithm algorithmRSCheck = Algorithm.RSA256(publicKey, null);
			
			JWTVerifier verifier = JWT.require(algorithmRSCheck)
					.withIssuer(ISSUER)
					.build(); //Reusable verifier instance
			DecodedJWT jwt = verifier.verify(token);
			Claim userid=jwt.getClaim("userid");
			int userids=userid.asInt();
			return userids;
		}
		catch (JWTVerificationException exception){
		    //Invalid signature/claims
			return 0;
		}	
	}
	
	@Override
	public DecodedJWT verifyToken(String token) throws IOException {
		try
		{
			RSAPublicKey publicKey=RSAKeysLoader.createPublicKeyPKCS1Format(readFile("key/jwtRS256.key"));//Get the key instance
			Algorithm algorithmRSCheck = Algorithm.RSA256(publicKey, null);
			
			JWTVerifier verifier = JWT.require(algorithmRSCheck)
					.withIssuer(ISSUER)
					.build(); //Reusable verifier instance
			return verifier.verify(token);
		}
		catch (JWTVerificationException exception){
		    //Invalid signature/claims
			return null;
		}	
	}
}
