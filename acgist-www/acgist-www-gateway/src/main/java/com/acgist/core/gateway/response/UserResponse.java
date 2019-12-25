package com.acgist.core.gateway.response;

import javax.validation.constraints.NotBlank;

/**
 * <p>响应 - 用户信息查询</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class UserResponse extends GatewayResponse {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>用户邮箱</p>
	 */
	@NotBlank(message = "用户邮箱不能为空")
	private String mail;
	/**
	 * <p>用户昵称</p>
	 */
	private String nick;
	/**
	 * <p>用户手机</p>
	 */
	private String mobile;

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
}
