package com.acgist.core.gateway.controller.admin;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.core.pojo.message.DataResultMessage;
import com.acgist.core.pojo.request.AdminSession;
import com.acgist.core.service.IPermissionService;

/**
 * <p>controller - 权限</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@RestController
@RequestMapping("/admin/permission")
public class PermissionController {

	@Autowired
	private ApplicationContext context;
	@Reference(version = "${acgist.service.version}")
	private IPermissionService permissionService;
	
	@GetMapping
	public DataResultMessage index() {
		final DataResultMessage message = new DataResultMessage();
		final var authoMessage = AdminSession.getAuthoMessage(this.context);
		final var permissions = this.permissionService.allPermission(authoMessage.getRoles());
		message.put("name", authoMessage.getName());
		message.put("roles", permissions); // 所有权限并非角色
//		message.put("avatar", ""); // 头像
//		message.put("introduction", ""); // 描述
		message.buildSuccess();
		return message;
	}
	
}
