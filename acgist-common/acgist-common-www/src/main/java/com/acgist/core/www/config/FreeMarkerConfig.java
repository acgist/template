package com.acgist.core.www.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * <p>config - FreeMarker</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Configuration
public class FreeMarkerConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(FreeMarkerConfig.class);
	
	@Value("${acgist.static.url:}")
	private String staticUrl;
	@Value("${spring.freemarker.settings.classic_compatible:true}")
	private Boolean compatible;
	
	@Autowired
	private freemarker.template.Configuration configuration;

	@PostConstruct
	public void init() throws Exception {
		LOGGER.info("设置FreeMarker静态文件域名：{}", staticUrl);
		this.configuration.setSharedVariable("staticUrl", this.staticUrl);
		LOGGER.info("设置FreeMarker空值优化处理");
		this.configuration.setSetting("classic_compatible", this.compatible.toString());
	}

}
