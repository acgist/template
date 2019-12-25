package com.acgist.core.gateway.request;

import javax.validation.constraints.NotBlank;

import com.acgist.core.gateway.Gateway;

/**
 * <p>网关 - 请求</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class GatewayRequest extends Gateway {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>请求时间</p>
	 */
	@NotBlank(message = "请求时间不能为空")
	protected String requestTime;

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

}
