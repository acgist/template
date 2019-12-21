package com.acgist.main;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AcgistWwwApplication {

	public static void main(String[] args) {
		ApplicationLauncher.newInstance().web(args, AcgistWwwApplication.class);
	}

}
