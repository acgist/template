package com.acgist.core.gateway;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import com.acgist.utils.APIUtils;
import com.acgist.utils.JSONUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * <p>抽象网关消息</p>
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AcgistGateway implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>用户名称</p>
	 */
	public static final String PROPERTY_USERNAME = "username";
	/**
	 * <p>签名</p>
	 */
	public static final String PROPERTY_SIGN = "sign";

	/**
	 * <p>请求编号</p>
	 */
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
	 * 获取map数据
	 */
	public Map<String, String> data() {
		return APIUtils.beanToMap(this);
	}

	/**
	 * 返回JSON字符串
	 */
	@Override
	public String toString() {
		return JSONUtils.toJSON(this);
	}
	
}
