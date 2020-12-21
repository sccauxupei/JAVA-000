package io.kimmking.rpcfx.demo.provider;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.swing.ListModel;

import org.assertj.core.util.Arrays;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import io.kimmking.rpcfx.api.RpcfxResolver;
import io.kimmking.rpcfx.demo.api.util.FindClassUtil;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * @aothor Master_PXu 
 * @date 时间 2020年12月16日下午7:39:20
 * @project_name 项目名 rpcfx-demo-provider
 * @type_name 类名 ReflectResolver
 * @function 功能 TODO
 */
@Primary
@Component
@Slf4j
public class ReflectResolver implements RpcfxResolver {

	/* (non-Javadoc)
	 * @see io.kimmking.rpcfx.api.RpcfxResolver#resolve(java.lang.String)
	 * 使用Javaassist来进行代理，没有增强
	 */
	@Override
	public Object resolve(String serviceClass) {
		ClassPool pool = ClassPool.getDefault();
		Object person;
		try {
			pool.appendClassPath("/rpcfx-demo-provider/rpcfx-demo-provider/src/main/java");
			CtClass ctClass = pool.get(serviceClass);
			if(ctClass.isInterface()) {
				try {
					//找到当前路径下该接口所有的子类，默认取第一个
					Class<?> interf = this.getClass().getClassLoader().loadClass(serviceClass);
					List<Class<?>>  allSubClass = FindClassUtil.getAllAssignedClass(interf,this.getClass());
					log.info("当前接口所有子类",allSubClass.toString());
					person = allSubClass.get(0).newInstance();
					return person;
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}
		    person = ctClass.toClass().newInstance();
		    return person;
		} catch (NotFoundException | InstantiationException | IllegalAccessException | CannotCompileException e) {
			e.printStackTrace();
		}
		return null;
	}

}
