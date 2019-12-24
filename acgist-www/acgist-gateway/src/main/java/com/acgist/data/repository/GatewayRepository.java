package com.acgist.data.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.acgist.data.pojo.entity.GatewayEntity;

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
	@Query(value = "UPDATE tb_gateway model SET model.status = :gateway.status, model.`code` = :gateway.code, model.message = :gateway.message, model.response = :gateway.response WHERE model.query_id = :gateway.queryId", nativeQuery = true)
	void updateResponse(GatewayEntity gateway);
	
}
