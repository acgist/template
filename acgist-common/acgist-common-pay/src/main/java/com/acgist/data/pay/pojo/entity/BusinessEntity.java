package com.acgist.data.pay.pojo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.acgist.data.pojo.entity.BaseEntity;

/**
 * <p>entity - 支付订单</p>
 */
@Entity
@Table(name = "tb_business", indexes = {
	@Index(name = "index_business_code", columnList = "code", unique = true)
})
public class BusinessEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>支付金额（分）</p>
	 */
	@NotNull(message = "通道编号不能为空")
	private Integer amount;
	/**
	 * <p>用户ID</p>
	 */
	@Size(max = 32, message = "用户ID长度不能超过32")
	@NotBlank(message = "用户ID不能为空")
	private String userId;
	/**
	 * <p>用户名称</p>
	 */
	@Size(max = 20, message = "用户名称长度不能超过20")
	@NotBlank(message = "用户名称不能为空")
	private String userName;
	/**
	 * <p>订单ID</p>
	 */
	@Size(max = 32, message = "通道ID长度不能超过32")
	@NotBlank(message = "通道ID不能为空")
	private String orderId;
	/**
	 * <p>订单编号</p>
	 */
	@Size(max = 20, message = "订单编号长度不能超过20")
	@NotBlank(message = "订单编号不能为空")
	private String orderCode;
	/**
	 * <p>通道ID</p>
	 */
	@Size(max = 32, message = "通道ID长度不能超过32")
	@NotBlank(message = "通道ID不能为空")
	private String channelId;
	/**
	 * <p>通道名称</p>
	 */
	@Size(max = 20, message = "通道名称长度不能超过20")
	@NotBlank(message = "通道名称不能为空")
	private String channelName;

	@Column(nullable = false)
	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	@Column(length = 32, nullable = false)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(length = 20, nullable = false)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(length = 32, nullable = false)
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Column(length = 20, nullable = false)
	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	@Column(length = 32, nullable = false)
	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@Column(length = 20, nullable = false)
	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	
}
