package com.acgist.core.gateway.service;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.gateway.gateway.response.UserResponse;
import com.acgist.core.gateway.response.GatewayResponse;
import com.acgist.core.service.IUserService;

@Service
public class UserService extends GatewayService {
	
	@Reference(version = "${acgist.service.version}")
	private IUserService userService;
	
	public GatewayResponse select() {
		final var session = this.gatewaySession();
		final var request = session.getRequest();
		final var response = (UserResponse) session.getResponse();
		final var user = this.userService.findByName(request.getUsername());
		response.setMail(user.getMail());
		response.setNick(user.getNick());
		response.setMobile(user.getMobile());
		return session.buildResponse(AcgistCode.CODE_0000);
	}
	
}
