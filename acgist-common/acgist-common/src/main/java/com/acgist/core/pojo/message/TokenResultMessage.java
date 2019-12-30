package com.acgist.core.pojo.message;

/**
 * <p>message - Token消息</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class TokenResultMessage extends ResultMessage {

	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>Token</p>
	 */
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
