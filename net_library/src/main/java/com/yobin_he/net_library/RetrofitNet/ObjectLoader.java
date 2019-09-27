package com.yobin_he.net_library.RetrofitNet;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
