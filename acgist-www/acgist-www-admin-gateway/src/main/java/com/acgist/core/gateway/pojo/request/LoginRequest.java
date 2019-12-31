package com.acgist.core.gateway.pojo.request;

import com.acgist.core.pojo.Pojo;

/**
 * <p>请求 - 登陆</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class LoginRequest extends Pojo {

	private static final long serialVersionUID = 1L;

	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
