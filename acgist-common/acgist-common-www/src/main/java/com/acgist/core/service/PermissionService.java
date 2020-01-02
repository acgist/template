package com.acgist.core.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.acgist.data.pojo.entity.PermissionEntity;
import com.acgist.data.pojo.message.PermissionMessage;

/**
 * <p>service - 权限</p>
 * 
 * TODO：删除缓存
 * 
 * @author acgist
 * @since 1.0.0
 */
@Service
public class PermissionService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PermissionService.class);

	/**
	 * <p>是否加载权限</p>
	 */
	@Value("${acgist.permission:false}")
	private boolean permission;
	/**
	 * <p>授权信息</p>
	 */
	private PermissionMessage permissionMessage;
	
	@Reference(version = "${acgist.service.version}")
	private IPermissionService permissionService;
	
	@PostConstruct
	public void init() {
		if(this.permission) {
			LOGGER.info("加载权限");
			this.permissionMessage = this.permissionService.allPermission();
		}
	}
	
	/**
	 * <p>所有权限树</p>
	 * 
	 * @return 权限树
	 */
	public List<PermissionEntity> allPermission() {
		return this.permissionMessage.getPermissions();
	}
	
	/**
	 * <p>获取权限</p>
	 * 
	 * @param path 路径
	 * 
	 * @return 权限
	 */
	public PermissionEntity getPermission(String path) {
		if(this.permissionMessage == null) {
			return null;
		}
		return this.permissionMessage.getPermissions().stream()
			.filter(permission -> StringUtils.equals(permission.getPath(), path))
			.findFirst()
			.orElse(null);
	}
	
	/**
	 * <p>判断是否拥有权限</p>
	 * 
	 * @param role 角色
	 * @param permission 权限
	 * 
	 * @return 是否拥有权限
	 */
	public boolean hasPermission(String[] roles, PermissionEntity permission) {
		if(this.permissionMessage == null) {
			return false;
		}
		return this.permissionMessage.getRoles().entrySet().stream()
			.filter(entry -> ArrayUtils.contains(roles, entry.getKey()))
			.flatMap(entry -> entry.getValue().stream())
			.anyMatch(value -> value.equals(permission));
	}
	
}
