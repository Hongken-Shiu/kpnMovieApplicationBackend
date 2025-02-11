package com.kpn.movieservice.filter;

import io.jsonwebtoken.Jwts;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends GenericFilter {
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		// Parse and validate the token and set the user id from claims in the request header as an attribute.

		String authHeader = request.getHeader("Authorization");
		if (authHeader == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			ServletOutputStream outputStream = response.getOutputStream();
			outputStream.println("Token Missing");
		} else {
			String jwtToken = authHeader.substring(7);
			String userdetail = Jwts.parser().setSigningKey("secretekey").parseClaimsJws(jwtToken).getBody().getSubject();
			System.out.println(userdetail);
			request.setAttribute("userId", userdetail);
			filterChain.doFilter(servletRequest, servletResponse);
		}
	}
}