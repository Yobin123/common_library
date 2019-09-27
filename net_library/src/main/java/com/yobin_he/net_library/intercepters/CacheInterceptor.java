package com.yobin_he.net_library.intercepters;

/**
 * @Author: yobin he
 * @Date:2019/7/30 10:54
 * @Email: heyibin@huawenpicture.com
 * @Des : 设置网络拦截器，实现在线和离线缓存
 * <p>
 * 判断网络是否可用
 * @return 判断网络是否可用
 * @return 判断网络是否可用
 * @return
 */
//public class CacheInterceptor implements Interceptor {
//    @Override
//    public Response intercept(Chain chain) throws IOException {
//        Request request = chain.request();
//        if (!isNetworkConnected()) {
//            request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE)
//                    .build();
//        }
//        Response originalResponse = chain.proceed(request);
//        if (isNetworkConnected()) {
//            //有网络时候读接口上的header里的配置，你可以在这里进行统一的设置（注掉部分）
//            String cacheControl = request.cacheControl().toString();
//            return originalResponse.newBuilder().header("Cache-Control", cacheControl)
////                    .header("Cache-control","max-age=3600")
//                    .removeHeader("Pragma") //清除头部信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
//                    .build();
//        } else {
//            //1.不需要缓存：Cache-Control: no-cache或Cache-Control: max-age=0
//            //2.如果想先显示数据，在请求。（类似于微博等）：Cache-Control: only-if-cached
//            int maxAge = 60 * 60;
//            return originalResponse.newBuilder().header("Cache-Control", "public,only-if-cached,max-age = " + maxAge)
//                    .removeHeader("Pragma")
//                    .build();
//        }

//    }

/**
 * 判断网络是否可用
 *
 * @return
 */
//    private boolean isNetworkConnected() {
//        ConnectivityManager manager = (ConnectivityManager) MyApplication.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (null != manager) {
//            NetworkInfo info = manager.getActiveNetworkInfo();
//            if (null != info && info.isConnected()) {
//                if (info.getState() == NetworkInfo.State.CONNECTED) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//}
