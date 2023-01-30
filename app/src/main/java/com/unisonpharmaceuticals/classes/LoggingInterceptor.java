package com.unisonpharmaceuticals.classes;

import android.util.Log;
import com.google.gson.Gson;
import okhttp3.*;
import okio.Buffer;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.charset.Charset;

public class LoggingInterceptor implements Interceptor {
	@Override
	public Response intercept(Chain chain) throws IOException {
		Request request = chain.request();
		Buffer requestBuffer = new Buffer();
		request.body().writeTo(requestBuffer);
		Response response = chain.proceed(request);
		MediaType contentType = response.body().contentType();
		String content = response.body().string();

        Headers headers = response.headers();
        for (int i = 0; i < headers.size(); i++) {
            Log.d("??? ",String.format("--> Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
        }

		Log.e("ReqResp >>> ", "url > "+request.url()+"\n"+
		"request params > "+"\n"+
		"response > "+content);

		ResponseBody wrappedBody = ResponseBody.create(contentType, content);
		return response.newBuilder().body(wrappedBody).build();

		/*Request request = chain.request();

		long t1 = System.nanoTime();
		Log.d("OkHttp", String.format("--> Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));

		Buffer requestBuffer = new Buffer();
		request.body().writeTo(requestBuffer);
		Log.d("OkHttp", requestBuffer.readUtf8());

		Response response = chain.proceed(request);

		long t2 = System.nanoTime();
		Log.d("OkHttp", String.format("<-- Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));

		MediaType contentType = response.body().contentType();
		String content = response.body().string();
		Log.d("OkHttp", content);

		ResponseBody wrappedBody = ResponseBody.create(contentType, content);
		return response.newBuilder().body(wrappedBody).build();*/
	}
}