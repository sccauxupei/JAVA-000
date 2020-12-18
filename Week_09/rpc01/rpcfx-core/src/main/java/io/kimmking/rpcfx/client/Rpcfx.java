package io.kimmking.rpcfx.client;


import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;

import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;
import javassist.ClassPool;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public final class Rpcfx {
    static {
        ParserConfig.getGlobalInstance().addAccept("io.kimmking");
    }
    /**
     *     想基于Spring AspectJ的AOP由于接口不能被Spring管理，想要用AspectJ AOP进行替换代理，则OrderService之类的Service需要定义为一个类才行。
     *     也可以直接切这个方法，但性能就很慢了。
     *     听课之后：用字节码增强，但还是想试下AspectJ来试一下
     * @see rpcfx-demo-consumer/io.kimmking.rpcfx.demo.consumer.RpcAspectJ
     * @param serviceClass
     * @param url
     * @return
     */
    public static <T> T create(final Class<T> serviceClass, final String url) {
        // 0. 替换动态代理 -> AOP
        return (T) Proxy.newProxyInstance(Rpcfx.class.getClassLoader(), new Class[]{serviceClass}, new RpcfxInvocationHandler(serviceClass, url));

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
}
