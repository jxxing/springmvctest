package jxx.service.proxy;

import java.lang.reflect.Proxy;

import jxx.service.IndexService;
import jxx.service.impl.IndexServiceImpl;

import org.junit.Test;

public class ProxyTest {
	@Test
	public void testProxy() throws Throwable {
		System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
		// 实例化目标对象
		IndexService indexService = new IndexServiceImpl();

		// 实例化InvocationHandler
		MyInvocationHandler invocationHandler = new MyInvocationHandler(indexService);

		// 根据目标对象生成代理对象
		IndexService proxy = (IndexService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), indexService
				.getClass().getInterfaces(), invocationHandler);

		// 调用代理对象的方法
		proxy.writer("");

	}

	@Test
	public void testGenerateProxyClass() {
		ProxyGeneratorUtils.writeProxyClassToHardDisk("D:/$Proxy11.class");
	}
}
