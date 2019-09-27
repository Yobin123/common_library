package com.yobin_he.packagedemo.mvp.views;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mvp_base_library.view.base.BaseActivity;
import com.yobin_he.packagedemo.R;
import com.yobin_he.packagedemo.beans.GankBean;
import com.yobin_he.packagedemo.mvp.contracts.WelfareContract;
import com.yobin_he.packagedemo.mvp.presenters.WalefarePresenterImpl;

import java.util.List;

public class MainActivity extends BaseActivity<WelfareContract.IWelfarePresenter> implements WelfareContract.IWelfareView {
    private TextView tv;


    @Override
    protected WelfareContract.IWelfarePresenter bindPresenter() {
        return new WalefarePresenterImpl(this);
    }

    @Override
    protected int onLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        tv = fv(R.id.tv);
    }

    @Override
    protected void addListener() {

    }

    @Override
    protected void setControl() {

    }

    public void onclickView(View view) {
        mvpPre.requestWelfale(10, 1);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onNetStart() {

    }

    @Override
    public void onNetFinish() {

    }

    @Override
    public void onNetFail(int code, String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNetSuccess(List<GankBean> beans) {
        tv.setText(beans.toString());
    }
}
