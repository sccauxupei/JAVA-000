package cn.zs.mstpxu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class XpGatewayApplicationTests {
	@Value("${weather.appid}")
	private String a;
	
	@Test
    public void contextLoads() {
		System.err.println(a);
	}

}
