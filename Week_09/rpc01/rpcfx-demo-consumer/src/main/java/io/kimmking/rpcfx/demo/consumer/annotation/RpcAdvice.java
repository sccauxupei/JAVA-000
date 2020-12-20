package io.kimmking.rpcfx.demo.consumer.annotation;


import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @aothor Master_PXu 
 * @date 时间 2020年12月16日下午3:31:30
 * @project_name 项目名 rpcfx-demo-provider
 * @type_name 类名 TimeCount
 * @function 功能 TODO
 */

@Aspect
@Component
@Slf4j
@Deprecated
public class RpcAdvice {
	
    public static final MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");
	private Class<?> classType;
	private String url;
	
	@Pointcut("@annotation(io.kimmking.rpcfx.demo.consumer.annotation.RpcEnhancer)")
    public void agrsAop() {}

	//这里需要自己拼接字节码
	//这个方法并不好，只是实现下试试看，其实不如直接用JDK的InterceptorHandler。
	//因为这里也直接用了InterceptorHandler
    @Around("agrsAop()")
    public Object doProcess(ProceedingJoinPoint point) throws Throwable {
    	
    	System.err.println("success");
        // 这里判断response.status，处理异常
        // 考虑封装一个全局的RpcfxException
        return null;
    }
    
    @Before("execution(* io.kimmking.rpcfx.demo.consumer.*TargetTest.*(..))")
    public void permissionCheck() {
        System.out.println("模拟权限检查");
    }
    
    
    /**
	 * @param l
	 * @return
	 */
	private boolean isWebUrl(Object l) {
		return l.toString().charAt(1) == '.' 
				|| l.toString().charAt(2) == '.' || l.toString().charAt(3) == '.';
	}

	private static RpcfxResponse post(RpcfxRequest req, String url) throws IOException {
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
	
	public static class JavaassistInterceptor implements InvocationHandler{
		Object target;
		RpcfxRequest request;
		String url;

		public JavaassistInterceptor(Object target, RpcfxRequest request, String url) {
			this.target = target;
			this.request = request;
			this.url = url;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
            request.setMethod(method.getName());
            request.setParams(params);
            RpcfxResponse response = post(request, url);
            return JSON.parse(response.getResult().toString());
		}
		
	}
}
