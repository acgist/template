package com.acgist.data.pojo.entity;

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
 * <p>entity - 产品</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Entity
@Table(name = "tb_product", indexes = {
	@Index(name = "index_product_code", columnList = "code")
})
public class ProductEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>产品编号</p>
	 */
	@Size(max = 12, message = "产品编号长度不能超过12")
	@NotBlank(message = "产品编号不能为空")
	private String code;
	/**
	 * <p>产品名称</p>
	 */
	@Size(max = 128, message = "产品名称长度不能超过128")
	@NotBlank(message = "产品名称不能为空")
	private String name;
	/**
	 * <p>产品金额</p>
	 */
	@NotNull
	private Integer amount;
	/**
	 * <p>产品图片</p>
	 */
	@Size(max = 512, message = "产品图片长度不能超过512")
	private String image;
	/**
	 * <p>产品描述</p>
	 */
	private String content;

	@Column(length = 12, nullable = false)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(length = 128, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = false)
	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	@Column(length = 512, nullable = false)
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	@Lob
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
