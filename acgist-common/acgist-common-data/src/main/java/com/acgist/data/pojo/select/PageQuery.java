package com.acgist.data.pojo.select;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <p>select - 分页查询</p>
 */
public class PageQuery implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * <p>默认页码：{@value}</p>
	 */
	private static final int DEFAULT_PAGE = 1;

	/**
	 * <p>默认每页记录：{@value}</p>
	 */
	private static final int DEFAULT_PAGE_SIZE = 20;

	/**
	 * <p>最大每页记录：{@value}</p>
	 */
	private static final int MAX_PAGE_SIZE = 1000;

	/**
	 * <p>页码</p>
	 */
	private int page = DEFAULT_PAGE;

	/**
	 * <p>每页记录</p>
	 */
	private int pageSize = DEFAULT_PAGE_SIZE;
	
	/**
	 * <p>排序条件</p>
	 */
	private final List<Order> orders = new ArrayList<Order>();

	/**
	 * <p>查询条件</p>
	 */
	private final List<Filter> filters = new ArrayList<Filter>();

	public PageQuery() {
	}

	public PageQuery(int page, int pageSize) {
		setPage(page);
		setPageSize(pageSize);
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		if (page < 1) {
			page = DEFAULT_PAGE;
		}
		this.page = page - 1;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		if (pageSize < 1 || pageSize > MAX_PAGE_SIZE) {
			pageSize = DEFAULT_PAGE_SIZE;
		}
		this.pageSize = pageSize;
	}
	
	public List<Order> getOrders() {
		return orders;
	}

	public List<Filter> getFilters() {
		return filters;
	}

	public void addFilters(Filter ... filters) {
		if (filters == null || filters.length == 0) {
			return;
		}
		this.filters.addAll(Arrays.asList(filters));
	}

	public void addOrders(Order ... orders) {
		if (orders == null || orders.length == 0) {
			return;
		}
		this.orders.addAll(Arrays.asList(orders));
	}

}
