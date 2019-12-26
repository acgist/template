package com.acgist.test;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.junit.Test;

import com.acgist.core.service.IPermissionService;

public class DubboTest extends BaseTest {

	@Test
	public void execute() {
		final var registry= new RegistryConfig();
		registry.setAddress("zookeeper://127.0.0.1:2181");
		registry.setTimeout(30000);
		ApplicationConfig config = new ApplicationConfig();
		config.setName("acgist-test");
		final var reference = new ReferenceConfig<IPermissionService>();
		reference.setInterface(IPermissionService.class);
		reference.setApplication(config);
		reference.setRegistry(registry);
		reference.setVersion("1.0.0");
		reference.setTimeout(30000);
		final var service = reference.get();
		final var result = service.getPermission();
		this.log(result);
	}
	
}
