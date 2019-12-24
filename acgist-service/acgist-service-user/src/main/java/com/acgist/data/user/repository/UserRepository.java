package com.acgist.data.user.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.acgist.data.repository.BaseExtendRepository;
import com.acgist.data.service.pojo.entity.UserEntity;

/**
 * <p>repository - 订单</p>
 */
@Repository
public interface UserRepository extends BaseExtendRepository<UserEntity> {

	/**
	 * <p>根据用户名称查询用户</p>
	 * 
	 * @param name 用户名称
	 * 
	 * @return 用户
	 */
	@Query(value = "SELECT * FROM tb_user model WHERE model.name = :name LIMIT 1", nativeQuery = true)
	UserEntity findByName(String name);
	
}
