package com.acgist.data.service.pojo.message;

import com.acgist.core.pojo.message.ResultMessage;

/**
 * <p>message - 授权信息</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class AuthoMessage extends ResultMessage {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>用户名称</p>
	 */
	private String name;
	/**
	 * <p>用户密码</p>
	 */
	private String password;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * <p>生成缓存Key</p>
	 * 
	 * @param name 用户名称
	 * 
	 * @return key
	 */
	public static final String buildCacheKey(String name) {
		return "AuthoMessage:" + name;
	}
	
}