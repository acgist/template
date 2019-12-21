package com.acgist.data.pojo.entity;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * <p>Listener - 数据库预处理</p>
 */
public class EntityListener {

	/**
	 * <p>保存预处理</p>
	 * 
	 * @param entity 实体
	 */
	@PrePersist
	public void prePersist(BaseEntity entity) {
		entity.setCreateDate(new Date());
		entity.setModifyDate(new Date());
	}

	/**
	 * <p>更新预处理</p>
	 * 
	 * @param entity 实体
	 */
	@PreUpdate
	public void preUpdate(BaseEntity entity) {
		entity.setModifyDate(new Date());
	}

}
