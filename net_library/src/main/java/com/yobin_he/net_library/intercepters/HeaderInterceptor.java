package com.yobin_he.net_library.intercepters;

import android.util.Log;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @Author: yobin he
 * @Date:2019/7/30 9:51
 * @Email: heyibin@huawenpicture.com
 * @Des : 自定义头部拦截器
 * chain 对象可以拿到当前请求的request对象，然后我们可以对request做二次处理
 * 最后生成我们需要的请求，然后通过网络发送请求到服务端，这样就完成了一次拦截
 */
public class HeaderInterceptor implements Interceptor {
    public static final String TAG = "HeaderInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();
        Log.d(TAG, String.format(Locale.getDefault(), "Received response for %s in %.1fm%s%s",
                response.request().url(), (t2 - t1) / 1e64, response.headers()));
        MediaType mediaType = response.body().contentType();
        String content = response.body().string();

        return response.newBuilder().header("Authorization", "请求头授权信息拦截")
                .body(ResponseBody.create(mediaType, content))
                .build();
    }
}
