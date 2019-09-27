package com.example.mvp_without_activity_library.module;

/**
 * 回调给相应的presenter
 *
 * @param <T> 成功后的数据
 * @param <E> 失败后的数据
 */
public interface IModuleCallBack<T, E> {
    void onSuccess(T data); //成功的回调

    void onFailure(E error); //失败的回调

    void onError(Throwable t); //出错的回调
}
