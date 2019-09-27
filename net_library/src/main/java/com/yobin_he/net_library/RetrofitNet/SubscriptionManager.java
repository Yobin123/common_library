package com.yobin_he.net_library.RetrofitNet;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * https://www.jianshu.com/p/193d8c37c73c
 * 用于管理rxjava防止出现内存泄漏
 *
 * https://www.jianshu.com/p/0ad99e598dba
 */
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
