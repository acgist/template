package com.acgist.data.pojo.select;

import java.io.Serializable;
import java.util.List;

/**
 * <p>select - 排序条件</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public final class Order implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>排序</p>
	 */
	public enum Direction {
		
		/** 递增 */
		ASC,
		
		/** 递减 */
		DESC;

	}

	/**
	 * <p>默认排序</p>
	 */
	private static final Direction DEFAULT_DIRECTION = Direction.DESC;

	/**
	 * <p>属性</p>
	 */
	private String property;

	/**
	 * <p>排序</p>
	 */
	private Direction direction = DEFAULT_DIRECTION;

	private Order(String property, Direction direction) {
		this.property = property;
		this.direction = direction;
	}

	/**
	 * <p>递增排序</p>
	 * 
	 * @param property 属性
	 * 
	 * @return 排序
	 */
	public static final Order asc(String property) {
		return new Order(property, Direction.ASC);
	}

	/**
	 * <p>递减排序</p>
	 * 
	 * @param property 属性
	 * 
	 * @return 排序
	 */
	public static final Order desc(String property) {
		return new Order(property, Direction.DESC);
	}
	
	/**
	 * <p>多个排序</p>
	 * 
	 * @param orders 排序
	 * 
	 * @return 排序
	 */
	public static final List<Order> orders(Order ... orders) {
		if(orders == null) {
			return null;
		}
		return List.of(orders);
	}
	
	/**
	 * <p>获取属性</p>
	 * 
	 * @return 属性
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * <p>获取排序</p>
	 * 
	 * @return 排序
	 */
	public Direction getDirection() {
		return direction;
	}

}