package com.acgist.core.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.api.core.config.FreemarkerConfig;

/**
 * config - freemarker<br>
 * 添加静态地址、设置默认值
 */
@Configuration
public class FreemarkerConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(FreemarkerConfig.class);
	
	@Value("${system.static.base:}")
	private String staticBase;
	@Value("${spring.freemarker.settings.classic_compatible:true}")
	private Boolean compatible;
	
	@Autowired
	private freemarker.template.Configuration configuration;

	@PostConstruct
	public void init() throws Exception {
		LOGGER.info("初始化freemarker静态文件域名：{}", staticBase);
		this.configuration.setSharedVariable("staticBase", this.staticBase);
		LOGGER.info("freemarker空值优化处理");
		this.configuration.setSetting("classic_compatible", this.compatible.toString()); // api-web-admin项目中不知为何没有自动配置此项
	}

}
