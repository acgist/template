package com.acgist.data.service.pojo.message;

import java.util.List;
import java.util.Map;

import com.acgist.core.pojo.Pojo;
import com.acgist.data.service.pojo.entity.PermissionEntity;

/**
 * <p>message - 权限信息</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class PermissionMessage extends Pojo {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>所有角色</p>
	 */
	private Map<String, List<PermissionEntity>> roles;
	/**
	 * <p>所有权限</p>
	 */
	private List<PermissionEntity> permissions;

	public Map<String, List<PermissionEntity>> getRoles() {
		return roles;
	}
	
	public void setRoles(Map<String, List<PermissionEntity>> roles) {
		this.roles = roles;
	}
	
	public List<PermissionEntity> getPermissions() {
		return permissions;
	}
	
	public void setPermissions(List<PermissionEntity> permissions) {
		this.permissions = permissions;
	}
	
}
