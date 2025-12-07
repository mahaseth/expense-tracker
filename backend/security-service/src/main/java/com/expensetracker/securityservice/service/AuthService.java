package com.expensetracker.securityservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.expensetracker.securityservice.dto.AuthRequest;
import com.expensetracker.securityservice.dto.AuthResp;

@Service
public class AuthService {

	@Autowired
	private JwtService jwtService;
	@Autowired
	private AuthenticationManager authenticationManager;
	private static Logger logger = LoggerFactory.getLogger(AuthService.class);
//
//	@Autowired
//	private GitHubAuthService githubAuthService;
//	@Autowired
//	private RestTemplate restTemplate;

	public AuthResp validateAuthRequest(AuthRequest authRequest) {
		AuthResp resp = new AuthResp();
		Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
		if (authenticate.isAuthenticated()) {
			String token = jwtService.generateToken(authRequest.getUsername());
			resp.setRespCode(200);
			resp.setRespDesc("Success");
			resp.setToken(token);
		} else {
			resp.setRespCode(403);
			resp.setRespDesc("Invalid Username");
		}
		return resp;
	}

//	public String generateToken(String username) {
//		return jwtService.generateToken(username);
//	}
//
	public int validateToken(String token) {
		logger.info("token [{}]", token);
		int respCode = jwtService.validateToken(token);
		logger.info("respCode [{}]", respCode);
		return respCode;
	}

//	public AuthResp processCallbackGithub(String code) {
//		logger.info("Code received from github [{}]", code);
//		AuthResp resp = new AuthResp();
//		if (StringUtils.hasLength(code)) {
//			try {
//				String accessToken = githubAuthService.getAccessTokenGitHub(code);
//				logger.info("accessToken received from github [{}]", accessToken);
//				if (accessToken != null) {
//					Map<String, Object> gitHubUserInfo = githubAuthService.getGitHubUserInfo(accessToken);
//					logger.info("User Info received from github [{}]", gitHubUserInfo);
//					if (gitHubUserInfo != null) {
//						String userName = (String) gitHubUserInfo.get("login");
//						String jwtToken = generateToken(userName);
//						resp.setRespCode(200);
//						resp.setRespDesc("Success");
//						resp.setToken(jwtToken);
//
//					} else {
//						resp.setRespCode(403);
//						resp.setRespDesc("Cannot get user information");
//					}
//
//				} else {
//					resp.setRespCode(403);
//					resp.setRespDesc("Cannot get access token from github");
//				}
//			} catch (Exception ex) {
//				logger.error("Exception occured!! ", ex);
//				resp.setRespCode(500);
//				resp.setRespDesc("Error while authenticating with github");
//			}
//		} else {
//			resp.setRespCode(403);
//			resp.setRespDesc("Cannot get auth code from github");
//		}
//		logger.info("resp [{}]", resp);
//		return resp;
//	}

}
