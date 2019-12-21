package com.api.core.user.service;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.api.core.config.APIConstURL;
import com.api.core.user.config.APIConstUserURL;
import com.api.core.user.pojo.message.AuthoMessage;
import com.api.core.user.pojo.message.LoginMessage;

/**
 * 服务 - 用户
 */
@RequestMapping(APIConstURL.URL_SERVICE)
public interface IUserService {

	/**
	 * 获取用户接口鉴权信息
	 * @param name 用户名
	 * @return 鉴权信息
	 */
	@PostMapping(APIConstUserURL.URL_USER_AUTHO)
	AuthoMessage autho(@RequestParam String name);

	/**
	 * 登陆
	 * @param name 用户名称
	 * @param password 用户密码
	 * @return 登陆结果
	 */
	@PostMapping(APIConstUserURL.URL_USER_LOGIN)
	LoginMessage login(@RequestParam String name, @RequestParam String password);
	
}
