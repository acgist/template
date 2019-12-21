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
 * 抽象请求：@JsonIgnoreProperties忽略传入的不需要的信息
 */
@JsonInclude(Include.NON_NULL)
//@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonIgnoreProperties(ignoreUnknown = true, value = {"sign"})
public abstract class API implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String DEFAULT_CHARSET = "UTF-8"; // 系统默认编码
	public static final String PROPERTY_USERNAME = "username"; // 用户账号
	public static final String PROPERTY_SIGN = "sign"; // 签名

	@NotBlank(message = "用户帐号不能为空")
	protected String username; // 用户账号
	protected String queryId; // 请求ID
	protected String reserved; // 原样返回数据
	@NotBlank(message = "签名不能为空")
	protected String sign; // 签名
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
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
