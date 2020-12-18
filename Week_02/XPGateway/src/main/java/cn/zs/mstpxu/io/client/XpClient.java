package cn.zs.mstpxu.io.client;

import java.io.IOException;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;

import cn.zs.mstpxu.io.common.HTTPRequestType;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class XpClient {
	private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
	private static final OkHttpClient client = new OkHttpClient();
	private static final HTTPRequestType DEFAULT_HTTPRequestType = HTTPRequestType.GET;
	
	private HTTPRequestType requestType = null;
	private String requestURL;
	@Nullable
	private String jsonBody;
	
	public XpClient() { this.requestType = DEFAULT_HTTPRequestType; }
	public XpClient(HTTPRequestType type) { this.requestType = type; }	
	public XpClient(String requestURL) {
		this();
		this.requestURL = requestURL;
	}
	public XpClient(HTTPRequestType type,String requestURL) {
		this.requestType = type;
		this.requestURL  = requestURL;
	}
	public XpClient(HTTPRequestType type,String requestURL,String jsonBody) {
		this.jsonBody     = jsonBody;
		this.requestType  = type;
		this.requestURL   = requestURL;
	}
	
	public static void main(String[] args) throws IOException {
		String url = "http://localhost:8808/test";
//		String url = "http://tianqiapi.com/api?version=v6&appid=56733817&appsecret=hQbbjF3q&cityid=101010100";
		System.out.print(get(url));
	}
	
	static String post(String url, String json) throws IOException {
		RequestBody body = RequestBody.create(json, JSON);
		Request request = new Request.Builder()
							.url(url)
							.post(body)
							.build();
		try (Response response = client.newCall(request).execute()) {
			return response.body().string();
		}
	}
	
	static String get(String url) throws IOException {
		Request request = new Request.Builder()
							.url(url)
							.build();
		try (Response response = client.newCall(request).execute()) {
			return response.body().string() + "HEADER:" +response.headers().toString();
		}
	}

}
