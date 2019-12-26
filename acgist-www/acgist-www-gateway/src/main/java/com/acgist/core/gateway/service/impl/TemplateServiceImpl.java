package com.acgist.core.gateway.service.impl;

import java.util.Map;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.acgist.core.config.AcgistCode;
import com.acgist.core.exception.ErrorCodeException;
import com.acgist.core.service.ITemplateService;
import com.acgist.core.www.service.FreeMarkerService;
import com.acgist.data.gateway.repository.TemplateRepository;
import com.acgist.data.service.pojo.entity.TemplateEntity;
import com.acgist.data.service.pojo.entity.TemplateEntity.Type;
import com.acgist.data.service.pojo.message.TemplateMessage;

/**
 * <p>服务 - 模板</p>
 */
@Service(retries = 0, version = "${acgist.service.version}")
public class TemplateServiceImpl implements ITemplateService {

	@Autowired
	private FreeMarkerService freeMarkerService;
	@Autowired
	private TemplateRepository templateRepository;
	
	@Override
	public TemplateMessage build(Type type, Map<String, Object> data) {
		final TemplateEntity template = this.templateRepository.findByType(type);
		if(template == null) {
			throw new ErrorCodeException(AcgistCode.CODE_3003, "没有配置模板");
		}
		final String content = this.freeMarkerService.templateConvert(template.getContent(), data);
		final TemplateMessage message = new TemplateMessage();
		message.setName(template.getName());
		message.setContent(content);
		return message;
	}

}
