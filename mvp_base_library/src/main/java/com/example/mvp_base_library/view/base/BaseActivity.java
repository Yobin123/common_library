package com.example.mvp_base_library.view.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.example.mvp_base_library.presenter.IPresenter;
import com.example.mvp_base_library.utils.ActivityCollector;
import com.example.mvp_base_library.view.IView;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 所有的Activity应该继承该基础类
 *
 * @param <P>
 */
public abstract class BaseActivity<P extends IPresenter> extends FragmentActivity implements IView, View.OnClickListener {
    protected P mvpPre;
    Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvpPre = bindPresenter();
        ActivityCollector.addActivity(this); //收集相应的activity
        if (0 != onLayout()) {
            setContentView(onLayout());
            unbinder =  ButterKnife.bind(this);
            initView();
            addListener();
            setControl();
        }

    }

    @Override
    public Activity getSelfActivity() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * 在生命周期结束的时候，将presenter与view之间的联系断开，防止内存泄漏
         */
        if (mvpPre != null) {
            mvpPre.detachView();
        }
        if(unbinder != null){
            unbinder.unbind();
        }
        ActivityCollector.removeActivity(this);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 抽象方法 start
    /////////////////////////////////////////////////////////////////////////// 
    protected abstract P bindPresenter();

    protected abstract int onLayout(); //设置布局id;

    protected abstract void initView(); //初始化控件

    protected abstract void addListener(); //添加监听器

    protected abstract void setControl(); // 添加主要逻辑
    // /////////////////////////////////////////////////////////////////////////
    // 抽象方法 start
    ///////////////////////////////////////////////////////////////////////////


    //进行控件的查找
    public <T> T fv(int resId) {
        return (T) findViewById(resId);
    }

    public <T> T fv(int resId, View parent) {
        return (T) parent.findViewById(resId);
    }
}
