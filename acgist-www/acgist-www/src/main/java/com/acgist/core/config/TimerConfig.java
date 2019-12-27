package com.acgist.core.config;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import com.acgist.core.service.StaticService;

/**
 * <p>config - 定时任务</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Configuration
public class TimerConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(TimerConfig.class);
	
	@Autowired
	private StaticService staticService;
	
	/**
	 * <p>静态化首页</p>
	 */
	@Scheduled(fixedRate = 30L * 60 * 1000)
	public void buildIndex() {
		LOGGER.info("静态化首页");
		this.staticService.buildIndex(Map.of());
	}
	
}
