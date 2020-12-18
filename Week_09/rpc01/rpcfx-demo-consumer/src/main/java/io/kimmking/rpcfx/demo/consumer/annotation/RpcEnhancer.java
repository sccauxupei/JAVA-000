package io.kimmking.rpcfx.demo.consumer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @aothor Master_PXu 
 * @date 时间 2020年12月16日下午9:47:19
 * @project_name 项目名 rpcfx-demo-consumer
 * @type_name 类名 RpcEnhancer
 * @function 功能 TODO
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RpcEnhancer {
	String url() default "";
	Class<?> serviceClass() default Object.class;
}
