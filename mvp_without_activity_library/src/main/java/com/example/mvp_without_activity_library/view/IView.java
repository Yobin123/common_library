package com.example.mvp_without_activity_library.view;

import android.app.Activity;

/**
 * 所有的在契约类中view都必须实现该接口(Activity,Fragment,fragmentActivity)
 */
public interface IView {
    /**
     * 这个方法是为了当presenter中需要获取上下文对象时，传递上下文对象，而不是让presenter直接持有上下文对象
     *
     * @return
     */
    Activity getSelfActivity();
}
