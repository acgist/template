package com.acgist.core.pojo.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.acgist.core.config.AcgistConstSession;
import com.acgist.core.pojo.session.BaseSession;
import com.acgist.core.pojo.session.UserSession;

/**
 * session - 用户
 */
public class UserSession extends BaseSession {

	private static final long serialVersionUID = 1L;

	public UserSession() {
		super(AcgistConstSession.SESSION_USER);
	}

	private String id; // 用户ID
	private String name; // 用户名称

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
	 * 获取session
	 */
	public static final UserSession get(HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserSession user = (UserSession) session.getAttribute(AcgistConstSession.SESSION_USER);
		return user;
	}
	
}
