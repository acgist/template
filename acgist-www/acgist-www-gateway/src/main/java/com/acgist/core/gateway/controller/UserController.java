package com.acgist.core.gateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.core.gateway.response.GatewayResponse;
import com.acgist.core.gateway.service.UserService;

/**
 * <p>控制器 - 用户</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@RestController
@RequestMapping("/gateway/user")
public final class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping
	public GatewayResponse select() {
		return this.userService.select();
	}
	
}
