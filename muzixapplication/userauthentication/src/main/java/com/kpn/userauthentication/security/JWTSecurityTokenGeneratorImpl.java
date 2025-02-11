package com.kpn.userauthentication.security;

import com.kpn.userauthentication.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
@Service
public class JWTSecurityTokenGeneratorImpl implements SecurityTokenGenerator{
	public String createToken(User user){
		return generateToken(null,user.getEmail());
		// Write logic to create the Jwt
	}

	public String generateToken(Map<String,Object> claims,String subject) {
		// Generate the token and set the user id in the claims
		String jwtToken=Jwts.builder().setSubject(subject).
				setIssuedAt(new Date()).
				signWith(SignatureAlgorithm.HS256,"secretekey").
				compact();

		return jwtToken;
	}

}
