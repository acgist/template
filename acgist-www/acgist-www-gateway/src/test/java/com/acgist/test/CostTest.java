package com.acgist.test;

import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.acgist.core.service.IUserService;
import com.acgist.data.service.pojo.message.AuthoMessage;
import com.acgist.main.AcgistWwwGatewayApplication;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = AcgistWwwGatewayApplication.class)
public class CostTest {

	@Reference(version = "${acgist.service.version}")
	private IUserService userService;
	
	@Test
	public void testCost() {
		AuthoMessage message = null;
		long begin = System.currentTimeMillis();
		for (int i = 0; i < 100000; i++) {
			message = userService.getAuthoMessage("test");
		}
		System.out.println(System.currentTimeMillis() - begin);
		System.out.println(message);
	}
	
}
