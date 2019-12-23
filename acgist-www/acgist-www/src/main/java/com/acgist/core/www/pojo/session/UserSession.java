package com.acgist.core.www.pojo.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.acgist.core.www.config.AcgistConstSession;
import com.acgist.core.www.pojo.session.BaseSession;

/**
 * <p>session - 用户</p>
 */
public class UserSession extends BaseSession {

	private static final long serialVersionUID = 1L;

	public UserSession() {
		super(AcgistConstSession.SESSION_USER);
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
		return (UserSession) session.getAttribute(AcgistConstSession.SESSION_USER);
	}
	
}
