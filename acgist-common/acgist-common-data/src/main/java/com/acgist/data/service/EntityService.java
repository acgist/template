package com.acgist.data.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.transaction.annotation.Transactional;

import com.acgist.data.pojo.entity.BaseEntity;
import com.acgist.data.pojo.select.PageQuery;
import com.acgist.data.pojo.select.PageResult;
import com.acgist.data.repository.BaseExtendRepository;

/**
 * <p>service - 实体</p>
 */
public abstract class EntityService<T extends BaseEntity> {

	/**
	 * <p>DAO</p>
	 */
	protected BaseExtendRepository<T> repository;
	
	public EntityService(BaseExtendRepository<T> repository) {
		this.repository = repository;
	}
	
	@Transactional(readOnly = true)
	public T find(String id) {
		return this.repository.findOne(id);
	}
	
	@Transactional
	public T submit(T t) {
		return this.repository.save(t);
	}
	
	@Transactional
	public T update(T t) {
		return this.repository.save(t);
	}
	
	@Transactional
	public boolean delete(String id) {
		this.repository.deleteById(id);
		return true;
	}
	
	@Transactional
	public void delete(String ... ids) {
		Stream.of(ids).forEach(id -> delete(id));
	}
	
	@Transactional(readOnly = true)
	public List<T> findAll() {
		return this.repository.findAll();
	}
	
	@Transactional(readOnly = true)
	public PageResult<T> findPage(PageQuery pageQuery) {
		return this.repository.findPage(pageQuery);
	}
	
}
