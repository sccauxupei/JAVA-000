package io.kimmking.rpcfx.demo.consumer;

import io.kimmking.rpcfx.client.Rpcfx;
import io.kimmking.rpcfx.demo.api.Order;
import io.kimmking.rpcfx.demo.api.OrderService;
import io.kimmking.rpcfx.demo.api.User;
import io.kimmking.rpcfx.demo.api.UserService;
import io.kimmking.rpcfx.demo.api.util.FindClassUtil;
import io.kimmking.rpcfx.demo.consumer.testinf.UserTestService;
import net.bytebuddy.implementation.Implementation.Context;

import static org.mockito.Mockito.RETURNS_SMART_NULLS;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.naming.factory.BeanFactory;
import org.aspectj.weaver.loadtime.Aj;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yaml.snakeyaml.Yaml;

@SpringBootApplication
public class RpcfxClientApplication {
	private static void init() {
		Map<String,String> resource = getYmlConfig();
		//根据配置的扫描包路径扫描路径下的接口
		String pk = resource.get("scanPackage");
        String uri = resource.get("url");
        String path = pk.replace('.', '/');  
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();  
        URL url = classloader.getResource(path);
        List<Class<?>> interfs;
		try {
			interfs = FindClassUtil.getAssignInterFace(new File(url.getFile()), pk);
	        //获取到所有接口后，迭代调用 generateAOPProxClass 进行增强并注入BeanFactory
	        for(Class<?> serviceClass : interfs) {
	        	 RpcAspectJ.generateAOPProxClass(serviceClass,uri);
	        }
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	//获取配置文件中的url和Service所在的包
	private static Map<String, String> getYmlConfig() {
	    Map<String, String> result = new HashMap<>();
	    //通过yaml对象将配置文件的输入流转换成map原始map对象
		String url = "http://localhost:8080/";
		String scanPackage = "io.kimmking.rpcfx.demo.api";
		result.put("url", url);
		result.put("scanPackage",scanPackage);
	    return result;
	}
	// 二方库
	// 三方库 lib
	// nexus, userserivce -> userdao -> user
	//
	public static void main(String[] args) {

		// UserService service = new xxx();
		// service.findById
		
		//用了傻办法，在ApplicationContext被创建前写class进ClassPath下....成功切进去了
		//我了解了下，如果自定义生成的类要被AOP切进去，似乎需要在Bean定义被解析的那个时间点注入。
		init();
		// 新加一个OrderService
		ApplicationContext context = SpringApplication.run(RpcfxClientApplication.class, args);
		try {
			UserService service = (UserService) context.getBean(UserService.class);
			System.err.println(service.findById(1));
//			OrderService orderService = (OrderService) context.getBean(OrderService.class);
//			System.err.println(orderService.findOrderById(1));
		} catch (BeansException e) {
			e.printStackTrace();
		}


	}
	@Controller
	@RequestMapping("/find")
	public class FindServiceController{	
//		@Autowired
//		RpcAspectJ aspect;
		
		@RequestMapping("/query")
		@ResponseBody
		public void rpcQuery() {
//			UserService userService = aspect.create(UserService.class, "http://localhost:8080/");
//			User user = userService.findById(1);
//			System.out.println("find user id=1 from server: " + user.getName());
//			
//			OrderService orderService = aspect.create(OrderService.class, "http://localhost:8080/");
//			Order order = orderService.findOrderById(1992129);
//			System.out.println(String.format("find order name=%s, amount=%f",order.getName(),order.getAmount()));
		}
	}

}
