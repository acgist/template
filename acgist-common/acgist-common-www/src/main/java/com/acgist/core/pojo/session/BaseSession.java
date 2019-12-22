package com.acgist.core.pojo.session;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * session - 基类
 */
public class BaseSession implements Serializable {
	
	private static final long serialVersionUID = 1L;

	protected String sessionKey;
	
	public BaseSession(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	/**
	 * 设置session
	 */
	public void putSession(HttpServletRequest request) {
		final HttpSession session = request.getSession();
		session.setAttribute(this.sessionKey, this);
	}
	
}
