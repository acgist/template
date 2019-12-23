package com.acgist.core.www.pojo.session;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * <p>session - 基类</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class BaseSession implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * <p>SessionKey</p>
	 */
	protected final String sessionKey;
	
	public BaseSession(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	/**
	 * <p>设置session</p>
	 * 
	 * @param request 请求
	 */
	public void putSession(HttpServletRequest request) {
		final HttpSession session = request.getSession();
		session.setAttribute(this.sessionKey, this);
	}
	
}
