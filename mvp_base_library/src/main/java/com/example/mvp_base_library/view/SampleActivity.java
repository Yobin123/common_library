package com.example.mvp_base_library.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.mvp_base_library.contracts.SampleContracts;
import com.example.mvp_base_library.presenter.SamplePresenterImpl;
import com.example.mvp_base_library.view.base.BaseActivity;


public class SampleActivity extends BaseActivity<SamplePresenterImpl> implements SampleContracts.ISampleView {
    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        button = new Button(this);
        button.setText("true");
        layout.addView(button);
        setContentView(layout);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = button.getText().toString();
                if (s.equals("true")) {
                    mvpPre.change(true);
                } else {
                    mvpPre.change(false);
                }
            }
        });
    }

    @Override
    public void showTip(boolean isSuccess) {
        if (isSuccess) {
            button.setText("true");
        } else {
            button.setText("false");
        }
    }

    @Override
    protected SamplePresenterImpl bindPresenter() {
        return new SamplePresenterImpl(this);
    }

    @Override
    protected int onLayout() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void addListener() {

    }

    @Override
    protected void setControl() {

    }

    @Override
    public void onClick(View v) {

    }
}
