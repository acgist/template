package com.acgist.core.service;

import com.acgist.core.pojo.message.ResultMessage;
import com.acgist.data.service.pojo.entity.UserEntity;
import com.acgist.data.service.pojo.message.LoginMessage;
import com.acgist.data.service.pojo.message.AuthoMessage;
import com.acgist.data.service.pojo.message.UserMessage;

/**
 * <p>服务 - 用户</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public interface IUserService {

	/**
	 * <p>获取用户授权信息</p>
	 * 
	 * @param name 用户名
	 * 
	 * @return 授权信息
	 */
	AuthoMessage permission(String name);
	
	/**
	 * <p>根据用户名称查询用户信息</p>
	 * 
	 * @param name 用户名称
	 * 
	 * @return 用户信息
	 */
	UserMessage findByName(String name);

	/**
	 * <p>修改用户信息</p>
	 * 
	 * @param userEntity 用户
	 * 
	 * @return 修改结果
	 */
	ResultMessage update(UserEntity userEntity);
	
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
