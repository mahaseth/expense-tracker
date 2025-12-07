package com.expensetracker.securityservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

  @GetMapping("/token")
  public String token() {
    return "token";
  }

  @GetMapping("/validate")
  public String validate() {
    return "validate";
  }
}
