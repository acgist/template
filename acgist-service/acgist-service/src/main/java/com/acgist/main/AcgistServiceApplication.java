package com.acgist.main;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.acgist.data.repository.BaseExtendRepositoryImpl;

@EntityScan("com.acgist.data.**.entity")
@EnableAsync
@ComponentScan({ "com.acgist.core", "com.acgist.data" })
@DubboComponentScan({ "com.acgist.core.**.service.impl" })
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.acgist.data.**.repository", repositoryBaseClass = BaseExtendRepositoryImpl.class)
@EnableTransactionManagement
public class AcgistServiceApplication {

	public static void main(String[] args) {
		ApplicationLauncher.getInstance().run(args, AcgistServiceApplication.class);
	}
	
}
