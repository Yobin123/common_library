package com.example.mvp_without_activity_library.presenter;

/**
 * 每个在契约类中presenter都必须实现该接口。
 */
public interface IPresenter {
    void detachView(); //用于分离视图
}
