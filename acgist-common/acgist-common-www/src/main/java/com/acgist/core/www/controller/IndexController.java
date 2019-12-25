package com.acgist.core.www.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>控制器 - 首页</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Controller
public class IndexController {

	@Value("${acgist.index:index.html}")
	private String index;
	
	@RequestMapping("/")
	public String index() {
		return this.index;
	}
	
}
