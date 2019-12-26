package com.acgist.data.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.acgist.data.service.pojo.entity.GatewayEntity;

/**
 * <p>repository - 网关</p>
 */
@Repository
public interface GatewayRepository extends BaseExtendRepository<GatewayEntity> {

	/**
	 * <p>更新网关记录</p>
	 * 
	 * @param gateway 网关记录
	 */
	@Modifying
	@Transactional(readOnly = false)
	@Query(value = "UPDATE GatewayEntity model SET model.status = :#{#gateway.status}, model.code = :#{#gateway.code}, model.message = :#{#gateway.message}, model.response = :#{#gateway.response} WHERE model.queryId = :#{#gateway.queryId}")
	void updateResponse(GatewayEntity gateway);
	
}
