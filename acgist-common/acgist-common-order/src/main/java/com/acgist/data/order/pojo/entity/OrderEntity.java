package com.acgist.data.order.pojo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.acgist.data.pojo.entity.BaseEntity;

/**
 * <p>entity - 订单</p>
 */
@Entity
@Table(name = "tb_order", indexes = {
	@Index(name = "index_order_code", columnList = "code", unique = true)
})
public class OrderEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>订单编号</p>
	 */
	public static final String PROPERTY_ORDER_ID = "code";
	
	/**
	 * <p>订单编号</p>
	 */
	@Size(max = 100, message = "订单编号长度不能超过100")
	@NotBlank(message = "订单编号不能为空")
	private String code;

	@Column(nullable = false, length = 100)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
