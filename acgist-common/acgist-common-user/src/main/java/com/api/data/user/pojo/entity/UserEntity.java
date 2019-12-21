package com.api.data.user.pojo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.api.data.pojo.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * entity - 用户<br>
 * 接口使用证书鉴权、页面使用密码登陆
 */
@Entity
@Table(name = "tb_user", indexes = {
	@Index(name = "index_user_name", columnList = "name", unique = true)
})
public class UserEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	public static final String PROPERTY_NAME = "name"; // 用户名
	public static final String PROPERTY_PASSWORD = "password"; // 密码
	public static final String PROPERTY_PUBLIC_KEY = "publicKey"; // 公钥
	public static final String PROPERTY_PRIVATE_KEY = "privateKey"; // 私钥
	
	/**
	 * 名称
	 */
	@Size(max = 20, message = "用户名称长度不能超过20")
	@NotBlank(message = "用户名称不能为空")
	private String name;
	/**
	 * 密码
	 */
	@Size(max = 50, message = "用户密码长度不能超过50")
	@NotBlank(message = "用户密码不能为空")
	private String password;
	/**
	 * 公钥
	 */
	@Size(max = 2048, message = "用户公钥长度不能超过2048")
	@JsonIgnore
	private String publicKey;
	/**
	 * 私钥
	 */
	@Size(max = 2048, message = "用户私钥长度不能超过2048")
	@JsonIgnore
	private String privateKey;

	@Column(length = 20, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(length = 50, nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(length = 2048)
	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	@Column(length = 2048)
	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

}
