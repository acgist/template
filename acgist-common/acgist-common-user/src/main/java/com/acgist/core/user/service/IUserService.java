package com.acgist.core.user.service;

import com.acgist.core.user.pojo.message.AuthoMessage;
import com.acgist.core.user.pojo.message.LoginMessage;

/**
 * <p>服务 - 用户</p>
 */
public interface IUserService {

	/**
	 * <p>获取用户授权信息</p>
	 * 
	 * @param name 用户名
	 * 
	 * @return 授权信息
	 */
	AuthoMessage autho(String name);

	/**
	 * <p>登陆</p>
	 * 
	 * @param name 用户名称
	 * @param password 用户密码（已经加密）
	 * 
	 * @return 登陆结果
	 */
	LoginMessage login(String name, String password);
	
}
