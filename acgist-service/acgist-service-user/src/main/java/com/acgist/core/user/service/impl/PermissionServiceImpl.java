package com.acgist.core.user.service.impl;

import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.acgist.core.service.IPermissionService;
import com.acgist.data.service.pojo.message.PermissionMessage;
import com.acgist.data.user.repository.RoleRepository;

/**
 * <p>service - 权限</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Service(retries = 0, version = "${acgist.service.version}")
public class PermissionServiceImpl implements IPermissionService {

	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	@Transactional
	public PermissionMessage allPermission() {
		final var allRoles = this.roleRepository.findAll();
		final PermissionMessage message = new PermissionMessage();
		// 手动创建瞬时对象
		final var roles = allRoles.stream()
			.map(role -> Map.entry(role.getId(), role.getPermissions().stream().collect(Collectors.toList())))
			.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		final var permissions = allRoles.stream()
			.flatMap(role -> role.getPermissions().stream())
			.collect(Collectors.toList());
		message.setRoles(roles);
		message.setPermissions(permissions);
		return message;
	}

}
