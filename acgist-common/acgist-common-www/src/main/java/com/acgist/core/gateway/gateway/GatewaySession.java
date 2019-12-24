package com.acgist.core.gateway.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.exception.ErrorCodeException;
import com.acgist.core.gateway.gateway.request.GatewayRequest;
import com.acgist.core.gateway.gateway.response.GatewayResponse;
import com.acgist.data.service.pojo.message.AuthoMessage;

/**
 * <p>网关 - 请求</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Component
@Scope("session")
public final class GatewaySession {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GatewaySession.class);

	public static final GatewaySession getInstance(ApplicationContext context) {
		return context.getBean(GatewaySession.class);
	}
	
	/**
	 * <p>请求编号</p>
	 */
	private String queryId;
	/**
	 * <p>网关类型</p>
	 */
	private GatewayType gatewayType;
	/**
	 * <p>请求</p>
	 */
	private GatewayRequest request;
	/**
	 * <p>响应</p>
	 */
	private GatewayResponse response;
	/**
	 * <p>授权</p>
	 */
	private AuthoMessage authoMessage;
	
	/**
	 * <p>生成响应</p>
	 * 
	 * @param request 请求
	 */
	public void buildResponse(GatewayRequest request) {
		this.request = request;
		this.response = this.buildResponse();
		this.response.setQueryId(this.queryId);
		this.response.valueOfRequest(this.request); // 设置需要原样返回的参数
	}
	
	/**
	 * <p>生成响应</p>
	 * 
	 * @return 响应
	 */
	private GatewayResponse buildResponse() {
		try {
			return (GatewayResponse) this.gatewayType.getResponseClazz().getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			LOGGER.error("生成响应异常", e);
		}
		throw new ErrorCodeException(AcgistCode.CODE_9999);
	}

	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public GatewayType getGatewayType() {
		return gatewayType;
	}

	public void setGatewayType(GatewayType gatewayType) {
		this.gatewayType = gatewayType;
	}

	public GatewayRequest getRequest() {
		return request;
	}

	public void setRequest(GatewayRequest request) {
		this.request = request;
	}

	public GatewayResponse getResponse() {
		return response;
	}

	public void setResponse(GatewayResponse response) {
		this.response = response;
	}

	public AuthoMessage getAuthoMessage() {
		return authoMessage;
	}

	public void setAuthoMessage(AuthoMessage authoMessage) {
		this.authoMessage = authoMessage;
	}
	
}
