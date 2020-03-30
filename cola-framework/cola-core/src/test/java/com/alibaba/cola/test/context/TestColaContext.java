package com.alibaba.cola.test.context;

import com.alibaba.cola.context.ColaContext;
import com.alibaba.cola.context.ColaContextRunnable;
import com.alibaba.cola.context.ColaContextSupprot;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * SofaContext test
 *
 * @author niexiaolong
 * @date 2018/7/16
 */
public class TestColaContext {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestColaContext.class);

	/**
	 * 父子线程上下文不通示例
	 */
	@Test
	public void testSofaContextLambda_transferFail(){
		PvgContext pvgContext = new PvgContext("1","1","1","1");
		// 父线程将上下文信息设进自己的Threadlocal
		ColaContext.setContext(pvgContext);
		// 子线程取不到父线程的Threadlocal
		IntStream.range(0,3).parallel().forEach(e->{
//			LOGGER.info(ColaContext.getUniqueIdentity(PvgContext.class).toString());
//			Assert.assertTrue(ColaContext.getUniqueIdentity(PvgContext.class) !=null);
			// NullPointException
		});
	}

	/**
	 * 父子线程上下文透传示例
	 */
	@Test
	public void testSofaContextLambda_transferSuccess() {
		// 父线程设置自己的上下文
		ColaContext.setContext(new PvgContext("1","1","1","1"),
							   new TenantContext("t2","ICBU"));
		// 导出父线程全部上下文信息，为透传准备
		Set<ColaContextSupprot> allContext = ColaContext.getAllContext();
		//TenantContext tenantContext = SofaContext.getUniqueIdentity(TenantContext.class);

		// 子线程来取
		IntStream.range(0,3).parallel().forEach(e->{
			// 子线程先设置一遍自己需要的上下文信息
			ColaContext.setContext(allContext);
			//SofaContext.setBizScenarioUniqueIdentity(tenantContext);
			LOGGER.info(ColaContext.getContext(TenantContext.class).toString());
			// 用完后清空（如果每次子线程都是重新setContext一次，该步可以省略）
			ColaContext.clearAllContext();
		});
	}

	/**
	 * 测试并发是否会有线程安全（移除sync锁后，会出现并发问题）
	 */
	@Test
	public void testSofaContext_concurrency(){
		for(int i=0;i<10000;i++){
			new Thread(() -> {
				ColaContext.setContext(new TenantContext("ICBU","1"));
				Assert.assertTrue(ColaContext.getContext(TenantContext.class) != null);
			}).start();
		}
	}

	/**
	 * SofaContextRunnable使用示例
	 * 自动设置并清空上下文信息，只需实现execute方法即可
	 */
	@Test
	public void testSofaContextRunnable() {
		// 父线程设置自己的上下文
		ColaContext.setContext(new PvgContext("1","1","1","1"));
		ColaContext.setContext(new TenantContext("t2","ICBU"));
		// 通过SofaContextRunnable，子线程自动获取父线程，并自动清空
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		executorService.execute(new ColaContextRunnable(ColaContext.getAllContext()) {
			@Override
			public void execute() {
				System.out.println(ColaContext.getContext(PvgContext.class).toString());
				System.out.println(ColaContext.getContext(TenantContext.class).toString());
			}
		});
	}
}
