package com.acgist.core.service;

import com.acgist.core.pojo.message.ResultMessage;
import com.acgist.data.pojo.entity.UserEntity;
import com.acgist.data.pojo.message.AuthoMessage;
import com.acgist.data.pojo.message.EntityResultMessage;
import com.acgist.data.pojo.message.LoginMessage;

/**
 * <p>服务 - 用户</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public interface IUserService {

	/**
	 * <p>创建用户</p>
	 * 
	 * @param user 用户
	 */
	ResultMessage save(UserEntity user);
	
	/**
	 * <p>获取用户授权信息</p>
	 * 
	 * @param name 用户名
	 * 
	 * @return 授权信息
	 */
	AuthoMessage getAuthoMessage(String name);
	
	/**
	 * <p>根据用户名称查询用户信息</p>
	 * 
	 * @param name 用户名称
	 * 
	 * @return 用户信息
	 */
	EntityResultMessage<UserEntity> findByName(String name);

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
	
	/**
	 * <p>登陆</p>
	 * 
	 * @param name 用户名称
	 * @param password 用户密码（已经加密）
	 * @param type 用户类型
	 * 
	 * @return 登陆结果
	 */
	LoginMessage login(String name, String password, UserEntity.Type type);
	
	/**
	 * <p>验证用户名称是否存在</p>
	 * 
	 * @param name 用户名称
	 * 
	 * @return 是否存在
	 */
	boolean checkUserName(String name);
	
	/**
	 * <p>验证用户邮箱是否存在</p>
	 * 
	 * @param mail 用户邮箱
	 * 
	 * @return 是否存在
	 */
	boolean checkUserMail(String mail);

}
