package com.acgist.core.gateway.request;

import javax.validation.constraints.NotBlank;

import com.acgist.core.gateway.API;
import com.acgist.utils.ValidatorUtils;

/**
 * 抽象请求<br>
 */
public class APIRequest extends API {

	private static final long serialVersionUID = 1L;

	@NotBlank(message = "请求时间不能为空")
	protected String requestTime; // 请求时间

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	/**
	 * 数据格式校验
	 */
	public String verify() {
		return ValidatorUtils.verify(this);
	}
	
}
