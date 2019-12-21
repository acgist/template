package com.api.data.user.pojo.entity;

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
 */
@Entity
@Table(name = "tb_user", indexes = {
	@Index(name = "index_user_name", columnList = "name", unique = true)
})
public class UserEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>名称：{@value}</p>
	 */
	public static final String PROPERTY_NAME = "name";
	/**
	 * <p>密码：{@value}</p>
	 * <p>前台登陆</p>
	 */
	public static final String PROPERTY_PASSWORD = "password";
	/**
	 * <p>公钥：{@value}</p>
	 * <p>前台网关</p>
	 */
	public static final String PROPERTY_PUBLIC_KEY = "publicKey";
	/**
	 * <p>私钥：{@value}</p>
	 * <p>前台网关</p>
	 */
	public static final String PROPERTY_PRIVATE_KEY = "privateKey";
	
	/**
	 * <p>名称</p>
	 */
	@Size(max = 20, message = "用户名称长度不能超过20")
	@NotBlank(message = "用户名称不能为空")
	private String name;
	/**
	 * <p>密码</p>
	 */
	@Size(max = 40, message = "用户密码长度不能超过40")
	@NotBlank(message = "用户密码不能为空")
	@JsonIgnore
	private String password;
	/**
	 * <p>公钥</p>
	 */
	@Size(max = 2048, message = "用户公钥长度不能超过2048")
	@JsonIgnore
	private String publicKey;
	/**
	 * <p>私钥</p>
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

	@Column(length = 40, nullable = false)
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
