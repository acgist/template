package com.acgist.main;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.acgist.data.repository.BaseExtendRepositoryImpl;

@EntityScan("com.acgist.data.**.entity")
@ComponentScan({ "com.acgist.core", "com.acgist.data" })
@EnableScheduling
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.acgist.data.**.repository", repositoryBaseClass = BaseExtendRepositoryImpl.class)
@EnableRedisHttpSession
@EnableTransactionManagement
public class AcgistWwwApplication {

	public static void main(String[] args) {
		ApplicationLauncher.getInstance().web(args, AcgistWwwApplication.class);
	}

}
