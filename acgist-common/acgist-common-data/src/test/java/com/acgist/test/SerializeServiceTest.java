package com.acgist.test;

import org.junit.Test;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.acgist.data.service.SerializeService;

public class SerializeServiceTest extends BaseTest {

	@Test
	@SuppressWarnings("unchecked")
	public void testSerialize() throws Exception {
		var source = new String[] {"1", "2"};
//		var source = new ResultMessage();
//		source.setCode("1234");
		RedisSerializer<Object> service = (RedisSerializer<Object>) SerializeService.buildValueSerializer();
		var bytes = service.serialize(source);
		var object = service.deserialize(bytes);
		this.log(new String(bytes));
		this.log(object);
		this.log(object.getClass());
	}
	
}
