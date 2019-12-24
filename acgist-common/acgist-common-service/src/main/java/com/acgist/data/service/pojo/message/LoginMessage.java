package com.acgist.data.service.pojo.message;

import com.acgist.core.pojo.message.ResultMessage;

/**
 * <p>message - 登陆信息</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class LoginMessage extends ResultMessage {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>用户ID</p>
	 */
	private String id;
	/**
	 * <p>用户名称</p>
	 */
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
