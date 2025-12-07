package com.expensetracker.securityservice.config;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.expensetracker.securityservice.dto.AuthResp;
import com.expensetracker.securityservice.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Autowired
	JwtService jwtService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {
		OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
		Map<String, Object> userAttributes = oauth2User.getAttributes();

		AuthResp resp = new AuthResp();
		resp.setRespCode(200);
		resp.setRespDesc("Success");
		String token = jwtService.generateToken((String) userAttributes.get("email"));
		resp.setToken(token);
		response.setContentType("application/json");
		response.getWriter().write(new ObjectMapper().writeValueAsString(resp));
	}
}
