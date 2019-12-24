package com.acgist.data.service.pojo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.acgist.data.pojo.entity.BaseEntity;

/**
 * <p>entity - 支付通道</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Entity
@Table(name = "tb_channel")
public class ChannelEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>通道编号</p>
	 */
	@Size(max = 10, message = "通道编号长度不能超过10")
	@NotBlank(message = "通道编号不能为空")
	private String code;
	/**
	 * <p>通道名称</p>
	 */
	@Size(max = 20, message = "通道名称长度不能超过20")
	@NotBlank(message = "通道名称不能为空")
	private String name;
	/**
	 * <p>通道官网</p>
	 */
	@Size(max = 256, message = "通道官网长度不能超过256")
	@NotBlank(message = "通道官网不能为空")
	private String home;
	/**
	 * <p>支付参数（JSON）</p>
	 */
	@Size(max = 1024, message = "支付参数长度不能超过1024")
	@NotBlank(message = "支付参数不能为空")
	private String arguments;

	@Column(length = 10, nullable = false)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(length = 20, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(length = 256, nullable = false)
	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	@Column(length = 1024, nullable = false)
	public String getArguments() {
		return arguments;
	}

	public void setArguments(String arguments) {
		this.arguments = arguments;
	}

}
