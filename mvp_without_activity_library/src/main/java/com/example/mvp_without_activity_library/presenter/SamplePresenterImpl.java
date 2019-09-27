package com.example.mvp_without_activity_library.presenter;


import com.example.mvp_without_activity_library.contracts.SampleContracts;
import com.example.mvp_without_activity_library.module.SampleModuleImpl;
import com.example.mvp_without_activity_library.presenter.base.BasePresenter;

public class SamplePresenterImpl extends BasePresenter<SampleContracts.ISampleView> implements SampleContracts.ISamplePresenter {
   
    private SampleModuleImpl module;

    public SamplePresenterImpl(SampleContracts.ISampleView view) {
        super(view);
        module = new SampleModuleImpl();
    }

    @Override
    public void change(boolean isSuccess) {
        //判断activity是否结束，不判断的化在极端情况可能出现内存泄漏
        if (isViewAttach()) {
            boolean success = module.change(isSuccess);
            mvpRef.get().showTip(success);
        }
    }
}
