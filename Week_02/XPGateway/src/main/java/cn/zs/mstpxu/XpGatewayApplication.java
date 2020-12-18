package cn.zs.mstpxu;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;

@SpringBootApplication
public class XpGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(XpGatewayApplication.class, args);
	}

}
