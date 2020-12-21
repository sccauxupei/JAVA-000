package io.kimmking.rpcfx.demo.consumer;


import static org.hamcrest.CoreMatchers.nullValue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.yaml.snakeyaml.Yaml;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;

import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.demo.api.util.FindClassUtil;
import io.kimmking.rpcfx.demo.consumer.annotation.RpcEnhancer;
import javassist.bytecode.stackmap.MapMaker;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.DynamicType.Unloaded;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

//@Component
//@Configuration
public class RpcAspectJ{
	static {
		ParserConfig.getGlobalInstance().addAccept("io.kimmking");
	}
	private static final URL URL= new RpcAspectJ().getClass().getResource("/");
        
    //调用时机不对，我试试大概在Bean初始化阶段把所有的Service接口都增强丢进Spring管理，这个方法是在初始化完毕之后调用的,肯定得不到AOP增强。
    public static <T> void generateAOPProxClass(final Class<T> serviceClass,final String url){	
    	//判断是否需要增强
		Method[] targetMehtod = serviceClass.getDeclaredMethods();	
		//为抽象类方法添加注解和函数体的写法,将serviceClass,url传递
		//误区：AspectJ似乎是编译时增强的，其实这也可以理解，毕竟在@Pointcut("@annotation())这里应该猜到的，如果不是编译期增强，那整个扫包、查看注解过程其实很长的
		//但AOP调用并没又很慢，所以应该猜一下是编译期增强的。
		try {
			DynamicType.Unloaded<T> aopTarget = (Unloaded<T>) new ByteBuddy().with(new NamingStrategy.AbstractBase() {//==subclass().name("io.kimmking.rpcfx.demo.consumer.TRpcSample")==
						@Override
						protected String name(TypeDescription superClass) {
					        return "io.kimmking.rpcfx.demo.consumer.generate." + superClass.getSimpleName() + "TargetTest";
						}
					  })
					.subclass(serviceClass,ConstructorStrategy.Default.DEFAULT_CONSTRUCTOR)
					.method(ElementMatchers.anyOf(targetMehtod))
//					.intercept(MethodDelegation.to(Interceptor.class))//报错None of [protected void java.lang.Object.finalize() throws java.lang.Throwable,...]allows for delegation from public abstract io.kimmking.rpcfx.demo.api.User io.kimmking.rpcfx.demo.api.UserService.findById(int)
//					.intercept(new Implementation.Simple(new ByteCodeAppender() {//拼接字节码用的类
//						@Override
//						public Size apply(MethodVisitor methodVisitor, Context implementationContext,
//								MethodDescription instrumentedMethod) {
//						      StackManipulation.Size operandStackSize = new StackManipulation.Compound(
//						    		  MethodReturn.REFERENCE
//						      ).apply(methodVisitor, implementationContext);
//						      return new Size(operandStackSize.getMaximalSize(),
//						                      instrumentedMethod.getStackSize());
//						}
//					  }))
					.intercept(FixedValue.nullValue())
					.annotateMethod(AnnotationDescription.Builder.ofType(RpcEnhancer.class).define("url", url).define("serviceClass", serviceClass).build())
					.annotateType(AnnotationDescription.Builder.ofType(Component.class).build())
					.annotateType(AnnotationDescription.Builder.ofType(Primary.class).build())
					.make();
//			将.class文件存入路径不太好，1、是再次运行的时候mJVM就会主动load .class 文件，这行代码就会重复加载;2、是这行代码主要意义是在运行时动态修改字节码，\G
//			虽然修改后的字节码并没有起到作用，可能再次run就可以被容器增强管理到，但那样并不是我预期的。所以这种方法我应该是用错了。
//			或者需要老老实实的把HttpClient通信的方法一个个拼进字节码...不知道是不是这样，想想工程量似乎有点大...听说bytebuddy很好用，\G
//			但我似乎没整明白，看它GitHub上面的Demo似乎只有一些简单类型的增强，要定制化的话似乎是要从预期的字节码逆推，然后再拼接进去？
			try {
				aopTarget.saveIn(new File(URL.getPath()));
			} catch (Exception e) {
				e.printStackTrace();
			}
//			Class<?> result = aopTarget.load(new RpcAspectJ().getClass().getClassLoader()).getLoaded();
			//生成的同时注册进Spring容器中，以便被AOP代理，注册Bean定义提升到上一层
//			GenericWebApplicationContext pContext =  (GenericWebApplicationContext) context;
//			ConfigurableBeanFactory factory =  pContext.getBeanFactory();
//			Object object = result.newInstance();
//			String name = serviceClass.getName();
//			factory.registerSingleton(name, object);
//		} catch (IllegalArgumentException | SecurityException | IllegalStateException | InstantiationException | IllegalAccessException e) {
		}finally {}
    }
}
