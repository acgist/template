package com.acgist.test;

import org.junit.Test;

import com.acgist.utils.BeanUtils;

public class BeanUtilsTest extends BaseTest {

	@Test
	public void testNewInstanceCost() {
		this.cost();
		for (int index = 0; index < 100000; index++) {
			BeanUtils.newInstance("com.acgist.core.gateway.response.GatewayResponse");
		}
		this.costed();
	}
	
}
