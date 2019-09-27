package com.yobin_he.net_library.OkNet;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.yobin_he.net_library.intercepters.HeaderInterceptor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @Author: yobin he
 * @Date:2019/7/30 14:08
 * @Email: heyibin@huawenpicture.com
 * @Des : okhttp管理器
 * @source: https://blog.csdn.net/ytfunnysite/article/details/81002120
 * 该管理类回调给页面的是一个json串，所以在接收页面或者处理页面还需要将该json转换为自己的model，
 * 可以用泛型接收回调，当然前提是项目中baseResponse中添加泛型变量。
 */
public class OkManager {
    private static OkHttpClient okHttpClient;
    private volatile static OkManager instance; // 防止多个线程同时访问
    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");//提交json数据
    private static final MediaType media_type_markdown = MediaType.parse("text/x-markdown;charset=utf-8");//提交字符串
    private static String responseStrAsyn;

    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
    //使用getCacheDir()来作为缓存文件的存放路径（data/data/包名/cache）
    //如果想看到缓存文件可以临时使用getExternalCacheDir()(/sdcard/Android/data/包名/cache)
    private static File cacheFile;
    private static Cache cache;

    public OkManager() {
//        if(null != MyApplication.getInstance().getApplicationContext().getCacheDir()){
//            cacheFile = new File(MyApplication.getInstance().getApplicationContext().getCacheDir(),"Test");
//            cache = new Cache(cacheFile,1024 * 1024 * 10);
//        }

        okHttpClient = new OkHttpClient();
        okHttpClient.newBuilder().addInterceptor(new HeaderInterceptor())
                //如果需要添加缓存可以自己定义缓存，由于在该缓存拦截器中需要上下文对象，故将该类全部注释掉了，如果有需要，可以自行解开（interceptor文件夹）
//                .addNetworkInterceptor(new CacheInterceptor()) 
                .cache(cache)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(@NonNull HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(url.host(), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(url.host());
                        return cookies != null ? cookies : new ArrayList<Cookie>();
                        //自动管理cookie发送的request不用管cookie这个参数，不用去response获取新的cookie，
                    }
                });
    }

    public static OkManager getInstance() {
        if (instance == null) {
            synchronized (OkManager.class) {
                if (instance == null) {
                    instance = new OkManager();
                }
            }
        }
        return instance;
    }

    ///////////////////////////////////////////////////////////////////////////
    // 方法区 start
    /////////////////////////////////////////////////////////////////////////// 

    /*********************************************同步get**********************************************************/

    /**
     * get同步请求，并不需要传递参数
     *
     * @param url
     * @return 返回数据
     */
    public String getSyncBackString(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            String responseStr = response.body().string();
            return responseStr;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get同步请求
     *
     * @param url
     * @return 返回二进制字节数组
     */
    public byte[] getSynBackArray(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);

        try {
            Response response = call.execute();
            byte[] responseStr = response.body().bytes();
            return responseStr;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get 同步获取二进制字节流
     *
     * @param url
     * @return
     */
    public InputStream getSynBackStream(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            InputStream responseStr = response.body().byteStream();
            return responseStr;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * get同步请求
     *
     * @param url
     * @return 获取返回的二进制字节流
     */
    public Reader getSynCharReader(String url) {

        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            Reader responseStr = response.body().charStream();
            return responseStr;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /***************************************************异步get***************************************************************/

    /**
     * get 异步请求 不添加参数
     *
     * @param url
     * @param responseCallBack
     */
    public void getAsynBackStringWithoutParams(String url, final ResponseCallBack responseCallBack) {
        getAsynBackString(url, null, responseCallBack);
    }

    /**
     * get 异步请求 添加参数
     *
     * @param url
     * @param callBack
     */
    public void getAsynBackString(String url, Map<String, String> params, final ResponseCallBack callBack) {
        final Request.Builder builder = new Request.Builder();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                urlBuilder.addQueryParameter(key, null == params.get(key) ? "" : params.get(key));
            }

        }
        builder.url(urlBuilder.build());
        Call call = okHttpClient.newCall(builder.build());


        callBack.onStart();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onError(call, e);
                callBack.onFinish();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callBack.onSuccess(response.body().string());
                    callBack.onFinish();
                } else {
                    callBack.onFailure(response.code(), response.message());
                    callBack.onFinish();
                }
            }
        });

    }

    /*****************************************************post异步请求***************************************************************/
    /**
     * post 异步 传参数
     *
     * @param url      地址
     * @param params   参数
     * @param callBack 回调参数
     */
    public void postAsynBackString(String url, Map<String, String> params, final ResponseCallBack callBack) {
        RequestBody requestBody;
        if (params == null) {
            params = new HashMap<>();
        }
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                builder.add(key, null == params.get(key) ? "" : params.get(key));
            }
        }
        requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        callBack.onStart();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onError(call, e);
                callBack.onFinish();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callBack.onSuccess(response.body().string());
                    callBack.onFinish();
                } else {
                    callBack.onFailure(response.code(), response.message());
                    callBack.onFinish();
                }
            }
        });
    }

    /**
     * post 异步传json
     *
     * @param url
     * @param params
     * @param callBack
     * @return
     */
    public void postAsynRequireJson(String url, Map<String, String> params, final ResponseCallBack callBack) {
        if (params == null) {
            params = new HashMap<>();
        }

        String mapTojson = new Gson().toJson(params);
        RequestBody body = RequestBody.create(JSON, mapTojson);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onError(call, e);
                callBack.onFinish();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callBack.onSuccess(response.body().string());
                    callBack.onFinish();
                } else {
                    callBack.onFailure(response.code(), response.message());
                    callBack.onFinish();
                }
            }
        });
    }

    /****************************************文件上传************************************************************/
    /**
     * 不带参数的文件上传
     *
     * @param url
     * @param file
     * @param fileKey
     * @param callBack
     */
    private void uploadFile(String url, @NonNull File file, @NonNull String fileKey, final ResponseCallBack callBack) {
        uploadFileWithParam(url, file, fileKey, null, callBack);
    }

    /**
     * 带参数的单文件上传
     *
     * @param url
     * @param file
     * @param fileKey
     * @param params
     * @param callBack
     */
    private void uploadFileWithParam(String url, @NonNull File file, @NonNull String fileKey, Map<String, String> params, final ResponseCallBack callBack) {
        File[] files = new File[]{file};
        String[] fileKeys = new String[]{fileKey};
        uploadFiles(url, files, fileKeys, params, callBack);
    }

    /**
     * 带参数的多文件上传
     *
     * @param url
     * @param files
     * @param fileKeys
     * @param params
     * @param callBack
     */
    private void uploadFiles(String url, @NonNull File[] files, @NonNull String[] fileKeys, Map<String, String> params, final ResponseCallBack callBack) {
        if (params == null) {
            params = new HashMap<>();
        }

        MultipartBody.Builder multipartBody = new MultipartBody.Builder();

        //添加参数
        if (params != null && params.size() > 0) {
            FormBody.Builder builder = new FormBody.Builder();
            for (String key : params.keySet()) {
                builder.add(key, null == params.get(key) ? "" : params.get(key));
            }
            RequestBody requestBody = builder.build();
            multipartBody.setType(MultipartBody.ALTERNATIVE)
                    .addPart(requestBody);
        }


        //添加文件
        if (files != null) {
            RequestBody fileBody = null;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(gussMimeType(fileName)), file);
                multipartBody.addPart(Headers.of("Content-Disposition", "form-data;" +
                        "name = \"" + fileKeys[i] + "\";filename = \"" + fileName + "\""), fileBody);
            }
        }

        final Request request = new Request.Builder()
                .url(url)
                .post(multipartBody.build())
                .build();

        Call call = okHttpClient.newCall(request);
        callBack.onStart();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onError(call, e);
                callBack.onFinish();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callBack.onSuccess(response.body().string());
                    callBack.onFinish();
                } else {
                    callBack.onFailure(response.code(), response.message());
                    callBack.onFinish();
                }
            }
        });

    }

    private String gussMimeType(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(fileName);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }


    /**
     * 下载文件 ，此时应该注意相应code码，不一定response.isSuccessful之后整个网络就可以传输数据的。
     *
     * @param url
     * @param destFileDir
     * @param callBack
     */
    private void downLoadFileAsyn(final String url, final String destFileDir, final ResponseCallBack callBack) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onError(call, e);
                callBack.onFinish();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException { //这里应该具体情况分析 如判断code码是否是正确的，需要根据项目不同去处理
                if (response.isSuccessful()) {
                    InputStream is = null;
                    byte[] buff = new byte[2048];
                    int len = 0;
                    FileOutputStream fos = null;
                    is = response.body().byteStream();
                    int separatorIndex = url.lastIndexOf("/");
                    File file = new File(destFileDir, (separatorIndex < 0) ? url : url.substring(separatorIndex + 1, url.length()));
                    try {
                        fos = new FileOutputStream(file);
                        while ((len = is.read(buff)) != -1) {
                            fos.write(buff, 0, len);
                        }
                        fos.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                    callBack.onSuccess(file.getAbsolutePath());
                } else {
                    callBack.onFailure(response.code(), response.message());
                    callBack.onFinish();
                }


            }
        });
    }


    ///////////////////////////////////////////////////////////////////////////
    // 方法区 end
    ///////////////////////////////////////////////////////////////////////////


    public interface ResponseCallBack {
        void onFailure(int code, String msg);

        void onSuccess(String data);

        void onError(Call call, IOException e);

        void onStart(); //开始

        void onFinish(); //完成
    }

}
