package com.acgist.data.repository;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import com.acgist.data.pojo.entity.BaseEntity;
import com.acgist.data.pojo.select.Filter;
import com.acgist.data.pojo.select.Order;

/**
 * <p>repository - 添加方法：{@code findOne}、{@code findList}、{@code update}</p>
 * <pre>
 * // 指定实现
 * @EnableJpaRepositories(basePackages = "com.acgist.core.**.repository", repositoryBaseClass = BaseExtendRepositoryImpl.class)
 * </pre>
 */
@NoRepositoryBean
@Transactional(readOnly = true)
public interface BaseExtendRepository<T extends BaseEntity> extends BaseRepository<T> {

	/**
	 * <p>更新忽略属性</p>
	 */
	static final String[] UPDATE_IGNORE_PROPERTIES = new String[] {
		BaseEntity.PROPERTY_CLASS,
		BaseEntity.PROPERTY_ID,
		BaseEntity.PROPERTY_CREATE_DATE,
		BaseEntity.PROPERTY_MODIFY_DATE
	};
	
	/**
	 * <p>第一条数据</p>
	 */
	int FIRST = 0;

	/**
	 * <p>查询一条数据</p>
	 */
	int FIND_ONE = 1;

	/**
	 * <p>根据ID查询数据</p>
	 * 
	 * @param id ID
	 * 
	 * @return 数据
	 */
	default T findOne(String id) {
		return findOne(Filter.eq(BaseEntity.PROPERTY_ID, id));
	}
	
	/**
	 * <p>根据条件查询数据</p>
	 * 
	 * @param filters 查询条件
	 * 
	 * @return 数据
	 */
	default T findOne(Filter ... filters) {
		return findOne((List<Order>) null, filters);
	}

	/**
	 * <p>根据条件查询数据</p>
	 * 
	 * @param order 排序条件
	 * @param filters 查询条件
	 * 
	 * @return 数据
	 */
	default T findOne(Order order, Filter ... filters) {
		return findOne(Order.orders(order), filters);
	}

	/**
	 * <p>根据条件查询数据</p>
	 * 
	 * @param orders 排序条件
	 * @param filters 查询条件
	 * 
	 * @return 数据
	 */
	default T findOne(List<Order> orders, Filter ... filters) {
		final List<T> list = findList(FIRST, FIND_ONE, orders, filters);
		if(list == null || list.isEmpty()) {
			return null;
		}
		return list.get(FIRST);
	}

	/**
	 * <p>根据条件查询数据集合</p>
	 * 
	 * @param size 查询数量
	 * 
	 * @return 数据集合
	 */
	default List<T> findList(int size) {
		return findList(FIRST, size, (List<Order>) null);
	}

	/**
	 * <p>根据条件查询数据集合</p>
	 * 
	 * @param size 查询数量
	 * @param filters 查询条件
	 * 
	 * @return 数据集合
	 */
	default List<T> findList(int size, Filter ... filters) {
		return findList(FIRST, size, (List<Order>) null, filters);
	}

	/**
	 * <p>根据条件查询数据集合</p>
	 * 
	 * @param size 查询数量
	 * @param orders 排序条件
	 * 
	 * @return 数据集合
	 */
	default List<T> findList(int size, Order ... orders) {
		return findList(FIRST, size, Order.orders(orders));
	}

	/**
	 * <p>根据条件查询数据集合</p>
	 * 
	 * @param size 查询数量
	 * @param order 排序条件
	 * @param filters 查询条件
	 * 
	 * @return 数据集合
	 */
	default List<T> findList(int size, Order order, Filter ... filters) {
		return findList(FIRST, size, Order.orders(order), filters);
	}

	/**
	 * <p>根据条件查询数据集合</p>
	 * 
	 * @param size 查询数量
	 * @param orders 排序条件
	 * @param filters 查询条件
	 * 
	 * @return 数据集合
	 */
	default List<T> findList(int size, List<Order> orders, Filter ... filters) {
		return findList(FIRST, size, orders, filters);
	}

	/**
	 * <p>根据条件查询数据集合</p>
	 * 
	 * @param first 开始索引
	 * @param size 查询数量
	 * 
	 * @return 数据集合
	 */
	default List<T> findList(int first, int size) {
		return findList(first, size, (List<Order>) null);
	}

	/**
	 * <p>根据条件查询数据集合</p>
	 * 
	 * @param first 开始索引
	 * @param size 查询数量
	 * @param filters 查询条件
	 * 
	 * @return 数据集合
	 */
	default List<T> findList(int first, int size, Filter ... filters) {
		return findList(first, size, (List<Order>) null, filters);
	}

	/**
	 * <p>根据条件查询数据集合</p>
	 * 
	 * @param first 开始索引
	 * @param size 查询数量
	 * @param orders 排序条件
	 * 
	 * @return 数据集合
	 */
	default List<T> findList(int first, int size, Order ... orders) {
		return findList(first, size, Order.orders(orders));
	}

	/**
	 * <p>根据条件查询数据集合</p>
	 * 
	 * @param first 开始索引
	 * @param size 查询数量
	 * @param order 排序条件
	 * @param filters 查询条件
	 * 
	 * @return 数据集合
	 */
	default List<T> findList(int first, int size, Order order, Filter ... filters) {
		return findList(first, size, Order.orders(order), filters);
	}

	/**
	 * <p>根据条件查询数据集合</p>
	 * 
	 * @param first 开始索引
	 * @param size 查询数量
	 * @param orders 排序条件
	 * @param filters 查询条件
	 * 
	 * @return 数据集合
	 */
	List<T> findList(int first, int size, List<Order> orders, Filter ... filters);

	/**
	 * <p>更新数据</p>
	 * 
	 * @param t 实体
	 * @param ignoreProperties 忽略字段
	 * 
	 * @return 实体
	 */
	@Transactional
	default T update(T t, String ... ignoreProperties) {
		final T persistant = findOne(t.getId());
		if (persistant != null) {
			BeanUtils.copyProperties(t, persistant, ArrayUtils.addAll(ignoreProperties, UPDATE_IGNORE_PROPERTIES));
			return save(persistant);
		} else {
			return saveAndFlush(persistant);
		}
	}
	
}
