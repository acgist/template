package com.acgist.core.gateway.gateway.response;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.acgist.core.gateway.response.GatewayResponse;

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
	@Size(max = 40, message = "用户邮箱长度不能超过40")
	@NotBlank(message = "用户邮箱不能为空")
	private String mail;
	/**
	 * <p>用户昵称</p>
	 */
	@Size(max = 20, message = "用户昵称长度不能超过20")
	private String nick;
	/**
	 * <p>用户手机</p>
	 */
	@Size(max = 20, message = "用户手机长度不能超过20")
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
