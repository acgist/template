package com.acgist.core.gateway.gateway.request;

import javax.validation.constraints.NotBlank;

import com.acgist.core.gateway.gateway.AcgistGateway;

/**
 * <p>网关请求</p>
 */
public class GatewayRequest extends AcgistGateway {

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
