package com.acgist.core.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * <p>service - 静态化</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Service
public class StaticService {

	@Autowired
	private FreeMarkerService freeMarkerService;
	
	@Value("${acgist.index:index.html}")
	private String index;
	@Value("${acgist.html.path:}")
	private String htmlPath;
	@Value("${acgist.index.template:index.ftl}")
	private String indexTemplate;
	
	/**
	 * <p>静态化首页</p>
	 * 
	 * @param data 数据
	 */
	public void buildIndex(Map<Object, Object> data) {
		this.freeMarkerService.build(this.indexTemplate, data, this.htmlPath, this.index);
	}
	
}
