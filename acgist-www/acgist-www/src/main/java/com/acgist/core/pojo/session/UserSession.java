package com.acgist.core.pojo.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.acgist.core.config.AcgistWwwConstSession;

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
	/**
	 * <p>用户昵称</p>
	 */
	private String nick;

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

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
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
	
	/**
	 * <p>登出</p>
	 * 
	 * @param request 请求
	 */
	public static final void logout(HttpServletRequest request) {
		request.getSession().removeAttribute(AcgistWwwConstSession.SESSION_USER);
	}
	
	/**
	 * <p>判断是否已经登陆</p>
	 * 
	 * @param request 请求
	 * 
	 * @return 是否已经登陆
	 */
	public static final boolean exist(HttpServletRequest request) {
		return get(request) != null;
	}
	
}
