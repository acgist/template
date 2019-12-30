package com.acgist.data.gateway.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.acgist.core.gateway.config.AcgistWwwGatewayCache;
import com.acgist.data.pojo.entity.TemplateEntity;
import com.acgist.data.repository.BaseExtendRepository;

/**
 * <p>repository - 模板</p>
 */
@Repository
public interface TemplateRepository extends BaseExtendRepository<TemplateEntity> {

	/**
	 * <p>查询模板</p>
	 * 
	 * @param type 模板类型
	 * 
	 * @return 模板（缓存）
	 */
	@Cacheable(AcgistWwwGatewayCache.TEMPLATE)
	@Query(value = "SELECT * FROM tb_template model WHERE model.type = :type limit 1", nativeQuery = true)
	TemplateEntity findByType(TemplateEntity.Type type);
	
}
