package com.acgist.core.gateway.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acgist.core.gateway.service.RoleService;
import com.acgist.core.pojo.message.DataResultMessage;

/**
 * <p>controller - 角色</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@RestController
@RequestMapping("/admin/role")
public class RoleController {

	@Autowired
	private RoleService roleService;
	
	@GetMapping
	public DataResultMessage index() {
		final DataResultMessage message = new DataResultMessage();
		message.buildSuccess();
		message.setData(this.roleService.all());
		return message;
	}
	
	@GetMapping("/routes")
	public DataResultMessage routes() {
		final DataResultMessage message = new DataResultMessage();
		message.buildSuccess();
		message.setData(this.roleService.routes());
		return message;
	}
	
}
