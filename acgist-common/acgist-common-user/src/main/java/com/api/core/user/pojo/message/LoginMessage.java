package com.api.core.user.pojo.message;

import com.api.core.pojo.message.ResultMessage;

/**
 * 登陆信息
 */
public class LoginMessage extends ResultMessage {

	private static final long serialVersionUID = 1L;

	private String id;
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
