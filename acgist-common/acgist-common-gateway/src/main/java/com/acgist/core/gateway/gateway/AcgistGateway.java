package com.acgist.core.gateway.gateway;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import com.acgist.core.pojo.Pojo;
import com.acgist.utils.GatewayUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * <p>网关消息</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AcgistGateway extends Pojo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>签名</p>
	 */
	public static final String PROPERTY_SIGN = "sign";

	/**
	 * <p>请求编号</p>
	 */
	@NotBlank(message = "请求编号不能为空")
	protected String queryId;
	/**
	 * <p>用户名称</p>
	 */
	@NotBlank(message = "用户名称不能为空")
	protected String username;
	/**
	 * <p>请求保留数据</p>
	 * <p>原样返回</p>
	 */
	protected String reserved;
	/**
	 * <p>签名</p>
	 * <p>MD5</p>
	 */
	@NotBlank(message = "签名不能为空")
	protected String sign;
	
	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getReserved() {
		return reserved;
	}

	public void setReserved(String reserved) {
		this.reserved = reserved;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	/**
	 * <p>获取请求参数</p>
	 * 
	 * @return 请求参数
	 */
	public Map<String, String> data() {
		return GatewayUtils.unpack(this);
	}
	
}
