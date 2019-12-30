package com.acgist.test;

import org.junit.Test;

import com.acgist.core.gateway.response.GatewayResponse;
import com.acgist.utils.JSONUtils;

public class JSONUtilsTest extends BaseTest {

	@Test
	public void testCost() {
		this.cost();
		GatewayResponse response = new GatewayResponse();
		response.setCode("0000");
		response.setMessage("成功");
		String json;
		for (int index = 0; index < 100000; index++) {
			json = JSONUtils.toJSON(response);
			JSONUtils.toJava(json, GatewayResponse.class);
		}
		this.costed();
	}
	
	@Test
	public void testJson() {
		GatewayResponse response = new GatewayResponse();
		response.setCode("0000");
		response.setMessage("成功");
		String json;
		json = JSONUtils.toJSON(response);
		this.log(json);
		response = JSONUtils.toJava(json, GatewayResponse.class);
		this.log(response.getCode());
		this.log(response.getMessage());
	}
	
}
