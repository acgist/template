package com.acgist.data.pojo.select;

import java.io.Serializable;
import java.util.List;

/**
 * <p>select - 查询条件</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public final class Filter implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>运算符</p>
	 */
	public enum Operator {

		/** 等于 */
		EQ,

		/** 不等 */
		NE,

		/** 大于 */
		GT,

		/** 小于 */
		LT,

		/** 大于等于 */
		GE,

		/** 小于等于 */
		LE,
		
		/** 包含 */
		IN,

		/** 相似 */
		LIKE,

		/** 等于{@code null} */
		IS_NULL,

		/** 不等{@code null} */
		IS_NOT_NULL;

	}

	/**
	 * <p>属性</p>
	 */
	private final String property;

	/**
	 * <p>运算符</p>
	 */
	private final Operator operator;

	/**
	 * <p>属性值</p>
	 */
	private final Comparable<?> value;

	private Filter(String property, Operator operator, Comparable<?> value) {
		this.property = property;
		this.operator = operator;
		this.value = value;
	}

	/**
	 * <p>创建等于条件查询</p>
	 * 
	 * @param property 属性
	 * @param value 属性值
	 * 
	 * @return 条件
	 */
	public static final Filter eq(String property, Comparable<?> value) {
		return new Filter(property, Operator.EQ, value);
	}

	/**
	 * <p>创建不等条件查询</p>
	 * 
	 * @param property 属性
	 * @param value 属性值
	 * 
	 * @return 条件
	 */
	public static final Filter ne(String property, Comparable<?> value) {
		return new Filter(property, Operator.NE, value);
	}
	
	/**
	 * <p>创建大于条件查询</p>
	 * 
	 * @param property 属性
	 * @param value 属性值
	 * 
	 * @return 条件
	 */
	public static final Filter gt(String property, Comparable<?> value) {
		return new Filter(property, Operator.GT, value);
	}

	/**
	 * <p>创建小于条件查询</p>
	 * 
	 * @param property 属性
	 * @param value 属性值
	 * 
	 * @return 条件
	 */
	public static final Filter lt(String property, Comparable<?> value) {
		return new Filter(property, Operator.LT, value);
	}

	/**
	 * <p>创建大于等于条件查询</p>
	 * 
	 * @param property 属性
	 * @param value 属性值
	 * 
	 * @return 条件
	 */
	public static final Filter ge(String property, Comparable<?> value) {
		return new Filter(property, Operator.GE, value);
	}

	/**
	 * <p>创建小于等于条件查询</p>
	 * 
	 * @param property 属性
	 * @param value 属性值
	 * 
	 * @return 条件
	 */
	public static final Filter le(String property, Comparable<?> value) {
		return new Filter(property, Operator.LE, value);
	}
	
	/**
	 * <p>创建包含条件查询</p>
	 * 
	 * @param property 属性
	 * @param value 属性值
	 * 
	 * @return 条件
	 */
	public static final Filter in(String property, Comparable<?> value) {
		return new Filter(property, Operator.IN, value);
	}

	/**
	 * <p>创建相似条件查询</p>
	 * 
	 * @param property 属性
	 * @param value 属性值
	 * 
	 * @return 条件
	 */
	public static final Filter like(String property, Comparable<?> value) {
		return new Filter(property, Operator.LIKE, value);
	}

	/**
	 * <p>创建等于{@code null}条件查询</p>
	 * 
	 * @param property 属性
	 * @param value 属性值
	 * 
	 * @return 条件
	 */
	public static final Filter isNull(String property) {
		return new Filter(property, Operator.IS_NULL, null);
	}

	/**
	 * <p>创建不等{@code null}条件查询</p>
	 * 
	 * @param property 属性
	 * @param value 属性值
	 * 
	 * @return 条件
	 */
	public static final Filter isNotNull(String property) {
		return new Filter(property, Operator.IS_NOT_NULL, null);
	}

	/**
	 * <p>创建多个条件查询</p>
	 * 
	 * @param filters 条件
	 * 
	 * @return 条件
	 */
	public static final List<Filter> filters(Filter ... filters) {
		if(filters == null) {
			return null;
		}
		return List.of(filters);
	}
	
	/**
	 * <p>获取属性</p>
	 * 
	 * @return 属性
	 */
	public String getProperty() {
		return this.property;
	}

	/**
	 * <p>获取运算符</p>
	 * 
	 * @return 运算符
	 */
	public Operator getOperator() {
		return this.operator;
	}

	/**
	 * <p>获取属性值</p>
	 * 
	 * @return 属性值
	 */
	public Comparable<?> getValue() {
		return this.value;
	}

}