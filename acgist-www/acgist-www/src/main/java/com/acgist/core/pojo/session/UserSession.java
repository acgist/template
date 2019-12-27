package com.acgist.core.pojo.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.acgist.core.config.AcgistWwwConstSession;
import com.acgist.core.pojo.session.BaseSession;

/**
 * <p>session - 用户</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public class UserSession extends BaseSession {

	private static final long serialVersionUID = 1L;

	public UserSession() {
		super(AcgistWwwConstSession.SESSION_USER);
	}

	/**
	 * <p>用户ID</p>
	 */
	private String id;
	/**
	 * <p>用户名称</p>
	 */
	private String name;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * <p>获取用户Session</p>
	 * 
	 * @param request 请求
	 * 
	 * @return 用户Session
	 */
	public static final UserSession get(HttpServletRequest request) {
		final HttpSession session = request.getSession();
		return (UserSession) session.getAttribute(AcgistWwwConstSession.SESSION_USER);
	}
	
}
