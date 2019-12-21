package com.api.core.user.pojo.message;

import com.api.core.pojo.message.BaseMessage;

/**
 * 授权信息
 */
public class AuthoMessage extends BaseMessage {

	private static final long serialVersionUID = 1L;

	private String name;
	private String pubilcKey;
	private String privateKey;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPubilcKey() {
		return pubilcKey;
	}

	public void setPubilcKey(String pubilcKey) {
		this.pubilcKey = pubilcKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

}
