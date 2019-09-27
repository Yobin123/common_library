package com.yobin_he.net_library.intercepters;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author: yobin he
 * @Date:2019/8/27 14:09
 * @Email: heyibin@huawenpicture.com
 * @Des : 头部参数拦截器
 */
public class HeaderParamsInterceptor implements Interceptor {
    public static final String REQUEST_METHOD_GET = "GET";
    public static final String REQUEST_METHOD_POST = "POST";
    public static final String COMMON_REQUEST_KEY = "Authorization";
    public static final String COMMON_REQUEST_VALUE = "Bearer ";
    public static final String TEST_TOKEN = "";

    @Override
    public Response intercept(Chain chain) throws IOException { //同时也可以添加相应的公共参数，不限于header
        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();
        requestBuilder.addHeader(COMMON_REQUEST_KEY, TEST_TOKEN);

//        if (!"".equals(SharePreferenceUtil.getString(MyApplication.getInstance(), SharePreConstant.USER_TOKEN))) {
//            String token = SharePreferenceUtil.getString(MyApplication.getInstance(), SharePreConstant.USER_TOKEN);
//            requestBuilder.addHeader(COMMON_REQUEST_KEY, COMMON_REQUEST_VALUE + token);
//        }
        return chain.proceed(requestBuilder.build());
    }
}
