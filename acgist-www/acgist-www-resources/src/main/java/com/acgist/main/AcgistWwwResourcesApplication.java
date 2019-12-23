package com.acgist.main;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.acgist.main.ApplicationLauncher;

@SpringBootApplication
public class AcgistWwwResourcesApplication {

	public static void main(String[] args) {
		ApplicationLauncher.newInstance().web(args, AcgistWwwResourcesApplication.class);
	}
	
}