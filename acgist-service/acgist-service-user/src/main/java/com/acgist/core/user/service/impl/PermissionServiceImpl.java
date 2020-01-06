package com.acgist.core.user.service.impl;

import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.acgist.core.service.IPermissionService;
import com.acgist.data.pojo.session.PermissionSession;
import com.acgist.data.user.repository.PermissionRepository;
import com.acgist.data.user.repository.RoleRepository;

/**
 * <p>service - 权限</p>
 * 
 * TODO：删除角色缓存
 * 
 * @author acgist
 * @since 1.0.0
 */
@Service(retries = 0, version = "${acgist.service.version}")
public class PermissionServiceImpl implements IPermissionService {

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PermissionRepository permissionRepository;
	
	@Override
	@Transactional
	public PermissionSession allPermission() {
		final PermissionSession session = new PermissionSession();
		final var allRoles = this.roleRepository.findAll();
		final var roles = allRoles.stream()
			.map(role -> Map.entry(role.getToken(), role.getPermissions().stream().collect(Collectors.toList())))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		session.setRoles(roles);
		final var allPermissions = this.permissionRepository.findAll();
		final var permissions = allPermissions.stream()
			.collect(Collectors.toList());
		session.setPermissions(permissions);
		return session;
	}
	
}
