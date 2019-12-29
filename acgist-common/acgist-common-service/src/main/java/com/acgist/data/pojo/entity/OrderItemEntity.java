package com.acgist.data.pojo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <p>entity - 订单项</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Entity
@Table(name = "tb_order_item", indexes = {
	@Index(name = "index_order_item_order_id", columnList = "orderId"),
	@Index(name = "index_order_item_product_id", columnList = "productId")
})
public class OrderItemEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>订单ID</p>
	 */
	@Size(max = 32, message = "订单ID长度不能超过32")
	@NotBlank(message = "订单ID不能为空")
	private String orderId;
	/**
	 * <p>商品ID</p>
	 */
	@Size(max = 32, message = "商品ID长度不能超过32")
	@NotBlank(message = "商品ID不能为空")
	private String productId;
	/**
	 * <p>商品单价</p>
	 */
	@NotNull
	private Integer amount;
	/**
	 * <p>商品数量</p>
	 */
	@NotNull
	private Integer number;

	@Column(length = 32, nullable = false)
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Column(length = 32, nullable = false)
	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	@Column(nullable = false)
	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	@Column(nullable = false)
	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
	
}
