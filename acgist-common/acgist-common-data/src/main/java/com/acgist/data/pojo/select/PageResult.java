package com.acgist.data.pojo.select;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>select - 分页结果</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public final class PageResult<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * <p>总记录数</p>
	 */
	private final long total;

	/**
	 * <p>分页信息</p>
	 */
	private final PageQuery pageQuery;

	/**
	 * <p>分页内容</p>
	 */
	private final List<T> result = new ArrayList<T>();

	public PageResult() {
		this.total = 0;
		this.pageQuery = new PageQuery();
	}

	public PageResult(long total, PageQuery pageQuery, List<T> result) {
		this.total = total;
		this.pageQuery = pageQuery;
		this.result.clear();
		this.result.addAll(result);
	}

	public long getTotal() {
		return this.total;
	}

	public int getPageSize() {
		return this.pageQuery.getPageSize();
	}

	public int getTotalPage() {
		return (int) (getTotal() / getPageSize());
	}
	
	public List<T> getResult() {
		return this.result;
	}

}
