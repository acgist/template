package com.acgist.core.service;

import com.acgist.data.service.pojo.message.PermissionMessage;

/**
 * <p>service - 权限</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public interface IPermissionService {

	/**
	 * <p>获取权限</p>
	 * <p>获取权限建议缓存不用每次进行查询</p>
	 * 
	 * @return 权限信息
	 */
	PermissionMessage getPermission();
	
}
