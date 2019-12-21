package com.acgist.main;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({ "com.acgist.core", "com.acgist.data" })
@DubboComponentScan({ "com.acgist.core.**.service.impl" })
@SpringBootApplication
public class AcgistServiceOrderApplication {

	public static void main(String[] args) {
		ApplicationLauncher.newInstance().run(args, AcgistServiceOrderApplication.class);
	}
	
}
