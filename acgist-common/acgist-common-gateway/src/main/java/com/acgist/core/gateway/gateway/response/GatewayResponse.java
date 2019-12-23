package com.acgist.core.gateway.gateway.response;

import java.util.Map;

import javax.validation.constraints.NotBlank;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.gateway.gateway.AcgistGateway;
import com.acgist.core.gateway.gateway.request.GatewayRequest;
import com.acgist.core.pojo.message.ResultMessage;
import com.acgist.utils.DateUtils;
import com.acgist.utils.GatewayUtils;

/**
 * <p>网关响应</p>
 */
public class GatewayResponse extends AcgistGateway {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>请求时间</p>
	 */
	@NotBlank(message = "请求时间不能为空")
	protected String requestTime;
	/**
	 * <p>响应时间</p>
	 */
	@NotBlank(message = "响应时间不能为空")
	protected String responseTime;
	/**
	 * <p>响应编码</p>
	 */
	@NotBlank(message = "响应编码不能为空")
	protected String code;
	/**
	 * <p>响应内容</p>
	 */
	@NotBlank(message = "响应内容不能为空")
	protected String message;

	public static final GatewayResponse newInstance() {
		return new GatewayResponse();
	}
	
	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public String getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * <p>判断是否成功</p>
	 * 
	 * @return 是否成功
	 */
	public boolean success() {
		return AcgistCode.success(this.code);
	}
	
	/**
	 * <p>判断是否失败</p>
	 * 
	 * @return 是否失败
	 */
	public boolean fail() {
		return !success();
	}
	
	/**
	 * <p>将请求数据设置到响应中</p>
	 * 
	 * @param request 请求
	 * 
	 * @return 响应
	 */
	public GatewayResponse valueOfRequest(GatewayRequest request) {
		if(request != null) {
			valueOfMap(request.data());
		}
		return this;
	}
	
	/**
	 * <p>设置响应参数</p>
	 * 
	 * @param data 参数
	 * 
	 * @return 响应
	 */
	public GatewayResponse valueOfMap(final Map<String, String> data) {
		if(data != null) {
			data.remove(AcgistGateway.PROPERTY_SIGN); // 移除签名
			GatewayUtils.pack(this, data);
		}
		return this;
	}
	
	/**
	 * <p>成功响应</p>
	 * 
	 * @return 响应
	 */
	public GatewayResponse buildSuccess() {
		return buildMessage(AcgistCode.CODE_0000);
	}
	
	/**
	 * <p>失败响应</p>
	 * 
	 * @return 响应
	 */
	public GatewayResponse buildFail() {
		return buildMessage(AcgistCode.CODE_9999);
	}
	
	/**
	 * <p>失败响应</p>
	 * 
	 * @param code 失败编码
	 * 
	 * @return 响应
	 */
	public GatewayResponse buildMessage(AcgistCode code) {
		return buildMessage(code.getCode(), code.getMessage());
	}
	
	/**
	 * <p>失败响应</p>
	 * 
	 * @param message 服务消息
	 * 
	 * @return 响应
	 */
	public GatewayResponse buildMessage(ResultMessage message) {
		return buildMessage(message.getCode(), message.getMessage());
	}
	
	/**
	 * <p>失败响应</p>
	 * 
	 * @param code 失败编码
	 * @param message 失败消息
	 * 
	 * @return 响应
	 */
	public GatewayResponse buildMessage(AcgistCode code, String message) {
		message = AcgistCode.message(code, message);
		return buildMessage(code.getCode(), message);
	}
	
	/**
	 * <p>失败响应</p>
	 * 
	 * @param code 失败编码
	 * @param message 失败消息
	 * 
	 * @return 响应
	 */
	public GatewayResponse buildMessage(String code, String message) {
		this.code = code;
		this.message = message;
		this.responseTime = DateUtils.nowTimestamp();
		return this;
	}

}