package io.kimmking.rpcfx.demo.provider.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @aothor Master_PXu 
 * @date 时间 2020年12月16日下午3:36:54
 * @project_name 项目名 rpcfx-demo-provider
 * @type_name 类名 Time
 * @function 功能 TODO
 */

@Target({ ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Time {
//	String type() default "Time";
}
