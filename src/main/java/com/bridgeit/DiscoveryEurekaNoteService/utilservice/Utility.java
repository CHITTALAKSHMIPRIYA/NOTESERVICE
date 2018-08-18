package com.bridgeit.DiscoveryEurekaNoteService.utilservice;

import java.util.Date;

import org.springframework.stereotype.Component;




import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author LAKSHMI PRIYA
 * @since DATE:10-07-2018
 *        <p>
 *        <b>Utility class to validate user details and to create token.</b>
 *        </p>
 */
@Component
public class Utility {

	/**
	 * @param id
	 * @return
	 */
	public static String createToken(String id) {
		final String KEY = "lakshmi";
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		Date startTime = new Date();
		Date expireTime = new Date(startTime.getTime() + (24 * 60 * 60 * 1000));

		JwtBuilder builder = Jwts.builder().setId(id).setIssuedAt(startTime).setExpiration(expireTime)
				.signWith(signatureAlgorithm, KEY);
		return builder.compact();
	}

	/**
	 * @param jwt
	 * @return
	 */
	public static Claims parseJwt(String jwt) {
		final String KEY = "lakshmi";
		return Jwts.parser().setSigningKey(KEY).parseClaimsJws(jwt).getBody();
	}

	
}
