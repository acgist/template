package com.acgist.data.repository;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import com.acgist.data.pojo.entity.BaseEntity;
import com.acgist.data.pojo.select.Filter;
import com.acgist.data.pojo.select.Order;
import com.acgist.data.pojo.select.PageQuery;
import com.acgist.data.pojo.select.PageResult;

/**
 * <p>repository - 添加方法：{@codefindAll}、{@code findPage}</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@NoRepositoryBean
@Transactional(readOnly = true)
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, String>, JpaSpecificationExecutor<T> {

	/**
	 * <p>相似查询拼接</p>
	 */
	String LIKE = "%";
	
	/**
	 * <p>相似查询拼接</p>
	 */
	Function<Object, String> LIKE_QUERY = (value) -> {
		if(value == null) {
			return null;
		}
		return LIKE + value + LIKE;
	};
	
	/**
	 * <p>查询所有数据</p>
	 * 
	 * @param filters 查询条件
	 * 
	 * @return 所有数据
	 */
	default List<T> findAll(Filter ... filters) {
		return findAll((List<Order>) null, filters);
	}
	
	/**
	 * <p>查询所有数据</p>
	 * 
	 * @param order 排序条件
	 * @param filters 查询条件
	 * 
	 * @return 所有数据
	 */
	default List<T> findAll(Order order, Filter ... filters) {
		return findAll(Order.orders(order), filters);
	}
	
	/**
	 * <p>查询所有数据</p>
	 * 
	 * @param orders 排序条件
	 * @param filters 查询条件
	 * 
	 * @return 所有数据
	 */
	default List<T> findAll(List<Order> orders, Filter ... filters) {
		return findAll(new Specification<T>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				buildCriteriaQuery(orders, Filter.filters(filters), root, criteriaQuery, criteriaBuilder);
				return null;
			}
		});
	}
	
	/**
	 * <p>分页查询</p>
	 * 
	 * @param pageQuery 分页信息
	 * 
	 * @return 分页结果
	 */
	default PageResult<T> findPage(PageQuery pageQuery) {
		final Page<T> result = findAll(new Specification<T>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
				buildCriteriaQuery(pageQuery.getOrders(), pageQuery.getFilters(), root, criteriaQuery, criteriaBuilder);
				return null;
			}
		}, PageRequest.of(pageQuery.getPage(), pageQuery.getPageSize()));
		return new PageResult<>(result.getTotalElements(), pageQuery, result.getContent());
	}

	/**
	 * <p>创建查询条件</p>
	 * 
	 * @param orders 排序条件
	 * @param filters 查询条件
	 * @param root root
	 * @param criteriaQuery query
	 * @param criteriaBuilder builder
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	default void buildCriteriaQuery(List<Order> orders, List<Filter> filters, Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
		// 查询条件
		if(filters != null && !filters.isEmpty()) {
			final var list = filters.stream()
				// 过滤无效条件
				.filter(filter -> filter != null && filter.getProperty() != null && filter.getOperator() != null)
				// 类型转换
				.map(filter -> {
					switch (filter.getOperator()) {
					case EQ:
						return criteriaBuilder.equal(root.get(filter.getProperty()), filter.getValue());
					case NE:
						return criteriaBuilder.notEqual(root.get(filter.getProperty()), filter.getValue());
					case GT:
						return criteriaBuilder.greaterThan(root.get(filter.getProperty()), (Comparable) filter.getValue());
					case LT:
						return criteriaBuilder.lessThan(root.get(filter.getProperty()), (Comparable) filter.getValue());
					case GE:
						return criteriaBuilder.greaterThanOrEqualTo(root.get(filter.getProperty()), (Comparable) filter.getValue());
					case LE:
						return criteriaBuilder.lessThanOrEqualTo(root.get(filter.getProperty()), (Comparable) filter.getValue());
					case IN:
						return root.get(filter.getProperty()).in(filter.getValue());
					case LIKE:
						return criteriaBuilder.like(root.get(filter.getProperty()), LIKE_QUERY.apply(filter.getValue()));
					case IS_NULL:
						return root.get(filter.getProperty()).isNull();
					case IS_NOT_NULL:
						return root.get(filter.getProperty()).isNotNull();
					default:
						return null;
					}
				})
				.toArray(Predicate[]::new);
//			OR条件
//			criteriaQuery.where(criteriaBuilder.or(list));
//			AND条件
//			criteriaQuery.where(criteriaBuilder.and(list));
			criteriaQuery.where(list);
		}
		// 排序条件
		if(orders != null && !orders.isEmpty()) {
			final var list = orders.stream()
				.filter(order -> {
					return
						order != null &&
						order.getProperty() != null &&
						order.getDirection() != null;
				})
				.map(order -> {
					switch (order.getDirection()) {
					case ASC:
						return criteriaBuilder.asc(root.get(order.getProperty()));
					case DESC:
						return criteriaBuilder.desc(root.get(order.getProperty()));
					default:
						return null;
					}
				})
				.collect(Collectors.toList());
			criteriaQuery.orderBy(list);
		}
	}
	
}
