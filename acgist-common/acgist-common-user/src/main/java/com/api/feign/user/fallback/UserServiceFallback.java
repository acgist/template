package com.api.feign.user.fallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import com.api.core.config.APIConstURL;
import com.api.core.user.pojo.message.AuthoMessage;
import com.api.core.user.pojo.message.LoginMessage;
import com.api.feign.service.BaseServiceFallback;
import com.api.feign.user.service.UserService;

/**
 * 服务熔断 - 用户
 */
@Component
@RequestMapping(APIConstURL.URL_FALLBACK_SERVICE)
public class UserServiceFallback extends BaseServiceFallback implements UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceFallback.class);

	@Override
	public AuthoMessage autho(String name) {
		LOGGER.error("服务调用失败：用户授权，用户名：{}", name);
		return null;
	}

	@Override
	public LoginMessage login(String name, String password) {
		LOGGER.error("服务调用失败：用户登陆，用户名：{}", name);
		LoginMessage message = new LoginMessage();
		message.buildFail();
		return message;
	}
	

}
