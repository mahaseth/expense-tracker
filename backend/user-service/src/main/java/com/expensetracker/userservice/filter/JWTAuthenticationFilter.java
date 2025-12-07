package com.expensetracker.userservice.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.expensetracker.userservice.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
//	@Autowired
//	ValidationService validationService;
	private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
	@Autowired
	private JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String header = req.getHeader("Authorization");
		logger.info("header [{}]", header);

		if (header == null || !header.startsWith("Bearer ")) {
			res.setStatus(HttpStatus.FORBIDDEN.value());
			return;
		}
		String token = header.substring(7);
		logger.info("token [{}]", token);
		int statusCode = jwtService.validateToken(token);
		logger.info("statusCode [{}]", statusCode);
		if (statusCode != 200) {
			res.setStatus(statusCode);
			return;
		}

		chain.doFilter(req, res);
	}

}