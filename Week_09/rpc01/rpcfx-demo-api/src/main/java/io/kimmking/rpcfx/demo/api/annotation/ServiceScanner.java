package io.kimmking.rpcfx.demo.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @aothor Master_PXu 
 * @date 时间 2020年12月21日上午9:10:26
 * @project_name 项目名 rpcfx-demo-consumer
 * @type_name 类名 ServiceScanner
 * @function 功能 TODO
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ServiceScanner {

}
