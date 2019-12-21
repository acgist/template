package com.acgist.data.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.acgist.data.pojo.entity.BaseEntity;
import com.acgist.data.pojo.select.Filter;
import com.acgist.data.pojo.select.Order;

/**
 * <p>repository - {@code BaseExtendRepository}实现</p>
 */
public class BaseExtendRepositoryImpl<T extends BaseEntity> extends SimpleJpaRepository<T, String> implements BaseExtendRepository<T> {

	/**
	 * <p>实体管理</p>
	 */
	protected EntityManager entityManager;
	/**
	 * <p>类型</p>
	 */
	protected JpaEntityInformation<T, ?> clazz;
	
	public BaseExtendRepositoryImpl(Class<T> clazz, EntityManager entityManager) {
		this(JpaEntityInformationSupport.getEntityInformation(clazz, entityManager), entityManager);
	}

	public BaseExtendRepositoryImpl(JpaEntityInformation<T, ?> clazz, EntityManager entityManager) {
		super(clazz, entityManager);
		this.clazz = clazz;
		this.entityManager = entityManager;
	}

	@Override
	public List<T> findList(int first, int size, List<Order> orders, Filter ... filters) {
		final Class<T> javaType = this.clazz.getJavaType();
		final CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
		final CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(javaType);
		final Root<T> root = criteriaQuery.from(javaType);
		this.buildCriteriaQuery(orders, Filter.filters(filters), root, criteriaQuery, criteriaBuilder);
		final TypedQuery<T> typedQuery = this.entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
		typedQuery.setFirstResult(first);
		typedQuery.setMaxResults(size);
		return typedQuery.getResultList();
	}
	
}
