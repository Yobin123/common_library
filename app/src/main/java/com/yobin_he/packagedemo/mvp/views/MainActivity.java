package com.yobin_he.packagedemo.mvp.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mvp_base_library.view.base.BaseActivity;
import com.yobin_he.image_preview_library.PreviewActivity;
import com.yobin_he.image_preview_library.entry.Image;
import com.yobin_he.packagedemo.R;
import com.yobin_he.packagedemo.beans.GankBean;
import com.yobin_he.packagedemo.mvp.contracts.WelfareContract;
import com.yobin_he.packagedemo.mvp.presenters.WalefarePresenterImpl;
import com.yobin_he.packagedemo.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<WelfareContract.IWelfarePresenter> implements WelfareContract.IWelfareView {
    private ImageView imageView;
    private List<GankBean> list = new ArrayList<>();


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
        imageView = fv(R.id.iv);
    }

    @Override
    protected void addListener() {
        imageView.setOnClickListener(this);
    }

    @Override
    protected void setControl() {

    }

    public void onclickView(View view) {
        mvpPre.requestWelfale(10, 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv:
                ArrayList<Image> arrayList = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    Image image = new Image(list.get(i).getUrl(),
                            DateUtil.dateToLongStamp(list.get(i).getDesc(), false),
                            list.get(i).getWho(), list.get(i).getType());
                    arrayList.add(image);
                }
                PreviewActivity.openActivity(getSelfActivity(), arrayList, arrayList, true, -1, 0);
                break;
        }
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
        Glide.with(getSelfActivity()).load(beans.get(0).getUrl()).into(imageView);
        this.list = beans;
    }
}
