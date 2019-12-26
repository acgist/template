package com.acgist.data.user.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.acgist.data.repository.BaseExtendRepository;
import com.acgist.data.service.pojo.entity.UserEntity;

/**
 * <p>repository - 订单</p>
 */
@Repository
public interface UserRepository extends BaseExtendRepository<UserEntity> {

	/**
	 * <p>根据用户名称查询用户信息</p>
	 * 
	 * @param name 用户名称
	 * 
	 * @return 用户信息
	 */
	@Query(value = "SELECT * FROM ts_user model WHERE model.name = :name LIMIT 1", nativeQuery = true)
	UserEntity findByName(String name);
	
	/**
	 * <p>更新用户信息</p>
	 * 
	 * @param nick 用户昵称
	 * @param name 用户名称
	 */
	@Modifying
	@Transactional(readOnly = false)
	@Query(value = "UPDATE UserEntity model SET model.nick = :nick WHERE model.name = :name")
	void update(String nick, String name);
	
}
