package com.acgist.data.pojo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <p>entity - 模板</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Entity
@Table(name = "ts_template", indexes = {
	@Index(name = "index_template_type", columnList = "type", unique = true)
})
public class TemplateEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>模板类型</p>
	 */
	public enum Type {
		
		/** 验证码短信 */
		CODE_SMS,
		/** 验证码邮件 */
		CODE_MAIL,
		/** 注册邮件 */
		REGISTER_SMS,
		/** 注册邮件 */
		REGISTER_MAIL,
		/** 找回密码邮件 */
		PASSWORD_SMS,
		/** 找回密码邮件 */
		PASSWORD_MAIL;
		
	}
	
	/**
	 * <p>模板类型</p>
	 */
	@NotNull(message = "模板类型不能为空")
	private Type type;
	/**
	 * <p>模板名称</p>
	 */
	@Size(max = 20, message = "模板名称长度不能超过20")
	@NotBlank(message = "模板名称不能为空")
	private String name;
	/**
	 * <p>模板内容</p>
	 */
	@NotBlank(message = "模板内容不能为空")
	private String content;

	@Column(nullable = false)
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Column(length = 20, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Lob
	@Column(nullable = false)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
