package com.acgist.data.service.pojo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.acgist.data.pojo.entity.BaseEntity;

/**
 * <p>entity - 网关</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Entity
@Table(name = "ts_gateway", indexes = {
	@Index(name = "index_gateway_query_id", columnList = "queryId", unique = true)
})
public class GatewayEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>状态</p>
	 */
	public enum Status {
		
		/**
		 * <p>接收</p>
		 * <p>收到网关请求</p>
		 */
		RECEIVE,
		/**
		 * <p>响应</p>
		 * <p>同步发送响应（需要异步通知）</p>
		 */
		ANSWER,
		/**
		 * <p>完成</p>
		 * <p>不用异步通知：同步响应完成</p>
		 * <p>需要异步通知：异步通知完成</p>
		 */
		FINISH,
		/**
		 * <p>失败</p>
		 * <p>异步通知失败</p>
		 */
		FAIL;
		
	}
	
	/**
	 * <p>请求编号</p>
	 */
	@Size(max = 32, message = "请求编号长度不能超过32")
	@NotBlank(message = "请求编号不能为空")
	private String queryId;
	/**
	 * <p>网关状态</p>
	 */
	@NotNull(message = "网关状态不能为空")
	private Status status;
	/**
	 * <p>用户名称</p>
	 */
	@Size(min = 8, max = 20, message = "用户名称长度不能小于8或者超过20")
	@NotBlank(message = "用户名称不能为空")
	private String username;
	/**
	 * <p>请求报文</p>
	 */
	private String request;
	/**
	 * <p>响应报文</p>
	 */
	private String response;
	/**
	 * <p>响应编码</p>
	 */
	@Size(max = 4, message = "响应编码长度不能超过4")
	private String code;
	/**
	 * <p>响应信息</p>
	 */
	@Size(max = 256, message = "响应信息长度不能超过256")
	private String message;

	@Column(length = 32, nullable = false)
	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

	@Column(nullable = false)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Column(length = 20, nullable = false)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Lob
	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	@Lob
	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	@Column(length = 4)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(length = 256)
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
