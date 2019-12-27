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

	@Cacheable(AcgistWwwGatewayCache.TEMPLATE)
	@Query(value = "SELECT * FROM tb_template model WHERE model.type = :type", nativeQuery = true)
	TemplateEntity findByType(TemplateEntity.Type type);
	
}
