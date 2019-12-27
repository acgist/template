package com.acgist.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.acgist.core.service.OrderService;

/**
 * <p>controller - 订单</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	private OrderService orderService;
	
	@GetMapping("/build")
	@ResponseBody
	public String build() {
		this.orderService.build();
		return "success";
	}
	
}
