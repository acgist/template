package com.acgist.data.user.pojo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.acgist.data.pojo.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <p>entity - 用户</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Entity
@Table(name = "tb_user", indexes = {
	@Index(name = "index_user_name", columnList = "name", unique = true),
	@Index(name = "index_user_mail", columnList = "mail", unique = true),
	@Index(name = "index_user_mobile", columnList = "mobile", unique = true)
})
public class UserEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>用户名称：{@value}</p>
	 * 
	 * @see {@link #name}
	 */
	public static final String PROPERTY_NAME = "name";
	/**
	 * <p>用户密码：{@value}</p>
	 * 
	 * @see {@link #password}
	 */
	public static final String PROPERTY_PASSWORD = "password";
	
	/**
	 * <p>用户名称</p>
	 */
	@Size(min = 8, max = 20, message = "用户名称长度不能小于8或者超过20")
	@NotBlank(message = "用户名称不能为空")
	private String name;
	/**
	 * <p>用户邮箱</p>
	 */
	@Size(max = 40, message = "用户邮箱长度不能超过40")
	@NotBlank(message = "用户邮箱不能为空")
	private String mail;
	/**
	 * <p>用户昵称</p>
	 */
	@Size(max = 20, message = "用户昵称长度不能超过20")
	private String nick;
	/**
	 * <p>用户手机</p>
	 */
	@Size(max = 20, message = "用户手机长度不能超过20")
	private String mobile;
	/**
	 * <p>用户密码</p>
	 */
	@Size(min = 8, max = 20, message = "用户密码长度不能小于8或者超过20")
	@NotBlank(message = "用户密码不能为空")
	@JsonIgnore
	private String password;

	@Column(length = 20, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(length = 40, nullable = false)
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	@Column(length = 20)
	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	@Column(length = 20)
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(length = 20, nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
