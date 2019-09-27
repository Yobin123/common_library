package com.yobin_he.net_library.RetrofitNet;

import com.yobin_he.net_library.NetConstance;
import com.yobin_he.net_library.intercepters.HeaderParamsInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit管理类
 */
public class RetrofitManager {
    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");//提交json数据
    private static final String CACHE_PATH = "httpCache";
    public static final long CACHE_SIZE = 1024 * 1024 * 100;
    public static final int TIMEOUT = 15; //超时时间
    private static volatile RetrofitManager mInstance;
    private Retrofit mRetrofit;
    private OkHttpClient mOkhttpClient;

    /**
     * 单例化
     *
     * @return
     */
    public static RetrofitManager newInstance() {
        if (mInstance == null) {
            synchronized (RetrofitManager.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitManager();
                }
            }
        }
        return mInstance;
    }


    /**
     * 初始化相关
     */
    private RetrofitManager() {
        initOkhttp();//初始化okhttp 
        initRetrofit(); //初始retrofit
    }

    /**
     * 初始化retrofit
     */
    private void initRetrofit() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(NetConstance.IS_DEBUG ? NetConstance.DEBUG_BASE_URL : NetConstance.RELEASE_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(ResponseConvert.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mOkhttpClient)
                .build();
    }

    /**
     * 初始化okhttp
     *
     * @return
     */
    private void initOkhttp() {
        // TODO: 2019/6/23  可以添加统一参数拦截器
        HeaderParamsInterceptor commonParamsInterceptor = new HeaderParamsInterceptor();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //关联相应的拦截器
        interceptor.setLevel(NetConstance.IS_DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        //同时可以添加统一参数
        if (mOkhttpClient == null) {
            synchronized ((RetrofitManager.class)) {
                if (mOkhttpClient == null) {
                    //缓存，需要用到上下文
//                    Cache cache = new Cache(new File(MyApplication.getInstance().getCacheDir(), CACHE_PATH), CACHE_SIZE);
                    mOkhttpClient = new OkHttpClient.Builder()
//                            .cache(cache)
                            .addInterceptor(interceptor)
                            .addInterceptor(commonParamsInterceptor)
//                            .retryOnConnectionFailure(true)
                            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }


    /**
     * 用于创建相关类
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> clazz) {
        if (mRetrofit != null) {
            return mRetrofit.create(clazz);
        }
        return null;
    }


}
