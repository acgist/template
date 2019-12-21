package com.acgist.main;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.acgist.core", "com.acgist.data"})
@SpringBootApplication
public class AcgistWwwApplication {

	public static void main(String[] args) {
		ApplicationLauncher.newInstance().web(args, AcgistWwwApplication.class);
	}

}
