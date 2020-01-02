package com.acgist.core.pojo.session;

import java.io.Serializable;
import java.security.PrivateKey;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.gateway.request.GatewayRequest;
import com.acgist.core.gateway.response.GatewayResponse;
import com.acgist.core.pojo.message.ResultMessage;
import com.acgist.data.pojo.entity.PermissionEntity;
import com.acgist.data.pojo.message.AuthoMessage;
import com.acgist.utils.BeanUtils;
import com.acgist.utils.DateUtils;
import com.acgist.utils.GatewayUtils;

/**
 * <p>网关组件</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Component
@Scope("request")
public final class GatewaySession implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final GatewaySession getInstance(ApplicationContext context) {
		return context.getBean(GatewaySession.class);
	}

	/**
	 * <p>是否处理完成</p>
	 */
	private boolean done = false;
	/**
	 * <p>是否是网关请求</p>
	 */
	private boolean gateway = false;
	/**
	 * <p>请求编号</p>
	 */
	private String queryId;
	/**
	 * <p>私钥</p>
	 */
	private PrivateKey privateKey;
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
	 * <p>授权</p>
	 */
	private PermissionEntity permission;
	
	/**
	 * <p>判断是否处理完成</p>
	 * 
	 * @return 是否完成
	 */
	public boolean done() {
		return this.done;
	}
	
	/**
	 * <p>判断是否保存</p>
	 * 
	 * @return 是否保存
	 */
	public boolean save() {
		return this.permission.getSave();
	}
	
	/**
	 * <p>判断是否是网关请求</p>
	 * 
	 * @return 是否是网关请求
	 */
	public boolean gateway() {
		return this.gateway;
	}
	
	/**
	 * <p>设置网关请求</p>
	 * 
	 * @param queryId 请求编号
	 * @param privateKey 私钥
	 */
	public void buildGateway(String queryId, PrivateKey privateKey) {
		this.gateway = true;
		this.queryId = queryId;
		this.privateKey = privateKey;
	}
	
	/**
	 * <p>生成响应</p>
	 * 
	 * @param request 请求
	 */
	public void buildRequest(GatewayRequest request) {
		this.request = request;
		this.response = this.buildResponse();
		this.response.setQueryId(this.queryId);
		// 设置原样返回参数
		this.response.valueOfRequest(this.request);
	}
	
	/**
	 * <p>生成响应</p>
	 * 
	 * @return 响应
	 */
	private GatewayResponse buildResponse() {
		return (GatewayResponse) BeanUtils.newInstance(this.permission.getResponseClazz());
	}

	/**
	 * <p>设置响应</p>
	 * 
	 * @param code 响应编码
	 * 
	 * @return 响应
	 */
	public GatewayResponse buildResponse(AcgistCode code) {
		return this.buildResponse(code.getCode(), code.getMessage());
	}
	
	/**
	 * <p>设置响应</p>
	 * 
	 * @param message 服务消息
	 * 
	 * @return 响应
	 */
	public GatewayResponse buildResponse(ResultMessage message) {
		return this.buildResponse(message.getCode(), message.getMessage());
	}
	
	/**
	 * <p>设置响应</p>
	 * 
	 * @param code 响应编码
	 * @param message 响应信息
	 * 
	 * @return 响应
	 */
	public GatewayResponse buildResponse(String code, String message) {
		this.done = true;
		if(this.response == null) {
			this.response = GatewayResponse.newInstance();
		}
		this.response.setCode(code);
		this.response.setMessage(message);
		this.response.setResponseTime(DateUtils.nowTimestamp());
		if(this.privateKey != null) {
			GatewayUtils.signature(this.privateKey, this.response);
		}
		return this.response;
	}
	
	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
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
	
	public PermissionEntity getPermission() {
		return permission;
	}
	
	public void setPermission(PermissionEntity permission) {
		this.permission = permission;
	}
	
}
