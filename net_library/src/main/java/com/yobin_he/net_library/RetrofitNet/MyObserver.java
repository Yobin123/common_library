package com.yobin_he.net_library.RetrofitNet;


import com.yobin_he.net_library.BaseBean.BaseResponse;

import io.reactivex.disposables.Disposable;

/**
 * 观察者的封装，这样回调给presenter
 *
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
        onSuccess(response);
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
