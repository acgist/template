package com.acgist.core.gateway.controller;

import java.security.PrivateKey;
import java.util.concurrent.TimeUnit;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.core.gateway.pojo.request.LoginRequest;
import com.acgist.core.pojo.message.DataResultMessage;
import com.acgist.core.service.IUserService;
import com.acgist.data.pojo.entity.UserEntity;
import com.acgist.data.pojo.message.AuthoMessage;
import com.acgist.data.pojo.message.LoginMessage;
import com.acgist.data.service.RedisService;
import com.acgist.utils.RsaUtils;
import com.acgist.utils.UuidUtils;

/**
 * <p>controller - admin</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@RestController
public class AdminController {

	@Value("${acgist.permission.duration:30}")
	private int duration;
	@Autowired
	private PrivateKey privateKey;
	
	@Reference(version = "${acgist.service.version}")
	private IUserService userService;
	@Autowired
	private RedisService redisService;
	
	@PostMapping("/login")
	public DataResultMessage login(@RequestBody LoginRequest login) {
		String username = login.getUsername();
		String password = login.getPassword();
		password = RsaUtils.decrypt(this.privateKey, password);
		final DataResultMessage message = new DataResultMessage();
		final LoginMessage loginMessage = this.userService.login(username, password, UserEntity.Type.ADMIN);
		if(loginMessage.fail()) {
			message.buildMessage(loginMessage);
			return message;
		}
		// 生成权限信息
		final String token = UuidUtils.buildUuid();
		final AuthoMessage authoMessage = this.userService.getAuthoMessage(username);
		this.redisService.store(token, authoMessage, this.duration, TimeUnit.MINUTES);
		message.buildSuccess();
		message.put("token", token);
		return message;
	}
	
}
