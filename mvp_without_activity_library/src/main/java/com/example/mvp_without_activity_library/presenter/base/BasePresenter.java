package com.example.mvp_without_activity_library.presenter.base;




import com.example.mvp_without_activity_library.presenter.IPresenter;
import com.example.mvp_without_activity_library.view.IView;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * 基础类的presenter
 *
 * @param <V>
 */
public class BasePresenter<V extends IView> implements IPresenter {
    protected Reference<V> mvpRef;

    @Override
    public void detachView() {
        if (null != mvpRef) {
            mvpRef.clear();
            mvpRef = null;
        }
    }

    public BasePresenter(V view) {
        attachView(view); //绑定相应视图。
    }

    /**
     * 进行View的绑定
     *
     * @param view
     */
    private void attachView(V view) {//进行弱引用的绑定
        mvpRef = new WeakReference<V>(view);
    }

    /**
     * 获取相应的View
     *
     * @return
     */
    protected V getView() {
        if (null != mvpRef) {
            return mvpRef.get();
        }
        return null;
    }

    /**
     * 主要用于判断Iveiw的生命周期是否结束，防止出现内存泄漏
     *
     * @return
     */
    protected boolean isViewAttach() {
        return null != mvpRef && null != mvpRef.get();
    }


}
