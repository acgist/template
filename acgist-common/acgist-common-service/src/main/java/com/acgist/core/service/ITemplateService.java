package com.acgist.core.service;

import java.util.Map;

import com.acgist.data.service.pojo.entity.TemplateEntity;
import com.acgist.data.service.pojo.message.TemplateMessage;

/**
 * <p>服务 - 模板</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public interface ITemplateService {

	/**
	 * <p>模板转换文本</p>
	 * 
	 * @param type 模板类型
	 * @param data 数据
	 * 
	 * @return 模板内容
	 */
	TemplateMessage build(TemplateEntity.Type type, Map<String, Object> data);
	
}
