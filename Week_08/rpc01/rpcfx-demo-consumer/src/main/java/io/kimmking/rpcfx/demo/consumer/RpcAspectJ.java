package io.kimmking.rpcfx.demo.consumer;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URL;

import org.aspectj.lang.annotation.Pointcut;
import org.hibernate.validator.internal.util.privilegedactions.NewInstance;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cglib.reflect.MethodDelegate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.GenericWebApplicationContext;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;

import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.demo.consumer.annotation.RpcEnhancer;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.DynamicType.Unloaded;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.Implementation.Context;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bytecode.ByteCodeAppender;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.implementation.bytecode.member.MethodReturn;
import net.bytebuddy.jar.asm.MethodVisitor;
import net.bytebuddy.matcher.ElementMatchers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

@Component
public class RpcAspectJ implements ApplicationContextAware{
	private static final URL URL= new RpcAspectJ().getClass().getResource("/");
	private ApplicationContext context;
    static {
        ParserConfig.getGlobalInstance().addAccept("io.kimmking");
    }

    public <T> T generateAOPProxClass(final Class<T> serviceClass,final String url){
    	//判断是否需要增强
    	Method[] delegateMehtod = serviceClass.getDeclaredMethods();
    	//获取实现类
//			Class<T> delegateClass = (Class<T>) generateProxClass(serviceClass, url, delegateMehtod).getClass();
		Method[] targetMehtod = serviceClass.getDeclaredMethods();
		//为抽象类方法添加注解和函数体的写法,将serviceClass,url传递
		//误区：AspectJ似乎是编译时增强的，其实这也可以理解，毕竟在@Pointcut("@annotation())这里应该猜到的，如果不是编译期增强，那整个扫包、查看注解过程其实很长的
		//但AOP调用并没又很慢，所以应该猜一下是编译期增强的。
		//或者是我Spring生命周期的问题，或许试试pre
		try {
			DynamicType.Unloaded<T> aopTarget = (Unloaded<T>) new ByteBuddy().with(new NamingStrategy.AbstractBase() {//==subclass().name("io.kimmking.rpcfx.demo.consumer.TRpcSample")==
						@Override
						protected String name(TypeDescription superClass) {
					        return "io.kimmking.rpcfx.demo.consumer." + superClass.getSimpleName() + "TargetTest";
						}
					  })
					.subclass(serviceClass,ConstructorStrategy.Default.DEFAULT_CONSTRUCTOR)
					.method(ElementMatchers.anyOf(targetMehtod))
//					.intercept(MethodDelegation.to(Interceptor.class))//报错None of [protected void java.lang.Object.finalize() throws java.lang.Throwable,...]allows for delegation from public abstract io.kimmking.rpcfx.demo.api.User io.kimmking.rpcfx.demo.api.UserService.findById(int)
//					.intercept(new Implementation.Simple(new ByteCodeAppender() {
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
					.make();
			aopTarget.saveIn(new File(URL.getPath()));
			
			Class<?> result = aopTarget.load(this.getClass().getClassLoader()).getLoaded();
			//生成的同时注册进Spring容器中，以便被AOP代理
			GenericWebApplicationContext pContext =  (GenericWebApplicationContext) context;
			ConfigurableBeanFactory factory =  pContext.getBeanFactory();
			Object object = result.newInstance();
			String name = serviceClass.getName();
			factory.registerSingleton(name, object);
			return (T) result;
		} catch (IllegalArgumentException | SecurityException | IllegalStateException | InstantiationException | IllegalAccessException | IOException e) {
			e.printStackTrace();
		}
    	
    	return null;
    }
    /**
     * 这里通过字节码生成生成serviceClass的代理实现类
     * ....然后再增强实现类，增加注解实现AOP...感觉做的比较愚蠢...
     * 但字节码拼那么长的逻辑...确实很让人头大...
     * @param serciceClass
     * @param url
     * @return
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    public <T> T generateProxClass(final Class<T> serviceClass,final String url,Method[] delegateMehtod) throws InstantiationException, IllegalAccessException {
//    	List<Object> fitMethod = Arrays.asList(delegateMehtod).stream()
//    					.filter(l->{return isObjectMethod(l);}).collect(Collectors.toList());
    	
    	try {
    		//先生成默认实现类，再用字节码增强实现类，把注解加到实现类方法上
    		Unloaded<T> target = (Unloaded<T>) new ByteBuddy().with(new NamingStrategy.AbstractBase() {//==subclass().name("io.kimmking.rpcfx.demo.consumer.TRpcSample")==
						@Override
						protected String name(TypeDescription superClass) {
    				        return "io.kimmking.rpcfx.demo.consumer." + superClass.getSimpleName() + "Test";
						}
    				  })
					  .subclass(serviceClass,ConstructorStrategy.Default.DEFAULT_CONSTRUCTOR)
					  .method(ElementMatchers.anyOf(delegateMehtod))
					  .intercept(MethodDelegation.to(new TestInterceptor()))
//					  .intercept(new Implementation.Simple(new ByteCodeAppender() {
//						@Override
//						public Size apply(MethodVisitor methodVisitor, Context implementationContext,
//								MethodDescription instrumentedMethod) {
////						    if (!instrumentedMethod.getReturnType().asErasure().represents(Object.class)) {
////						        throw new IllegalArgumentException(instrumentedMethod + " must return Object");
////						      }
//						      StackManipulation.Size operandStackSize = new StackManipulation.Compound(
//						    		  MethodReturn.REFERENCE
//						      ).apply(methodVisitor, implementationContext);
//						      return new Size(operandStackSize.getMaximalSize(),
//						                      instrumentedMethod.getStackSize());
//						}
//					  }))
					  .make();
    		          target.saveIn(new File(URL.getPath()));
					  Class<?> result = target.load(new RpcAspectJ().getClass().getClassLoader()).getLoaded();
					  
    				return (T) result.newInstance();
		} catch (IllegalArgumentException | SecurityException | IOException e) {
			e.printStackTrace();
		}

    	return (T) new Object();
    }


	/**
     *     想基于Spring AspectJ的AOP由于接口不能被Spring管理，想要用AspectJ AOP进行替换代理，则OrderService之类的Service需要定义为一个类才行。
     *     也可以直接切这个方法，但性能就很慢了。
     *     听课之后：用字节码增强，但还是想试下AspectJ来试一下,原Rpcfx类为final修饰，无法被增强，因此需要去掉final关键字
     * @param serviceClass
     * @param url
     * @return
     */
    @RpcEnhancer
    public <T> T create(final Class<T> serviceClass, final String url) {
        // 0. 替换动态代理 -> AOP
    	Method[] delegateMehtod = serviceClass.getDeclaredMethods();
    	return null;
//        return (T) Proxy.newProxyInstance(RpcAspectJ.class.getClassLoader(), new Class[]{serviceClass}, new MockHandler());

    }
    
    @Slf4j
    public static class RpcfxInvocationHandler implements InvocationHandler {
    	
        public static final MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");

        private final Class<?> serviceClass;
        private final String url;
        public <T> RpcfxInvocationHandler(Class<T> serviceClass, String url) {
            this.serviceClass = serviceClass;
            this.url = url;
        }

        // 可以尝试，自己去写对象序列化，二进制还是文本的，，，rpcfx是xml自定义序列化、反序列化，json: code.google.com/p/rpcfx
        // int byte char float double long bool
        // [], data class

        @Override
        public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
        	long start = System.currentTimeMillis();
            RpcfxRequest request = new RpcfxRequest();
            request.setServiceClass(this.serviceClass.getName());
            request.setMethod(method.getName());
            request.setParams(params);
            log.info("invoke 发送响应前执行时间为：{} ms",System.currentTimeMillis() - start);
            
            RpcfxResponse response = post(request, url);
            
            log.info("invoke 发送响应后执行时间为：{} ms",System.currentTimeMillis() - start);
            // 这里判断response.status，处理异常
            // 考虑封装一个全局的RpcfxException

            return JSON.parse(response.getResult().toString());
        }

        private RpcfxResponse post(RpcfxRequest req, String url) throws IOException {
        	long start = System.currentTimeMillis();
            String reqJson = JSON.toJSONString(req);
            System.out.println("req json: "+reqJson);

            // 1.可以复用client
            // 2.尝试使用httpclient或者netty client
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(JSONTYPE, reqJson))
                    .build();
            String respJson = client.newCall(request).execute().body().string();
            System.out.println("resp json: "+respJson);
            log.info("post 执行时间为：{} ms",System.currentTimeMillis() - start);
            return JSON.parseObject(respJson, RpcfxResponse.class);
        }
    }

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		// TODO Auto-generated method stub
		this.context = context;
	}

}
