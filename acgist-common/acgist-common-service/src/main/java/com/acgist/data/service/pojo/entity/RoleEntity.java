package com.acgist.data.service.pojo.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.acgist.data.pojo.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * <p>entity - 权限角色</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Entity
@Table(name = "ts_role")
public final class RoleEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>角色类型</p>
	 */
	@NotNull(message = "角色类型不能为空")
	private UserEntity.Type type;
	/**
	 * <p>角色名称</p>
	 */
	@Size(max = 20, message = "角色名称长度不能超过20")
	@NotBlank(message = "角色名称不能为空")
	private String name;
	/**
	 * <p>角色描述</p>
	 */
	@Size(max = 256, message = "角色描述长度不能超过256")
	private String memo;
	/**
	 * <p>角色权限</p>
	 */
	@JsonIgnore
	private transient List<PermissionEntity> permissions;

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

	@Column(length = 256)
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinTable(
		name = "ts_role_permission",
		joinColumns = @JoinColumn(
			name = "role_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "key_role_permission_role_id")
		),
		inverseJoinColumns = @JoinColumn(
			name = "permission_id",
			referencedColumnName = "id",
			foreignKey = @ForeignKey(name = "key_role_permission_permission_id")
		)
	)
	public List<PermissionEntity> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<PermissionEntity> permissions) {
		this.permissions = permissions;
	}

	public boolean hasPermission(PermissionEntity permission) {
		if(this.permissions == null) {
			return false;
		}
		return this.permissions.contains(permission);
	}
	
}
