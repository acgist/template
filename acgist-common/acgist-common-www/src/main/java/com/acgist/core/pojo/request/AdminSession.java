package com.acgist.core.pojo.request;

import java.io.Serializable;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.acgist.data.pojo.entity.PermissionEntity;
import com.acgist.data.pojo.message.AuthoMessage;

/**
 * <p>后台组件</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Component
@Scope("request")
public final class AdminSession implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final AdminSession getInstance(ApplicationContext context) {
		return context.getBean(AdminSession.class);
	}

	public static final AuthoMessage getAuthoMessage(ApplicationContext context) {
		final var session = getInstance(context);
		if(session == null) {
			return null;
		}
		return session.getAuthoMessage();
	}
	
	/**
	 * <p>授权</p>
	 */
	private AuthoMessage authoMessage;
	/**
	 * <p>授权</p>
	 */
	private PermissionEntity permission;

	public AuthoMessage getAuthoMessage() {
		return authoMessage;
	}

	public void setAuthoMessage(AuthoMessage authoMessage) {
		this.authoMessage = authoMessage;
	}

	public PermissionEntity getPermission() {
		return permission;
	}

	public void setPermission(PermissionEntity permission) {
		this.permission = permission;
	}
	
}
