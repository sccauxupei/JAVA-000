package io.kimmking.rpcfx.demo.consumer;

/**
 * @aothor Master_PXu 
 * @date 时间 2020年12月18日下午8:40:42
 * @project_name 项目名 rpcfx-demo-consumer
 * @type_name 类名 TestInterceptor
 * @function 功能 TODO
 */
public class TestInterceptor{
	public Object get(Object argument) {
		System.err.println("test" + argument);
		return null;
	}
}
