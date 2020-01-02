package com.acgist.core.gateway.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acgist.core.gateway.pojo.vo.PermissionVo;
import com.acgist.core.service.PermissionService;
import com.acgist.data.pojo.entity.RoleEntity;
import com.acgist.data.repository.RoleRepository;

/**
 * <p>service - 角色</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PermissionService permissionService;
	
	/**
	 * <p>所有角色</p>
	 * 
	 * @return 所有角色
	 */
	public List<RoleEntity> all() {
		return this.roleRepository.findAll();
	}
	
	/**
	 * <p>获取权限树</p>
	 * 
	 * @return 权限树
	 */
	public List<PermissionVo> routes() {
		return this.permissionService.allPermission().stream()
			.map(permission -> {
				final PermissionVo vo = new PermissionVo();
				vo.setName(permission.getName());
				vo.setPath(permission.getPath());
				vo.setToken(permission.getToken());
				vo.setMeta(Map.of("title", permission.getName()));
				return vo;
			})
			.collect(Collectors.toList());
	}
	
}
