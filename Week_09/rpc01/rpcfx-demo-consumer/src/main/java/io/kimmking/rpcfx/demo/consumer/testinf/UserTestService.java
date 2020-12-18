package io.kimmking.rpcfx.demo.consumer.testinf;

import org.springframework.stereotype.Component;

import io.kimmking.rpcfx.demo.consumer.annotation.RpcEnhancer;

/**
 * @aothor Master_PXu 
 * @date 时间 2020年12月17日上午8:46:00
 * @project_name 项目名 rpcfx-demo-consumer
 * @type_name 类名 UserTestService
 * @function 功能 TODO
 */
@Component
public interface UserTestService {
	@RpcEnhancer
	default void test() {System.out.println("Entry");};
}
