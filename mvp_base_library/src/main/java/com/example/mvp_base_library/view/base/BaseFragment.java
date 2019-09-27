package com.example.mvp_base_library.view.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mvp_base_library.presenter.IPresenter;
import com.example.mvp_base_library.view.IView;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * 所有的fragment应该继承该基础类
 *
 * @param <P>
 */
public abstract class BaseFragment<P extends IPresenter> extends Fragment implements IView, View.OnClickListener {
    protected P mvpPre;
    Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mvpPre = bindPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(onLayout(), container, false);
        unbinder = ButterKnife.bind(this,view);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addListener();
        setControl();
    }

    @Override
    public Activity getSelfActivity() {
        return getActivity();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(unbinder != null)
            unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /**
         * 在生命周期结束的时候，将presenter与view之间的联系断开，防止内存泄漏
         */
        if (mvpPre != null) {
            mvpPre.detachView();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 抽象方法 start
    /////////////////////////////////////////////////////////////////////////// 
    protected abstract P bindPresenter();

    protected abstract int onLayout(); //设置布局id;

    protected abstract void initView(View view); //初始化控件

    protected abstract void addListener(); //添加监听器

    protected abstract void setControl(); // 添加主要逻辑
    // /////////////////////////////////////////////////////////////////////////
    // 抽象方法 start
    ///////////////////////////////////////////////////////////////////////////

    //带有父控件查找
    public <T> T fv(int resId, View parent) {
        return (T) parent.findViewById(resId);
    }
}
