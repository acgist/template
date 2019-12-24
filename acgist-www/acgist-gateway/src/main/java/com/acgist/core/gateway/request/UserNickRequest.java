package com.acgist.core.gateway.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.acgist.core.gateway.gateway.request.GatewayRequest;

/**
 * <p>请求 - 用户昵称修改</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class UserNickRequest extends GatewayRequest {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>用户昵称</p>
	 */
	@Size(max = 20, message = "用户昵称长度不能超过20")
	@NotBlank(message = "用户昵称不能为空")
	private String nick;

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}
	
}
