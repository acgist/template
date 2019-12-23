package com.acgist.data.order.pojo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.acgist.data.pojo.entity.BaseEntity;

/**
 * <p>entity - 订单</p>
 */
@Entity
@Table(name = "tb_order", indexes = {
	@Index(name = "index_order_user_id", columnList = "user_id"),
	@Index(name = "index_order_code", columnList = "code", unique = true)
})
public class OrderEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * @see {@link #code}
	 */
	public static final String PROPERTY_ORDER_CODE = "code";
	
	/**
	 * <p>用户ID</p>
	 */
	@Size(max = 32, message = "用户ID长度不能超过32")
	@NotBlank(message = "用户ID不能为空")
	private String userId;
	/**
	 * <p>订单编号</p>
	 */
	@Size(max = 22, message = "订单编号长度不能超过22")
	@NotBlank(message = "订单编号不能为空")
	private String code;
	/**
	 * <p>订单金额</p>
	 */
	@NotNull
	private Integer amount;

	@Column(length = 32, nullable = false)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(length = 22, nullable = false)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(nullable = false)
	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

}
