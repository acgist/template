package com.acgist.main;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({ "com.acgist.core", "com.acgist.data" })
@SpringBootApplication
public class AcgistServiceOrderApplication {

	public static void main(String[] args) {
		ApplicationLauncher.newInstance().run(args, AcgistServiceOrderApplication.class);
	}
	
}
