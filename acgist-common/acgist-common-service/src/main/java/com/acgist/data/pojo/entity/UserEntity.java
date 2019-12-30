package com.acgist.data.pojo.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <p>entity - 用户</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Entity
@Table(name = "ts_user", indexes = {
	@Index(name = "index_user_name", columnList = "name", unique = true),
	@Index(name = "index_user_mail", columnList = "mail", unique = true),
	@Index(name = "index_user_mobile", columnList = "mobile", unique = true)
})
public final class UserEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>角色类型</p>
	 */
	public enum Type {
		
		/**
		 * <p>用户权限</p>
		 */
		USER,
		/**
		 * <p>后台权限</p>
		 */
		ADMIN;
		
	}
	/**
	 * <p>用户类型</p>
	 */
	@NotNull(message = "用户类型不能为空")
	private UserEntity.Type type;
	/**
	 * <p>用户名称</p>
	 */
	@Size(min = 4, max = 20, message = "用户名称长度不能小于4或者超过20")
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
	 * <p>MD5编码保存</p>
	 */
	@Size(min = 8, max = 40, message = "用户密码长度不能小于8或者超过40")
	@NotBlank(message = "用户密码不能为空")
	@JsonIgnore
	private String password;
	/**
	 * <p>角色列表</p>
	 */
	@JsonIgnore
	private transient List<RoleEntity> roles;

	@Column(nullable = false)
	public UserEntity.Type getType() {
		return type;
	}

	public void setType(UserEntity.Type type) {
		this.type = type;
	}
	
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

	@Column(length = 40, nullable = false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinTable(
		name = "ts_user_role",
		joinColumns = @JoinColumn(
			name = "user_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "key_user_role_user_id")
		),
		inverseJoinColumns = @JoinColumn(
			name = "role_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "key_user_role_role_id")
		)
	)
	public List<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleEntity> roles) {
		this.roles = roles;
	}
	
	public boolean hasRole(RoleEntity role) {
		if(this.roles == null) {
			return false;
		}
		return this.roles.contains(role);
	}
	
	public boolean hasPermission(PermissionEntity permission) {
		if(this.roles == null) {
			return false;
		}
		return this.roles
			.stream()
			.anyMatch(role -> role.hasPermission(permission));
	}
	
}
