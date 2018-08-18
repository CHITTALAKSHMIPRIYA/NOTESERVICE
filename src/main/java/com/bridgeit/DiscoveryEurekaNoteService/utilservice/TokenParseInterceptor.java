
package com.bridgeit.DiscoveryEurekaNoteService.utilservice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import io.jsonwebtoken.Claims;

/**
 * @author LAKSHMI PRIYA
 * @since DATE:10-07-2018
 *        <p>
 *        <b>A POJO class with the user details.</b>
 *        </p>
 */
/*@Component
public class TokenParseInterceptor implements HandlerInterceptor {
	@Autowired
	Utility utility;
	public static final Logger logger = LoggerFactory.getLogger(TokenParseInterceptor.class);

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String token = request.getHeader("token");
		System.out.println(token);
		Claims claim = Utility.parseJwt(token);
		System.out.println(claim.getId());
		request.setAttribute("userId", claim.getId());
		return true;
	}
}*/
