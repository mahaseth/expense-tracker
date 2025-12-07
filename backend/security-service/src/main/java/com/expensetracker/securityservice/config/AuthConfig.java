package com.expensetracker.securityservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class AuthConfig {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CustomAuthenticationSuccessHandler successHandler;
	@Autowired
	private CustomAuthenticationFailureHandler failureHandler;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(
						requests -> requests
								.requestMatchers("/auth/token", "/auth/validate/**", "/oauth2/**", "/login/**", "/actuator/**")
								.permitAll().anyRequest().authenticated())
				.oauth2Login(oauth2Login -> oauth2Login
						.loginPage("/oauth2/authorization/google")
						.successHandler(successHandler)
						.failureHandler(failureHandler))
				.build();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("user1").password("user1pass").roles("ADMIN");
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
