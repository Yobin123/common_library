package com.example.mvp_without_activity_library.view.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.example.mvp_without_activity_library.utils.ActivityCollector;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 普通Activity继承该基础类，这样就不用修改已有项目基类
 */
public abstract class BaseActivity extends FragmentActivity implements View.OnClickListener {
    Unbinder unbinder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this); //收集相应的activity
        if (0 != onLayout()) {
            setContentView(onLayout());
            unbinder = ButterKnife.bind(this);
            initView();
            addListener();
            setControl();
        }

    }

   
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(unbinder != null){
            unbinder.unbind();
        }
        ActivityCollector.removeActivity(this);
    }

    ///////////////////////////////////////////////////////////////////////////
    // 抽象方法 start
    ///////////////////////////////////////////////////////////////////////////

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
