package com.api.core.user.pojo.message;

import com.acgist.core.pojo.message.ResultMessage;

/**
 * <p>message - 授权信息</p>
 */
public class AuthoMessage extends ResultMessage {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>用户名称</p>
	 */
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
