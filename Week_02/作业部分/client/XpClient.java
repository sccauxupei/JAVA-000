package cn.zs.mstpxu.io.client;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class XpClient {

	private static final MediaType JSON
	= MediaType.get("application/json; charset=utf-8");
	
	private static OkHttpClient client = new OkHttpClient();

	public static void main(String[] args) throws IOException {
		String url = "http://localhost:8808/test";
		  System.out.print(run(url));
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
	
	static String run(String url) throws IOException {
		  Request request = new Request.Builder()
		      .url(url)
		      .build();

		  try (Response response = client.newCall(request).execute()) {
		    return response.body().string();
		  }
		}

}
