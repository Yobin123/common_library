### 前言 基于以下参考进行改良封装
### 一、接入前准备，将下面的依赖导入工程里面

```
    implementation 'io.reactivex.rxjava2:rxjava:2.1.0'
    // 必要rxjava2依赖
    implementation 'io.reactivex.rxjava2:rxandroid:2.0.1'
    // 必要rxandrroid依赖，切线程时需要用到
    implementation 'com.squareup.retrofit2:retrofit:2.3.0'
    // 必要retrofit依赖
    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
    // 必要依赖，和Rxjava结合必须用到，
    implementation 'com.squareup.retrofit2:converter-gson:2.3.0'
    // 必要依赖，解析json字符所用
    
    //非必要依赖， log依赖，如果需要打印OkHttpLog需要添加
    implementation 'com.squareup.okhttp3:logging-interceptor:3.8.1'
    //  gson转换工厂
    implementation 'com.google.code.gson:gson:2.8.5'
```
### 二、Retrofit使用讲解（网上许多参考故略）
### 三、工程结构图
![结构图](https://upload-images.jianshu.io/upload_images/2422869-2c9ca7c60e752e4f.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
### 四、封装流程
#### 1. 进行Retrofit的封装 
* 单例化Retrofit管理类
* 初始化okhttp
* 初始化retrofit

```
  private static final String CACHE_PATH = "httpCache";
    public static final long CACHE_SIZE = 1024 * 1024 * 100;
    public static final int TIMEOUT = 15; //超时时间
    private static volatile RetrofitManager mInstance;
    private Retrofit mRetrofit; //retorfit对象
    private OkHttpClient mOkhttpClient; //okhttp对象
    private ServerApi api; //生成接口类

   //单利化
    public static ServerApi getServerApi() {
        if (mInstance == null) {
            synchronized (RetrofitManager.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitManager();
                }
            }
        }
        return mInstance.api;
    }


    /**
     * 构造器中初始化okhttp和retrofit
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
                .baseUrl(CommonConstant.BASE_URL)
                .addConverterFactory(CustomGsonFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(mOkhttpClient)
                .build();
        api = mRetrofit.create(ServerApi.class);
    }

    /**
     * 初始化okhttp
     *
     * @return
     */
    private void initOkhttp() {
        //头部参数拦截器
        HeaderParamsInterceptor commonParamsInterceptor = new HeaderParamsInterceptor();
        //日志参数拦截器
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //关联相应的拦截器
        interceptor.setLevel(CommonConstant.IS_DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        
        if (mOkhttpClient == null) {
            synchronized ((RetrofitManager.class)) {
                if (mOkhttpClient == null) {
                //增加缓存
                    Cache cache = new Cache(new File(MyApplication.getInstance().getCacheDir(), CACHE_PATH), CACHE_SIZE);
                    mOkhttpClient = new OkHttpClient.Builder()
                            .cache(cache)
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

```

#### 2.根据要求封装请求基类，响应基类，以及错误基类
* 请求基类，由于我们请求会将请求参数放在请求体中，然后通过json数据传给服务器

```
public class BaseRequest<T> {
    
    private T options;
    
    public T getOptions() {
        return options;
    }

    public void setOptions(T options) {
        this.options = options;
    }
}
```
* 响应基类，一般情况会有msg ,code,data这三个字段

```
public class BaseResponse<T> {
    @SerializedName(value = "code", alternate = "status")
    private int code; //返回码
    private T data; //具体数据结果
    private String msg; //返回信息
    
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
```
* 错误基类，可以根据实际情况来，我们接口的请求放在errorbody中，故可以这样处理，但是如果放在response中通过这就需要灵活处理这个数据，防止类造型错误。

```
public class FailureBody {
    private List data;
    @SerializedName(value = "code",alternate = "status")
    private int code;
    private String msg;
    

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
```
#### 3.Observable的生成
* 通过ObjectNetLoader这个类，简化相应的线程切换
* 通过继承的NetDataLoader这个类生成相应的Observable对象

```
public class ObjectLoader {
    /**
     * 用于封装相关
     * @param observable
     * @param <T>
     * @return
     */
    protected   <T> Observable<T> observer(Observable<T> observable){
        return observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}


/**
 * 用于对数据提前处理，同时也可用来对参数处理
 * 例如合并，map ,zip或者flateMap这样可以将不符合自己参数的数据转换为自己想请求的参数
 * 这一层是属于module内，用于处理数据源，供给presenter作为中间纽带进行调用
 */
public class NetDataLoader extends ObjectLoader {
    private ServerApi api;
    private NetDataLoader() {
        api = RetrofitManager.getServerApi();
    }
    public static NetDataLoader loader;
    public static NetDataLoader get() {
        if (loader == null) {
            synchronized (NetDataLoader.class) {
                if (loader == null) {
                    loader = new NetDataLoader();
                }
            }
        }
        return loader;
    }
    /*******************************请求数据*****************************************/

    /************************************用户**************************************/
    //用用户信息登录
    public Observable<BaseResponse<UserBean>> postUserLogin(ReqUserLogin bean) {
        return observer(api.postUserLogin(getRequestBody(bean))); //请求参数的初步处理
    }
    
    /***********************请求数据modle类*************************************/
    //用于对请求数据model修改为json数据
    private <T> BaseRequest getRequestBody(T data) {
        if (data == null)
            return null;
        BaseRequest<T> requestBody = new BaseRequest<>();
        requestBody.setOptions(data);
        return requestBody;
    }

}

```
* observer的生成，自定义Myobserver，既可以作为回调（供MVP中module回调接口），也可以作为单独使用。

```
/**
 * @param <T>
 */
public abstract class MyObserver<T> implements io.reactivex.Observer<BaseResponse<T>> {
    
    private Disposable d;

    @Override
    public void onSubscribe(Disposable d) {
        SubscriptionManager.newStance().add(d);//提前添加相应的Disposable
        this.d = d;
        onDisposable(d);
    }

    @Override
    public void onNext(BaseResponse<T> response) {
        if (response.getCode() == 200) {//通过200说明请求成功
            onSuccess(response);
        }
        SubscriptionManager.newStance().cancel(d);
    }

    @Override
    public void onError(Throwable e) {
        onFail(ExceptionHandle.handleException(e));
        SubscriptionManager.newStance().cancel(d);

    }

    @Override
    public void onComplete() {
        onCompleted();
        SubscriptionManager.newStance().cancel(d);

    }

    public abstract void onSuccess(BaseResponse<T> t);

    public abstract void onFail(ExceptionHandle.ResponseThrowable throwable);

    public abstract void onCompleted();

    public abstract void onDisposable(Disposable d);

}
```

#### 4.Rxjava相关管理，防止发生内存泄漏
* 用于管理Rxjava，及时的取消订阅，防止出现泄漏等情况（包括了Helper,以及管理类）

```
/**
 * 订阅监听器帮助类
 * @param <T>
 */
public interface SubscriptionHelper<T> {
    void add(Disposable disposable); //添加
    void cancel(Disposable t); //取消
    void cancelAll(); //取消所有订阅
}

public class SubscriptionManager implements SubscriptionHelper<Object> {
    public CompositeDisposable mDisposables;
    private static SubscriptionManager manager;

    private SubscriptionManager(){
        if(mDisposables == null){
            mDisposables = new CompositeDisposable();
        }
    }

    public static SubscriptionManager newStance(){
        if(manager == null){
            synchronized (SubscriptionManager.class){
                if(manager == null){
                    manager = new SubscriptionManager();
                }
            }
        }
        return manager;
    }


    @Override
    public void add(Disposable disposable) {
        if(disposable == null) return;
        mDisposables.add(disposable);
    }

    @Override
    public void cancel(Disposable t) {
        if(null == t) return;
        mDisposables.delete(t);
    }

    @Override
    public void cancelAll() {
        if(mDisposables != null && mDisposables.size() > 0){
            if(!mDisposables.isDisposed())
                mDisposables.dispose();
                mDisposables.clear();
        }
    }
}

```

#### 5.异常处理类
* 用于对错误和异常的同意处理，通过在自定义observer中onError的回调中throwable的进行相关处理，从而统一回调给onFailure方法中，

```
public class ExceptionHandle {
    private static final int UNAUTHORIZED = 401; //用户信息无效
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;
    private static final int FAIL_QUEST = 406;//无法使用请求的内容特性来响应请求的网页
    private static final int BAD_REQUEST = 400;
    private static ResponseBody body;

    public static ResponseThrowable handleException(Throwable e) {
        ResponseThrowable ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResponseThrowable(e, ERROR.HTTP_ERROR);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                    body = ((HttpException) e).response().errorBody();
                    try {
                        String message = body.string();
                        FailureBody body = GsonUtil.GsonToBean(message, FailureBody.class);
                        ex.code = body.getCode();
                        ex.message = body.getMsg();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    break;
                case FORBIDDEN:
                    ex.message = "服务器已经理解请求，但是拒绝执行它";
                    break;
                case NOT_FOUND:
                    body = ((HttpException) e).response().errorBody();
                    try {
                        String message = body.string();
                        FailureBody body = GsonUtil.GsonToBean(message, FailureBody.class);
                        ex.code = body.getCode();
                        ex.message = body.getMsg();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    break;
                case REQUEST_TIMEOUT:
                    ex.message = "请求超时";
                    break;
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                    ex.message = "服务器遇到了一个未曾预料的状况，无法完成对请求的处理";
                    break;
                case BAD_REQUEST:
                    body = ((HttpException) e).response().errorBody();
                    try {
                        String message = body.string();
                        FailureBody failureBody = GsonUtil.GsonToBean(message, FailureBody.class);
                        ex.message = failureBody.getMsg();
                        ex.code = failureBody.getCode();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    break;
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                case FAIL_QUEST:
                    body = ((HttpException) e).response().errorBody();
                    try { //用于处理相关信息
                        String message = body.string();
                        Gson gson = new Gson();
//                        ErrorBodyDTO globalExceptionDTO = gson.fromJson(message, ErrorBodyDTO.class);
//                        if (globalExceptionDTO.getErrMsg() != null) {
//                            ex.message = globalExceptionDTO.getErrMsg();
//                        } else {
//                            ex.message = "";
//                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    break;
                default:
                    ex.message = "网络错误";
                    break;
            }
            return ex;
        } else if (e instanceof ServerException) {
            ServerException resultException = (ServerException) e;
            ex = new ResponseThrowable(resultException, resultException.code);
            ex.message = resultException.message;
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            ex = new ResponseThrowable(e, ERROR.PARSE_ERROR);
            ex.message = "解析错误";
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ResponseThrowable(e, ERROR.NETWORD_ERROR);
            ex.message = "连接失败";
            return ex;
        } else if (e instanceof javax.net.ssl.SSLHandshakeException) {
            ex = new ResponseThrowable(e, ERROR.SSL_ERROR);
            ex.message = "证书验证失败";
            return ex;
        } else if (e instanceof java.net.SocketTimeoutException) {
            ex = new ResponseThrowable(e, ERROR.TIMEOUT_ERROR);
            //ex.message = "连接超时";
            ex.message = "当前网络连接不顺畅，请稍后再试！";
            return ex;
        } else if (e instanceof java.net.UnknownHostException) {
            ex = new ResponseThrowable(e, ERROR.TIMEOUT_ERROR);
            ex.message = "网络中断，请检查网络状态！";
            return ex;
        } else if (e instanceof javax.net.ssl.SSLException) {
            ex = new ResponseThrowable(e, ERROR.TIMEOUT_ERROR);
            ex.message = "网络中断，请检查网络状态！";
            return ex;
        } else if (e instanceof java.io.EOFException) {
            ex = new ResponseThrowable(e, ERROR.PARSE_EmptyERROR);
            ex.message = "1007";
            return ex;
        } else if (e instanceof java.lang.NullPointerException) {
            ex = new ResponseThrowable(e, ERROR.PARSE_EmptyERROR);
            ex.message = "数据为空，显示失败";
            return ex;
        } else {
            ex = new ResponseThrowable(e, ERROR.UNKNOWN);
            ex.message = "未知错误";
            return ex;
        }
    }


    /**
     * 约定异常
     */
    public class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 解析no content错误
         */
        public static final int PARSE_EmptyERROR = 1007;
        /**
         * 网络错误
         */
        public static final int NETWORD_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;

        /**
         * 连接超时
         */
        public static final int TIMEOUT_ERROR = 1006;

        public static final int LOGIN_ERROR = -1000;
        public static final int DATA_EMPTY = -2000;


    }

    public static class ResponseThrowable extends Exception {
        public int code;
        public String message;

        public ResponseThrowable(Throwable throwable, int code) {
            super(throwable);
            this.code = code;
        }

        public ResponseThrowable(String message, int code) {
            this.code = code;
            this.message = message;
        }
    }

    public class ServerException extends RuntimeException {
        public int code;
        public String message;

        public ServerException(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}
```
#### 5.使用方法

```
 NetDataLoader.get().postUserLogin(request).subscribe(new MyObserver<UserBean>() {
            @Override
            public void onSuccess(BaseResponse<UserBean> t) {
                
            }

            @Override
            public void onFail(ExceptionHandle.ResponseThrowable throwable) {

            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onDisposable(Disposable d) {

            }
        });
```



### 五、[git传送门](https://github.com/Yobin123/base_mvp)

### 六、参考 
[1.Rxjava2+Retrofit2+MVP的网络请求封装](https://www.jianshu.com/p/193d8c37c73c)

[2.Android版&Kotlin版RxJava2+Retrofit2+OkHttp3的基础、封装和项目中的使用](https://www.jianshu.com/p/0ad99e598dba)
