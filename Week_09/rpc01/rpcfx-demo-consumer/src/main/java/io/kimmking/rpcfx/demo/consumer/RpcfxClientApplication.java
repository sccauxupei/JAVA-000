package io.kimmking.rpcfx.demo.consumer;

import io.kimmking.rpcfx.client.Rpcfx;
import io.kimmking.rpcfx.demo.api.Order;
import io.kimmking.rpcfx.demo.api.OrderService;
import io.kimmking.rpcfx.demo.api.User;
import io.kimmking.rpcfx.demo.api.UserService;
import io.kimmking.rpcfx.demo.consumer.testinf.UserTestService;
import net.bytebuddy.implementation.Implementation.Context;

import static org.mockito.Mockito.RETURNS_SMART_NULLS;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import org.apache.naming.factory.BeanFactory;
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

	// 二方库
	// 三方库 lib
	// nexus, userserivce -> userdao -> user
	//
	public static void main(String[] args) {

		// UserService service = new xxx();
		// service.findById

		// 新加一个OrderService
		ApplicationContext context = SpringApplication.run(RpcfxClientApplication.class, args);
		try {
//			RpcAspectJ utilAspectJ = context.getBean(RpcAspectJ.class);
//			Object object = utilAspectJ.generateAOPProxClass(UserService.class, "http://localhost:8080/");
			UserService service = (UserService) context.getBean(UserService.class.getName());
			System.err.println(service.findById(1));
			OrderService orderService = (OrderService) context.getBean(OrderService.class.getName());
			System.err.println(orderService.findOrderById(1));
		} catch (BeansException e) {
			e.printStackTrace();
		}


	}
	
	@Controller
	@RequestMapping("/find")
	public class FindServiceController{	
		@Autowired
		RpcAspectJ aspect;
		
		@RequestMapping("/query")
		@ResponseBody
		public void rpcQuery() {
			UserService userService = aspect.create(UserService.class, "http://localhost:8080/");
			User user = userService.findById(1);
			System.out.println("find user id=1 from server: " + user.getName());
			
			OrderService orderService = aspect.create(OrderService.class, "http://localhost:8080/");
			Order order = orderService.findOrderById(1992129);
			System.out.println(String.format("find order name=%s, amount=%f",order.getName(),order.getAmount()));
		}
	}

}
