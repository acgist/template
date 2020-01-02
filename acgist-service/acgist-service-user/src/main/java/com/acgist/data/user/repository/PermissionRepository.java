package com.acgist.data.user.repository;

import org.springframework.stereotype.Repository;

import com.acgist.data.pojo.entity.RoleEntity;
import com.acgist.data.repository.BaseExtendRepository;

/**
 * <p>repository - 角色</p>
 */
@Repository
public interface PermissionRepository extends BaseExtendRepository<RoleEntity> {
	
}
