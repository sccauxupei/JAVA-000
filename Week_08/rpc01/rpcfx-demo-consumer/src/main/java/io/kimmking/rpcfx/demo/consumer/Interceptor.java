package io.kimmking.rpcfx.demo.consumer;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import javassist.expr.NewArray;
import net.bytebuddy.asm.Advice.AllArguments;
import net.bytebuddy.asm.Advice.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

/**
 * @aothor Master_PXu 
 * @date 时间 2020年12月17日下午4:36:22
 * @project_name 项目名 rpcfx-demo-consumer
 * @type_name 类名 Interceptor
 * @function 功能 TODO
 */
public class Interceptor {
	@RuntimeType
    public static Object intercept(@AllArguments Object[] allArguments, @Origin Method method, @SuperCall Callable<Object> callable) throws Exception {
		System.err.println("entry method...");
		return callable.call();
	}
}
