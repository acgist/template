package com.acgist.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * <p>控制器 - 用户</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Controller
public class UserContoller {

	@GetMapping("/login")
	public String login() {
		return "/login";
	}

	@GetMapping("/login")
	public String login(String pas) {
		return null;
	}
	
	@GetMapping("/register")
	public String register() {
		return "/register";
	}
	
	@PostMapping("/register")
	public String register(String pas) {
		return null;
	}
	
}
