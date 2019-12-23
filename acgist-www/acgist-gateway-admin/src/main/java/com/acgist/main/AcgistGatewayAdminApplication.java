package com.acgist.main;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.acgist.data.repository.BaseExtendRepositoryImpl;

@EntityScan("com.acgist.data.**.entity")
@ComponentScan({ "com.acgist.core", "com.acgist.data" })
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.acgist.data.**.repository", repositoryBaseClass = BaseExtendRepositoryImpl.class)
@EnableTransactionManagement
public class AcgistGatewayAdminApplication {

	public static void main(String[] args) {
		ApplicationLauncher.newInstance().web(args, AcgistGatewayAdminApplication.class);
	}

}