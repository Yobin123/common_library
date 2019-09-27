package com.yobin_he.net_library.RetrofitNet;

import io.reactivex.disposables.Disposable;

/**
 * 订阅监听器帮助类
 * @param <T>
 */
public interface SubscriptionHelper<T> {
    void add(Disposable disposable); //添加
    void cancel(Disposable t); //取消
    void cancelAll(); //取消所有订阅
}
